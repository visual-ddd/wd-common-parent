package com.wakedata.common.storage.utils;


import cn.hutool.core.io.FileUtil;
import com.wakedata.common.core.exception.BizException;
import com.wakedata.common.core.util.BaseEnumUtil;
import com.wakedata.common.core.util.FfmpegUtil;
import com.wakedata.common.storage.StorageConstants;
import com.wakedata.common.storage.config.BaseConfigProperties;
import com.wakedata.common.storage.config.BucketConfig;
import com.wakedata.common.storage.enums.BucketEnum;
import com.wakedata.common.storage.model.UploadRequest;
import com.wakedata.common.storage.model.UploadResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @Desc
 * @Author zkz
 * @Date 2021/12/9
 */
@Slf4j
public class FileStorageUtils {

    private final static String VIDEO_THUMBNAIL_SUFFIX = "VIDEO_THUMBNAIL";


    public static String getFileKeyFromUrlHandle(String ossUrl, Map<String, BucketConfig> bucketConfigs, BaseConfigProperties properties) {
        if (StringUtils.isEmpty(ossUrl)) {
            return null;
        }
        try {
            URL url = new URL(ossUrl);
            String host = url.getHost();
            if (host.equals(bucketConfigs.get(defaultBucket(properties.getBucketConfigs()).getValue()).getCdnUrl())) {
                String fileKey = ossUrl.substring(ossUrl.lastIndexOf(StorageConstants.SLASH));
                return fileKey;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String buildFileKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 返回默认的桶，没有设置默认则直接报错，设置多个返回第一个
     * @param bucketConfigs
     * @return
     */
    public static BucketEnum defaultBucket(List<BucketConfig> bucketConfigs){
        if (CollectionUtils.isEmpty(bucketConfigs)) {
            throw new RuntimeException("bucket is not config");
        }
        for (BucketConfig b: bucketConfigs) {
            if (b.getIsDefault() != null && b.getIsDefault()) {
                return BaseEnumUtil.getEnumByValue(b.getKey(), BucketEnum.class);
            }
        }
        log.error("default bucket is not config");
        throw new RuntimeException("default bucket is not config");
    }

    public static UploadResult checkUploadParam(UploadRequest request, InputStream inputStream,
                                          BaseConfigProperties properties, Map<String, BucketConfig> bucketConfigs) {
        if (inputStream == null) {
            return UploadResult.builder().success(false).uploadMsg("上传文件不能为空").build();
        }
        if (request.getBucket() == null) {
            request.setBucket(defaultBucket(properties.getBucketConfigs()));
        }
        if (StringUtils.isEmpty(request.getFileKey())) {
            request.setFileKey(buildFileKey());
        }
        if (StringUtils.isEmpty(request.getFileName())) {
            request.setFileName(request.getFileKey());
        }
        if (StringUtils.isEmpty(request.getDisposition())) {
            request.setDisposition(properties.buildDisposition(request.getFileName()));
        }
        if (request.getBucket() == null || bucketConfigs.get(request.getBucket().getValue()) == null) {
            log.warn("bucket未配置, 获取默认的");
            request.setBucket(defaultBucket(properties.getBucketConfigs()));

        }
        return UploadResult.builder().bucket(request.getBucket().getValue()).success(true).build();
    }

    public static UploadResult checkUploadFile(UploadRequest request, File file) {
        if (file == null) {
            return UploadResult.builder().success(false).uploadMsg("上传文件不能为空").build();
        }
        if (StringUtils.isEmpty(request.getFileName())) {
            request.setFileName(file.getName());
        }
        return UploadResult.builder().success(true).build();
    }

    public static UploadResult checkUploadCommonFile(UploadRequest request, CommonsMultipartFile file) {
        if (file == null) {
            return UploadResult.builder().success(false).uploadMsg("上传文件不能为空").build();
        }
        if (StringUtils.isEmpty(request.getFileName())) {
            request.setFileName(file.getOriginalFilename());
        }
        return UploadResult.builder().success(true).build();
    }

    public static Map<String, BucketConfig> initBucketConfigs(BaseConfigProperties properties, Set<String> fileHosts) {
        Map<String, BucketConfig> bucketConfigs = new HashMap<>();
        properties.getBucketConfigs().forEach(x -> {
            bucketConfigs.put(x.getKey(), x);
            if (StringUtils.isNotEmpty(x.getCdnUrl())) {
                try {
                    URL url = new URL(x.getCdnUrl());
                    String host = url.getHost();
                    fileHosts.add(host);
                } catch (MalformedURLException e) {
                    log.error("error", e);
                }
            }
            fileHosts.add(x.getName() + StorageConstants.POINT + properties.getEndpoint());
        });
        return bucketConfigs;
    }

    /**
     * 替换成cdn域名
     * @return
     */
    public static String repalceCndDomain(String localUrl, String cdnDomian) {
        // 把原来域名替换成cdn域名
        String[] split = localUrl.split("/");
        String domian = split[2];
        StringBuilder cdnUrl = new StringBuilder(cdnDomian);
        for (int i=3; i<split.length; i++) {
            cdnUrl.append(StorageConstants.SLASH).append(split[i]);
        }
        return cdnUrl.toString();
    }

    /**
     * 处理cdn url
     * @return
     */
    public static void handleCdnUrl(BucketConfig bucketConfig, UploadResult.UploadResultBuilder urb, UploadRequest request, String thumbnailUrlSuffix) {
        if (StringUtils.isNotEmpty(bucketConfig.getCdnUrl())) {
            try {
                String cdnUrl = FileStorageUtils.repalceCndDomain(urb.build().getLocalUrl(), bucketConfig.getCdnUrl());
                urb.cdnUrl(cdnUrl);
            }catch (Exception e) {
                log.error("handle cdn url fail ", e);
            }

        }
    }

    public static String getBucketName(String bucketKey, Map<String, BucketConfig> bucketConfigs, BaseConfigProperties configProperties) {
        return StringUtils.isNotBlank(bucketKey) ? bucketConfigs.get(bucketKey).getName():
                bucketConfigs.get(FileStorageUtils.defaultBucket(configProperties.getBucketConfigs()).getValue()).getName();
    }

    public static File makeVideoThumbnail(InputStream inputStream) {
        String tmp = "/tmp/";
        String thumbnail = tmp + System.currentTimeMillis() + UUID.randomUUID().toString() + ".jpg";
        File targetFile = FileUtil.writeFromStream(inputStream, new FileStorageUtils().getClass().getResource("/").getPath() + "/tmp/" + UUID.randomUUID().toString());
		try {
            FfmpegUtil.makeThumbnail(targetFile.getAbsolutePath(), thumbnail);
            File destFile = new File(thumbnail);
            return destFile;
        }catch (Exception e) {
		  log.error("生成缩略图异常", e);
		  throw new BizException("生成缩略图异常");
        } finally {
            targetFile.delete();
        }
    }

    public static String buildVideoThumKey(String key) {
        return key.concat(VIDEO_THUMBNAIL_SUFFIX);
    }

    public static ByteArrayOutputStream getNewInpustream(InputStream inputStream) {
        ByteArrayOutputStream writeoutputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                writeoutputStream.write(buffer, 0, len);
            }
            writeoutputStream.flush();
            return writeoutputStream;
        } catch (IOException e) {
            log.error("fail", e);
            throw new BizException("fail");
        } finally {
            try {
                writeoutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
