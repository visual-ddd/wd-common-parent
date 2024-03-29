package com.wakedata.common.storage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import com.wakedata.common.storage.StorageConstants;
import com.wakedata.common.storage.config.BaseConfigProperties;
import com.wakedata.common.storage.config.BucketConfig;
import com.wakedata.common.storage.config.ObsConfigProperties;
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
import java.io.*;
import java.util.*;

/**
 * @Desc 华为云实现
 * @Author zkz
 * @Date 2021/12/8
 */
@Slf4j
@ConditionalOnProperty(prefix = "storage", name = "type", havingValue = "obs")
@EnableConfigurationProperties(ObsConfigProperties.class)
public class ObsFileStorageService implements FileStorageService {

    private ObsConfigProperties obsConfigProperties;
    private Map<String, BucketConfig> bucketConfigs;
    private ObsClient obsClient;
    private ObsClient internalObsClient;

    private Set<String> fileHosts = new HashSet<>();

    private final static String THUMBNAIL_URL_SUFFIX = "x-image-process=image/resize,w_200,h_200/auto-orient,1/quality,q_90/format,jpg";
    private final static String THUMBNAIL_STYLE_TYPE = "image/resize,w_200,h_200/auto-orient,1/quality,q_90/format,jpg";

    private final static Map<String, Object> QUERY_THUMB_PARAMS = new HashMap() {
        {
            put("x-image-process", THUMBNAIL_STYLE_TYPE);
        }
    };


    @Autowired
    public ObsFileStorageService(ObsConfigProperties properties) {
        this.obsConfigProperties = properties;
    }

    @PostConstruct
    public void init() {
        obsClient = new ObsClient(obsConfigProperties.getAccessKey(),
                obsConfigProperties.getSecretKey(), obsConfigProperties.getEndpoint());
        internalObsClient = new ObsClient(obsConfigProperties.getAccessKey(),
                obsConfigProperties.getSecretKey(), obsConfigProperties.getInternalEndpoint());
        bucketConfigs = FileStorageUtils.initBucketConfigs(obsConfigProperties, fileHosts);
        log.info("oss hosts:{}", JSON.toJSONString(fileHosts));
    }

    @Override
    public UploadResult upload(UploadRequest request, InputStream inputStream) {
        if (Objects.isNull(request)) {
            request = UploadRequest.builder().build();
        }
        UploadResult uploadResult = FileStorageUtils.checkUploadParam(request, inputStream, obsConfigProperties, bucketConfigs);
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
                internalObsClient.putObject(bucketConfig.getName(), fileKey, inputStreamT, metadataT);
            }

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(Long.valueOf(inputStream_video.available()));
            metadata.setContentDisposition(request.getDisposition());
            internalObsClient.putObject(bucketConfig.getName(), request.getFileKey(), inputStream_video, metadata);
            // 处理URL
            handleLocalUrl(bucketConfig, urb, request);
            // 处理CDN URL
            FileStorageUtils.handleCdnUrl(bucketConfig, urb, request, THUMBNAIL_URL_SUFFIX);
            urb.fileKey(request.getFileKey());
            urb.bucket(bucketConfig.getName());
            urb.success(true);
            urb.uploadMsg("上传成功");
        } catch (Exception e) {
            log.error("obs上传失败", e);
            urb.success(false).uploadMsg("上传obs文件失败");
        } finally {
            IOUtils.close(inputStreamT);
            if (Objects.nonNull(file)) {
                file.delete();
            }
            stopWatch.stop();
            log.info("obs uploadFile cost time: {}ms", stopWatch.getTotalTimeMillis());
        }
        return urb.build();
    }

    @Override
    public String getFileKeyFromUrl(String ossUrl) {
        return FileStorageUtils.getFileKeyFromUrlHandle(ossUrl, bucketConfigs, obsConfigProperties);
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
            bucketEnum = FileStorageUtils.defaultBucket(obsConfigProperties.getBucketConfigs());
        }
        long startTime = System.currentTimeMillis();
        internalObsClient.deleteObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, obsConfigProperties), fileKey);
        long endTime = System.currentTimeMillis();
        log.info("obs deleteFile used time: {}", (endTime - startTime));

    }

    @Override
    public InputStream getFileStream(BucketEnum bucketEnum, String fileKey) {
        ObsObject obsObject = internalObsClient.getObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, obsConfigProperties), fileKey);
        return obsObject.getObjectContent();
    }

    @Override
    public void downloadFile(BucketEnum bucketEnum, String fileKey, File outputFile) {
        if (StringUtils.isEmpty(fileKey)) {
            throw new RuntimeException("obs delete fail, fileKey is null");
        }
        if (Objects.isNull(bucketEnum)) {
            // 设置默认桶
            bucketEnum = FileStorageUtils.defaultBucket(obsConfigProperties.getBucketConfigs());
        }
        ObsObject obsObject = internalObsClient.getObject(FileStorageUtils.
                getBucketName(bucketEnum.getValue(), bucketConfigs, obsConfigProperties), fileKey);
        InputStream input = obsObject.getObjectContent();
        int byteCount = 0;
        byte[] bytes = new byte[1024];
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
            while ((byteCount = input.read(bytes)) > -1) {
                outputStream.write(bytes, 0, byteCount);
            }
        } catch (
        FileNotFoundException e) {
            log.error("File Not Find");
        } catch (IOException e) {
            log.error("Write To ServletOutputStream Error: {}", e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                }
                if (input != null) {
                    input.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }

            }catch (Exception e) {

            }

        }

    }

    private void handleLocalUrl(BucketConfig bucketConfig, UploadResult.UploadResultBuilder urb, UploadRequest request) {
        if (ReadPermissionEnum.PUBLIC.getName().equals(bucketConfig.getReadPermission())) {
            String pubAccessUrl = generatePublicBucketAccessUrl(bucketConfig.getName(), request.getFileKey(), obsConfigProperties);
            urb.localUrl(pubAccessUrl);
            if (request.isGenThumb()) {
                if (FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                    urb.thumbnailUrl(generatePublicBucketAccessUrl(bucketConfig.getName(), FileStorageUtils.buildVideoThumKey(request.getFileKey()), obsConfigProperties));
                }else {
                    urb.thumbnailUrl(pubAccessUrl.concat(StorageConstants.QUESTION_MARK).concat(THUMBNAIL_URL_SUFFIX));
                }
            }
        }
        if (ReadPermissionEnum.PRIVATE.getName().equals(bucketConfig.getReadPermission())) {
            urb.localUrl(generatePrivateBucketAccessUrl(bucketConfig.getName(), request.getFileKey(), false));
            if (request.isGenThumb()) {
                if (FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                    urb.thumbnailUrl(generatePrivateBucketAccessUrl(bucketConfig.getName(), FileStorageUtils.buildVideoThumKey(request.getFileKey()), false));
                }else {
                    urb.thumbnailUrl(generatePrivateBucketAccessUrl(bucketConfig.getName(), request.getFileKey(), true));
                }
            }
        }

    }
    private String generatePrivateBucketAccessUrl(String bucketName, String fileKey, boolean queryThumb) {
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, StorageConstants.EXPIRE_SECONDS);
        request.setBucketName(bucketName);
        request.setObjectKey(fileKey);
        if (queryThumb) {
            request.setQueryParams(QUERY_THUMB_PARAMS);
        }
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        return response.getSignedUrl();
    }
    /**
     * 生成公共资源URL
     * @param bucketName
     * @param fileKey
     * @param properties
     * @return
     */
    String generatePublicBucketAccessUrl(String bucketName, String fileKey, BaseConfigProperties properties) {
        StringBuilder sb = new StringBuilder();
        sb.append(StorageConstants.HTTPS_PREFIX);
        sb.append(bucketName);
        sb.append(StorageConstants.POINT);
        sb.append(properties.getEndpoint());
        sb.append(StorageConstants.SLASH);
        sb.append(fileKey);
        return sb.toString();
    }
    @Override
    public UploadResult getURL(BucketEnum bucketEnum, String fileKey) {
        UploadResult.UploadResultBuilder builder = UploadResult.builder();
        if (StringUtils.isEmpty(fileKey)) {
            log.error("fileKey is null");
            return builder.success(false).uploadMsg("fileKey is null").build();
        }
        if (Objects.isNull(bucketEnum)){
            bucketEnum = FileStorageUtils.defaultBucket(obsConfigProperties.getBucketConfigs());
        }
        UploadRequest request = UploadRequest.builder()
                .bucket(bucketEnum)
                .fileKey(fileKey)
                .genThumb(false)
                .build();
        builder.bucket(bucketConfigs.get(bucketEnum.getValue()).getName());
        builder.localUrl(generatePrivateBucketAccessUrl(bucketConfigs.get(bucketEnum.getValue()).getName(), fileKey, false));
        FileStorageUtils.handleCdnUrl(bucketConfigs.get(bucketEnum.getValue()), builder, request, null);
        return builder.build();
    }
}