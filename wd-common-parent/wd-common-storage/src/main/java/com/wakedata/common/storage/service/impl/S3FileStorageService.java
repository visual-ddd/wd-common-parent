package com.wakedata.common.storage.service.impl;

import com.alibaba.fastjson.util.IOUtils;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.wakedata.common.core.util.ImageUtil;
import com.wakedata.common.storage.StorageConstants;
import com.wakedata.common.storage.config.BucketConfig;
import com.wakedata.common.storage.config.S3ConfigProperties;
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
 * @Desc Amazon 对象存储实现
 * @Author zkz
 * @Date 2021/12/8
 */
@Slf4j
@ConditionalOnProperty(prefix = "storage", name = "type", havingValue = "s3")
@EnableConfigurationProperties(S3ConfigProperties.class)
public class S3FileStorageService implements FileStorageService {

    private S3ConfigProperties s3ConfigProperties;
    private Map<String, BucketConfig> bucketConfigs;

    private Set<String> fileHosts = new HashSet<>();

    private AmazonS3 amazonS3;
//    private final static String THUMBNAIL_URL_SUFFIX = "imageView2/1/w/200/h/200/q/90/format/jpg";
//    private final static String THUMBNAIL_STYLE_TYPE = "imageMogr2/thumbnail/200x200";

    private final static int HEIGHT = 200;
    private final static int WIDTH = 200;

    @Autowired
    public S3FileStorageService(S3ConfigProperties properties) {
        this.s3ConfigProperties = properties;
    }

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(s3ConfigProperties.getAccessKeyId())
                || StringUtils.isEmpty(s3ConfigProperties.getAccessKeySecret())) {
            throw new IllegalArgumentException(
                    "[s3] Config err!");
        }
        AWSCredentials credentials = new BasicAWSCredentials(s3ConfigProperties.getAccessKeyId(), s3ConfigProperties.getAccessKeySecret());
        // 获取地区，没有使用默认值 China (Ningxia)
        Regions region = Regions.CN_NORTHWEST_1;
        if (!StringUtils.isEmpty(s3ConfigProperties.getRegion())) {
            try {
                region = Regions.fromName(s3ConfigProperties.getRegion());
            }catch (Exception e) { }
        }

        amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        bucketConfigs = FileStorageUtils.initBucketConfigs(s3ConfigProperties, fileHosts);
    }

    @PreDestroy
    public void destroy() {
        if (amazonS3 != null) {
            amazonS3.shutdown();
            log.info("[s3] client shutdown");
        }
    }

    @Override
    public UploadResult upload(UploadRequest request, InputStream inputStream) {

        if (Objects.isNull(request)) {
            request = UploadRequest.builder().build();
        }
        UploadResult uploadResult = FileStorageUtils.checkUploadParam(request, inputStream, s3ConfigProperties, bucketConfigs);
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
            ObjectMetadata objectMetadata = new ObjectMetadata();
            ByteArrayOutputStream outputStream = FileStorageUtils.getNewInpustream(inputStream);
            InputStream inputStream_video = new ByteArrayInputStream(outputStream.toByteArray());
            InputStream inputStream_thum = new ByteArrayInputStream(outputStream.toByteArray());
            InputStream originInputStream = new ByteArrayInputStream(outputStream.toByteArray());

            if (request.isGenThumb()) {
                // 处理缩略图
                if (FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                    file = FileStorageUtils.makeVideoThumbnail(inputStream_thum);
                    inputStreamT = new FileInputStream(file);
                    ObjectMetadata metadataT = new ObjectMetadata();
                    metadataT.setContentLength(Long.valueOf(inputStreamT.available()));
                    String fileKey = FileStorageUtils.buildVideoThumKey(request.getFileKey());
                    amazonS3.putObject(bucketConfig.getName(), fileKey, inputStreamT, metadataT);
                    urb.thumbnailUrl(generateS3PrivateBucketAccessUrl(bucketConfig.getName(), fileKey, false));
                }else {
                    try {
                        String thumbFileKey = WIDTH + "X" + HEIGHT + "_" + request.getFileKey();
                        ByteArrayOutputStream outputStreamP = ImageUtil.thumbnail(WIDTH, HEIGHT, originInputStream);
                        InputStream thumbnailsInputStream = new ByteArrayInputStream(outputStreamP.toByteArray());
                        objectMetadata.setContentLength(thumbnailsInputStream.available());
                        amazonS3.putObject(bucketConfig.getName(), thumbFileKey, thumbnailsInputStream, objectMetadata);
                        urb.thumbnailUrl(generateS3PrivateBucketAccessUrl(bucketConfig.getName(), thumbFileKey, false));
                    }catch (Exception e) {
                        log.error("上传缩略图失败", e);
                    }
                }

            }
            objectMetadata.setContentLength(inputStream_video.available());
            objectMetadata.setContentDisposition(request.getDisposition());
            amazonS3.putObject(bucketConfig.getName(), request.getFileKey(), inputStream_video,objectMetadata);
            // 处理URL
            handleLocalUrl(bucketConfig, urb, request);
            // 处理 CDN URL
            FileStorageUtils.handleCdnUrl(bucketConfig, urb, request, null);
            urb.fileKey(request.getFileKey());
            urb.bucket(bucketConfig.getName());
            urb.success(true);
            urb.uploadMsg("上传成功");
        } catch (Exception e) {
            log.error("s3上传失败", e);
            urb.success(false).uploadMsg("上传s3文件失败");
        } finally {
            IOUtils.close(inputStreamT);
            if (Objects.nonNull(file)) {
                file.delete();
            }
            stopWatch.stop();
            log.info("s3 uploadFile cost time: {}ms", stopWatch.getTotalTimeMillis());
        }
        return urb.build();

    }
    /**
     * 处理 url
     * @return
     */
    void handleLocalUrl(BucketConfig bucketConfig, UploadResult.UploadResultBuilder urb,UploadRequest request) {
        if (ReadPermissionEnum.PUBLIC.getName().equals(bucketConfig.getReadPermission())) {
            String pubAccessUrl = generateS3PublicBucketAccessUrl(bucketConfig.getName(), request.getFileKey(), s3ConfigProperties);
            urb.localUrl(pubAccessUrl);
        }
        if (ReadPermissionEnum.PRIVATE.getName().equals(bucketConfig.getReadPermission())) {
            String signedUrl = generateS3PrivateBucketAccessUrl(bucketConfig.getName(), request.getFileKey(), false);
            urb.localUrl(signedUrl);
        }
    }
    private String generateS3PrivateBucketAccessUrl(String bucketName, String fileKey, boolean queryThumb) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, fileKey).withMethod(HttpMethod.GET);
        req.setExpiration(StorageConstants.EXPIRE_DAY_7);
        URL url = amazonS3.generatePresignedUrl(req);
        return url.toString();
    }

    private String generateS3PublicBucketAccessUrl(String bucketName, String fileKey, S3ConfigProperties s3ConfigProperties) {
        return amazonS3.getUrl(bucketName, fileKey).toString();
    }

    @Override
    public String getFileKeyFromUrl(String ossUrl) {
        return FileStorageUtils.getFileKeyFromUrlHandle(ossUrl, bucketConfigs, s3ConfigProperties);
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
            bucketEnum = FileStorageUtils.defaultBucket(s3ConfigProperties.getBucketConfigs());
        }
        long startTime = System.currentTimeMillis();
        amazonS3.deleteObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, s3ConfigProperties), fileKey);
        long endTime = System.currentTimeMillis();
        log.info("s3 deleteFile used time: {}", (endTime - startTime));
    }

    @Override
    public InputStream getFileStream(BucketEnum bucketEnum, String fileKey) {
        S3Object s3Object = amazonS3.getObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, s3ConfigProperties), fileKey);
        return s3Object.getObjectContent().getDelegateStream();
    }

    @Override
    public void downloadFile(BucketEnum bucketEnum, String fileKey, File outputFile) {
        if (StringUtils.isEmpty(fileKey)) {
            throw new RuntimeException("obs delete fail, fileKey is null");
        }
        if (Objects.isNull(bucketEnum)) {
            // 设置默认桶
            bucketEnum = FileStorageUtils.defaultBucket(s3ConfigProperties.getBucketConfigs());
        }
        long startTime = System.currentTimeMillis();
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建
        amazonS3.getObject(new GetObjectRequest(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, s3ConfigProperties), fileKey), outputFile);
        long endTime = System.currentTimeMillis();
        log.info("s3 downloadFile used time: {}", (endTime - startTime));
    }

    @Override
    public UploadResult getURL(BucketEnum bucketEnum, String fileKey) {
        UploadResult.UploadResultBuilder builder = UploadResult.builder();
        if (StringUtils.isEmpty(fileKey)) {
            log.error("fileKey is null");
            return builder.success(false).uploadMsg("fileKey is null").build();
        }
        if (Objects.isNull(bucketEnum)){
            bucketEnum = FileStorageUtils.defaultBucket(s3ConfigProperties.getBucketConfigs());
        }
        UploadRequest request = UploadRequest.builder()
                .bucket(bucketEnum)
                .fileKey(fileKey)
                .genThumb(false)
                .build();

        builder.localUrl(generateS3PrivateBucketAccessUrl(bucketConfigs.get(bucketEnum.getValue()).getName(), fileKey, false));
        builder.bucket(bucketConfigs.get(bucketEnum.getValue()).getName());
        FileStorageUtils.handleCdnUrl(bucketConfigs.get(bucketEnum.getValue()), builder, request, null);
        return builder.build();
    }
}
