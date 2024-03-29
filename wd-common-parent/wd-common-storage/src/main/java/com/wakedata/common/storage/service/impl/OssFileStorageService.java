package com.wakedata.common.storage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.wakedata.common.storage.StorageConstants;
import com.wakedata.common.storage.config.BaseConfigProperties;
import com.wakedata.common.storage.config.BucketConfig;
import com.wakedata.common.storage.config.OssConfigProperties;
import com.wakedata.common.storage.enums.BucketEnum;
import com.wakedata.common.storage.enums.FileTypeEnum;
import com.wakedata.common.storage.enums.ReadPermissionEnum;
import com.wakedata.common.storage.model.CloudStorageAuthInfoResult;
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
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 阿里云实现
 *
 * @author zkz
 * @date 2021/12/8
 */
@Slf4j
@EnableConfigurationProperties(OssConfigProperties.class)
@ConditionalOnProperty(prefix = "storage", name = "type", havingValue = "oss", matchIfMissing = false)
public class OssFileStorageService implements FileStorageService {

    private OssConfigProperties ossConfigProperties;
    private Map<String, BucketConfig> bucketConfigs;

    private OSSClient internalOssClient;
    private OSSClient ossClient;

    private Set<String> fileHosts = new HashSet<>();

    private final static String THUMBNAIL_URL_SUFFIX = "x-oss-process=image/resize,w_200,h_200/auto-orient,1/quality,q_90/format,jpg";
    private final static String THUMBNAIL_STYLE_TYPE = "image/resize,w_200,h_200/auto-orient,1/quality,q_90/format,jpg";

    private final String ROLE_SESSION_NAME = "oss-uploader";

    @Autowired
    public OssFileStorageService(OssConfigProperties properties) {
        this.ossConfigProperties = properties;
    }

    @PostConstruct
    public void init() {
        internalOssClient = new OSSClient(ossConfigProperties.getInternalEndpoint(),
                ossConfigProperties.getSecretId(),
                ossConfigProperties.getSecretKey());

        ossClient = new OSSClient(ossConfigProperties.getEndpoint(),
                ossConfigProperties.getSecretId(),
                ossConfigProperties.getSecretKey());
        bucketConfigs = FileStorageUtils.initBucketConfigs(ossConfigProperties, fileHosts);
        log.info("oss hosts:{}", JSON.toJSONString(fileHosts));
    }

    @Override
    public String getFileKeyFromUrl(String ossUrl) {
        return FileStorageUtils.getFileKeyFromUrlHandle(ossUrl, bucketConfigs, ossConfigProperties);
    }

    @Override
    public UploadResult upload(UploadRequest request, InputStream inputStream) {
        if (Objects.isNull(request)) {
            request = UploadRequest.builder().build();
        }
        // 校验参数
        UploadResult checkRes = FileStorageUtils.checkUploadParam(request, inputStream, ossConfigProperties, bucketConfigs);
        if (!checkRes.getSuccess()) {
            return checkRes;
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
                internalOssClient.putObject(bucketConfig.getName(), fileKey, inputStreamT, metadataT);
            }

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(inputStream_video.available());
            metadata.setContentDisposition(request.getDisposition());
            internalOssClient.putObject(bucketConfig.getName(), request.getFileKey(), inputStream_video, metadata);
            // 处理URL
            handleLocalUrl(bucketConfig, urb, request);
            // 处理CND URL
            FileStorageUtils.handleCdnUrl(bucketConfig, urb, request, THUMBNAIL_URL_SUFFIX);
            urb.fileKey(request.getFileKey());
            urb.bucket(bucketConfig.getName());
            urb.success(true);
            urb.uploadMsg("上传成功");
        } catch (Exception e) {
            log.error("oss上传失败", e);
            urb.success(false).uploadMsg("上传oss文件失败");
        } finally {
            IOUtils.close(inputStreamT);
            if (Objects.nonNull(file)) {
                file.delete();
            }
            stopWatch.stop();
            log.info("oss uploadFile cost time: {}ms", stopWatch.getTotalTimeMillis());
        }
        return urb.build();
    }


    /**
     * 处理cdn url
     *
     * @return
     */
    private void handleLocalUrl(BucketConfig bucketConfig, UploadResult.UploadResultBuilder urb, UploadRequest request) {
        if (ReadPermissionEnum.PUBLIC.getName().equals(bucketConfig.getReadPermission())) {
            String pubAccessUrl = generatePublicBucketAccessUrl(bucketConfig.getName(), request.getFileKey(), ossConfigProperties);
            urb.localUrl(pubAccessUrl);
            if (request.isGenThumb()) {
                if (FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                    String s = generatePublicBucketAccessUrl(bucketConfig.getName(), FileStorageUtils.buildVideoThumKey(request.getFileKey()), ossConfigProperties);
                    urb.thumbnailUrl(s);
                } else {
                    urb.thumbnailUrl(pubAccessUrl.concat(StorageConstants.QUESTION_MARK).concat(THUMBNAIL_URL_SUFFIX));
                }
            }
        }
        if (ReadPermissionEnum.PRIVATE.getName().equals(bucketConfig.getReadPermission())) {
            String signedUrl = ossClient.generatePresignedUrl(bucketConfig.getName(), request.getFileKey(), StorageConstants.EXPIRE_DAY).toString();
            urb.localUrl(signedUrl);
            if (request.isGenThumb()) {
                if (FileTypeEnum.VIDEO.equals(request.getFileTypeEnum())) {
                    urb.thumbnailUrl(ossClient.generatePresignedUrl(bucketConfig.getName(), FileStorageUtils.buildVideoThumKey(request.getFileKey()), StorageConstants.EXPIRE_DAY).toString());
                } else {
                    GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketConfig.getName(), request.getFileKey(), HttpMethod.GET);
                    req.setExpiration(StorageConstants.EXPIRE_DAY);
                    req.setProcess(THUMBNAIL_STYLE_TYPE);
                    URL thumbnailSignedUrl = ossClient.generatePresignedUrl(req);
                    urb.thumbnailUrl(thumbnailSignedUrl.toString());
                }

            }
        }
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
            bucketEnum = FileStorageUtils.defaultBucket(ossConfigProperties.getBucketConfigs());
        }
        long startTime = System.currentTimeMillis();
        // 删除OSS文件
        internalOssClient.deleteObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, ossConfigProperties), fileKey);
        long endTime = System.currentTimeMillis();
        log.info("oss deleteFile used time: {}", (endTime - startTime));
    }

    @Override
    public InputStream getFileStream(BucketEnum bucketEnum, String fileKey) {
        OSSObject ossObject = internalOssClient.getObject(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, ossConfigProperties), fileKey);
        return ossObject.getObjectContent();
    }

    @Override
    public void downloadFile(BucketEnum bucketEnum, String fileKey, File outputFile) {
        if (StringUtils.isEmpty(fileKey)) {
            throw new RuntimeException("obs delete fail, fileKey is null");
        }
        if (Objects.isNull(bucketEnum)) {
            // 设置默认桶
            bucketEnum = FileStorageUtils.defaultBucket(ossConfigProperties.getBucketConfigs());
        }
        long startTime = System.currentTimeMillis();
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建
        internalOssClient.getObject(new GetObjectRequest(FileStorageUtils.getBucketName(bucketEnum.getValue(), bucketConfigs, ossConfigProperties), fileKey), outputFile);
        long endTime = System.currentTimeMillis();
        log.info("oss downloadFile used time: {}", (endTime - startTime));
    }

    /**
     * 生成公共资源URL
     *
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
        if (Objects.isNull(bucketEnum)) {
            bucketEnum = FileStorageUtils.defaultBucket(ossConfigProperties.getBucketConfigs());
        }
        UploadRequest request = UploadRequest.builder()
                .bucket(bucketEnum)
                .fileKey(fileKey)
                .genThumb(false)
                .build();
        String signedUrl = ossClient.generatePresignedUrl(bucketConfigs.get(bucketEnum.getValue()).getName(),
                request.getFileKey(), StorageConstants.EXPIRE_DAY).toString();
        builder.localUrl(signedUrl);
        builder.bucket(bucketConfigs.get(bucketEnum.getValue()).getName());
        FileStorageUtils.handleCdnUrl(bucketConfigs.get(bucketEnum.getValue()), builder, request, null);
        return builder.build();
    }


    @Override
    public CloudStorageAuthInfoResult getCloudStorageAuthInfo(String bucketName) {
        DefaultProfile profile = DefaultProfile.getProfile(ossConfigProperties.getRegionId(), ossConfigProperties.getSecretId(), ossConfigProperties.getSecretKey());
        try {
            IAcsClient client = new DefaultAcsClient(profile);
            AssumeRoleRequest request = new AssumeRoleRequest();
            //构造请求，设置参数。关于参数含义和设置方法，请参见《API参考》。
            request.setRoleArn(ossConfigProperties.getRoleArn());
            request.setRoleSessionName(ROLE_SESSION_NAME);
            //发起请求，并得到响应。
            AssumeRoleResponse response = client.getAcsResponse(request);
            if (response == null || response.getCredentials() == null) {
                log.error("获取对象存储授权信息失败,SecretId:{} RegionId:{} RoleArn:{}",
                        ossConfigProperties.getSecretId(), ossConfigProperties.getRegionId(),
                        ossConfigProperties.getRoleArn());
                return null;
            }
            CloudStorageAuthInfoResult result = new CloudStorageAuthInfoResult();
            result.setAccessKeyId(response.getCredentials().getAccessKeyId());
            result.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
            result.setToken(response.getCredentials().getSecurityToken());
            result.setExpiration(response.getCredentials().getExpiration());
            result.setRegion("oss-" + ossConfigProperties.getRegionId());
            if (bucketName != null) {
                result.setBucket(bucketName);
            } else {
                result.setBucket(getDefaultBucket());
            }
            return result;
        } catch (Exception e) {
            log.error("获取对象存储授权信息失败:", e);
        }
        return FileStorageService.super.getCloudStorageAuthInfo(bucketName);
    }

    @Override
    public String getDefaultBucket() {
        for (Map.Entry<String, BucketConfig> bucketConfigEntry : bucketConfigs.entrySet()) {
            if (bucketConfigEntry.getValue().getIsDefault()) {
                return bucketConfigEntry.getValue().getKey();
            }
        }
        return null;
    }
}