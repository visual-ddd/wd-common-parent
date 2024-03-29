package com.wakedata.common.storage.service.impl;

import com.alibaba.fastjson.util.IOUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.wakedata.common.storage.StorageConstants;
import com.wakedata.common.storage.config.BucketConfig;
import com.wakedata.common.storage.config.CosConfigProperties;
import com.wakedata.common.storage.enums.BucketEnum;
import com.wakedata.common.storage.enums.FileTypeEnum;
import com.wakedata.common.storage.enums.ReadPermissionEnum;
import com.wakedata.common.storage.model.UploadRequest;
import com.wakedata.common.storage.model.UploadResult;
import com.wakedata.common.storage.service.FileStorageService;
import com.wakedata.common.storage.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Desc 腾讯云
 * @Author zkz
 * @Date 2021/12/8
 */
@Slf4j
@ConditionalOnProperty(prefix = "storage", name = "type", havingValue = "cos")
@EnableConfigurationProperties(CosConfigProperties.class)
public class CosFileStorageService implements FileStorageService {

    private CosConfigProperties cosConfigProperties;
    private Map<String, BucketConfig> bucketConfigs;

    private Set<String> fileHosts = new HashSet<>();

    private COSClient cosClient;
    private COSClient internalCosClient;
    private final static String THUMBNAIL_URL_SUFFIX = "imageView2/1/w/200/h/200/q/90/format/jpg";
//    private final static String THUMBNAIL_STYLE_TYPE = "imageMogr2/thumbnail/200x200";
    private final static String CLOUD_TYPE = "cos";
    private final static String MYQCLOUD = ".myqcloud.com";
    @Autowired
    public CosFileStorageService(CosConfigProperties properties) {
        this.cosConfigProperties = properties;
    }

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(cosConfigProperties.getEndpoint())
                || StringUtils.isEmpty(cosConfigProperties.getAccessKeyId())
                || StringUtils.isEmpty(cosConfigProperties.getAccessKeySecret())) {
            throw new IllegalArgumentException(
                    "[cos] Config err!");
        }
        COSCredentials cred = new BasicCOSCredentials(cosConfigProperties.getAccessKeyId(), cosConfigProperties.getAccessKeySecret());
        cosClient = new COSClient(cred, new ClientConfig(new Region(cosConfigProperties.getEndpoint())));
        internalCosClient = new COSClient(cred, new ClientConfig(new Region(cosConfigProperties.getInternalEndpoint())));
        bucketConfigs = FileStorageUtils.initBucketConfigs(cosConfigProperties, fileHosts);
    }

    @PreDestroy
    public void destroy() {
        if (cosClient != null) {
            cosClient.shutdown();
            log.info("[cos] client shutdown");
        }
    }

    @Override
    public UploadResult upload(UploadRequest request, InputStream inputStream) {
        if (Objects.isNull(request)) {
            request = UploadRequest.builder().build();
        }
        UploadResult uploadResult = FileStorageUtils.checkUploadParam(request, inputStream, cosConfigProperties, bucketConfigs);
        if (!uploadResult.getSuccess()) {
            return uploadResult;
        }
        BucketConfig bucketConfig = bucketConfigs.get(request.getBucket().getValue());
        UploadResult.UploadResultBuilder urb = UploadResult.builder();
        StopWatch stopWatch = new StopWatch();
        File file = null;
        InputStream inputStreamT = null;
        try {
            stopWatch.start();

            ByteArrayOutputStream outputStream = FileStorageUtils.getNewInpustream(inputStream);
            InputStream inputStream_video = new ByteArrayInputStream(outputStream.toByteArray());
            InputStream inputStream_thum = new ByteArrayInputStream(outputStream.toByteArray());
            if (request.isGenThumb() && FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                // 上传视频缩略图
                file = FileStorageUtils.makeVideoThumbnail(inputStream_thum);
                inputStreamT = new FileInputStream(file);
                ObjectMetadata metadataT = new ObjectMetadata();
                metadataT.setContentLength(Long.valueOf(inputStreamT.available()));
                String fileKey = FileStorageUtils.buildVideoThumKey(request.getFileKey());
                PutObjectRequest putObjectRequestT = new PutObjectRequest(bucketConfig.getName(), fileKey, inputStreamT, metadataT);
                internalCosClient.putObject(putObjectRequestT);
            }

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(Long.valueOf(inputStream_video.available()));
            metadata.setContentDisposition(request.getDisposition());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketConfig.getName(), request.getFileKey(), inputStream_video, metadata);
            internalCosClient.putObject(putObjectRequest);

            // 处理URL
            handleLocalUrl(bucketConfig, urb, request);
            // 处理CDN URL
            FileStorageUtils.handleCdnUrl(bucketConfig, urb, request, THUMBNAIL_URL_SUFFIX);
            urb.fileKey(request.getFileKey());
            urb.bucket(bucketConfig.getName());
            urb.success(true);
            urb.uploadMsg("上传成功");
        } catch (Exception e) {
            log.error("cos上传失败", e);
            urb.success(false).uploadMsg("上传cos文件失败");
        } finally {
            IOUtils.close(inputStreamT);
            if (Objects.nonNull(file)) {
                file.delete();
            }
            stopWatch.stop();
            log.info("cos uploadFile cost time: {}ms", stopWatch.getTotalTimeMillis());
        }
        return urb.build();

    }

    private void handleLocalUrl(BucketConfig bucketConfig, UploadResult.UploadResultBuilder urb, UploadRequest request) {
        if (ReadPermissionEnum.PUBLIC.getName().equals(bucketConfig.getReadPermission())) {
            String pubAccessUrl = generateCosPublicBucketAccessUrl(bucketConfig.getName(), request.getFileKey(), cosConfigProperties);
            urb.localUrl(pubAccessUrl);
            if (request.isGenThumb()) {
                if (FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                    urb.thumbnailUrl(generateCosPublicBucketAccessUrl(bucketConfig.getName(), FileStorageUtils.buildVideoThumKey(request.getFileKey()), cosConfigProperties));
                }else {
                    urb.thumbnailUrl(pubAccessUrl.concat(StorageConstants.QUESTION_MARK).concat(THUMBNAIL_URL_SUFFIX));
                }
            }
        }
        if (ReadPermissionEnum.PRIVATE.getName().equals(bucketConfig.getReadPermission())) {
            String signedUrl = generateCosPrivateBucketAccessUrl(bucketConfig.getName(), request.getFileKey());
            urb.localUrl(signedUrl);
            if (request.isGenThumb()) {
                if (FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                    urb.thumbnailUrl(generateCosPrivateBucketAccessUrl(bucketConfig.getName(), FileStorageUtils.buildVideoThumKey(request.getFileKey())));
                }else {
                    String thumbUrl = generateCosPrivateBucketAccessUrl(bucketConfig.getName(), request.getFileKey());
                    urb.thumbnailUrl(thumbUrl.concat(StorageConstants.AND_MARK).concat(THUMBNAIL_URL_SUFFIX));

                }
            }
        }
    }

    private String generateCosPrivateBucketAccessUrl(String bucketName, String fileKey) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, fileKey, HttpMethodName.GET);
        req.setExpiration(StorageConstants.EXPIRE_DAY);

        URL url = cosClient.generatePresignedUrl(req);
        return url.toString();
    }

    private String generateCosPublicBucketAccessUrl(String bucketName, String fileKey, CosConfigProperties cosConfigProperties) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(StorageConstants.HTTPS_PREFIX)
                .append(bucketName)
                .append(StorageConstants.POINT)
                .append(CLOUD_TYPE)
                .append(StorageConstants.POINT)
                .append(cosConfigProperties.getEndpoint())
                .append(MYQCLOUD)
                .append(StorageConstants.SLASH)
                .append(fileKey);
        return urlBuilder.toString();
    }

    @Override
    public String getFileKeyFromUrl(String ossUrl) {
        return FileStorageUtils.getFileKeyFromUrlHandle(ossUrl, bucketConfigs, cosConfigProperties);
    }

    @Override
    public Set<String> getHosts() {
        return fileHosts;
    }

    @Override
    public void deleteFile(BucketEnum bucketEnum, String fileKey) {
        if (StringUtils.isEmpty(fileKey)) {
            throw new RuntimeException("obs delete fail, fileKey is null");
        }
        if (Objects.isNull(bucketEnum)) {
            // 设置默认桶
            bucketEnum = FileStorageUtils.defaultBucket(cosConfigProperties.getBucketConfigs());
        }
        long startTime = System.currentTimeMillis();

        internalCosClient.deleteObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, cosConfigProperties), fileKey);
        long endTime = System.currentTimeMillis();
        log.info("cos deleteFile used time: {}", (endTime - startTime));
    }

    @Override
    public InputStream getFileStream(BucketEnum bucketEnum, String fileKey) {
        COSObject cosObject = internalCosClient.getObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, cosConfigProperties), fileKey);
        return cosObject.getObjectContent();
    }

    @Override
    public void downloadFile(BucketEnum bucketEnum, String fileKey, File outputFile) {
        if (StringUtils.isEmpty(fileKey)) {
            throw new RuntimeException("obs delete fail, fileKey is null");
        }
        if (Objects.isNull(bucketEnum)) {
            // 设置默认桶
            bucketEnum = FileStorageUtils.defaultBucket(cosConfigProperties.getBucketConfigs());
        }
        long startTime = System.currentTimeMillis();
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建
        internalCosClient.getObject(new GetObjectRequest(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, cosConfigProperties), fileKey), outputFile);
        long endTime = System.currentTimeMillis();
        log.info("cos downloadFile used time: {}", (endTime - startTime));
    }

    @Override
    public UploadResult getURL(BucketEnum bucket, String fileKey) {
        UploadResult.UploadResultBuilder builder = UploadResult.builder();
        if (StringUtils.isEmpty(fileKey)) {
            log.error("fileKey is null");
            return builder.success(false).uploadMsg("fileKey is null").build();
        }
        if (Objects.isNull(bucket)){
            bucket = FileStorageUtils.defaultBucket(cosConfigProperties.getBucketConfigs());
        }
        UploadRequest request = UploadRequest.builder()
                .bucket(bucket)
                .fileKey(fileKey)
                .genThumb(false)
                .build();

        builder.localUrl(generateCosPrivateBucketAccessUrl(bucketConfigs.get(bucket.getValue()).getName(), fileKey));
        builder.bucket(bucketConfigs.get(bucket.getValue()).getName());
        FileStorageUtils.handleCdnUrl(bucketConfigs.get(bucket.getValue()), builder, request, null);
        return builder.build();
    }
}
