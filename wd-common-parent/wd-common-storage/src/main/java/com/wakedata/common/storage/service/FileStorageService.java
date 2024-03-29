package com.wakedata.common.storage.service;

import com.wakedata.common.storage.enums.BucketEnum;
import com.wakedata.common.storage.model.CloudStorageAuthInfoResult;
import com.wakedata.common.storage.model.UploadRequest;
import com.wakedata.common.storage.model.UploadResult;
import com.wakedata.common.storage.utils.FileStorageUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

/**
 * 对象存储接口
 *
 * @author zkz
 * @date 2021/12/8
 */
public interface FileStorageService {
    /**
     * 上传文件
     *
     * @param file 要上传的文件
     * @return 上传结果
     */
    @Deprecated
    default UploadResult upload(File file) {
        return upload(null, file);
    }

    /**
     * 上传文件
     *
     * @param request 上传请求对象
     * @param file    要上传的文件
     * @return 上传结果
     */
    default UploadResult upload(UploadRequest request, File file) {
        if (request == null) {
            request = UploadRequest.builder().build();
        }
        UploadResult uploadResult = FileStorageUtils.checkUploadFile(request, file);
        if (!uploadResult.getSuccess()) {
            return uploadResult;
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            return upload(request, inputStream);
        } catch (Exception e) {
            throw new RuntimeException("upload fail");
        }
    }

    /**
     * 上传文件
     *
     * @param request 上传请求对象
     * @param file    要上传的文件
     * @return 上传结果
     */
    default UploadResult upload(UploadRequest request, CommonsMultipartFile file) {
        if (request == null) {
            request = UploadRequest.builder().build();
        }
        UploadResult uploadResult = FileStorageUtils.checkUploadCommonFile(request, file);
        if (!uploadResult.getSuccess()) {
            return uploadResult;
        }
        try (InputStream inputStream = file.getInputStream()) {
            return upload(request, inputStream);
        } catch (Exception e) {
            throw new RuntimeException("upload fail");
        }
    }

    /**
     * 上传文件
     *
     * @param file 要上传的文件
     * @return 上传结果
     */
    @Deprecated
    default UploadResult upload(CommonsMultipartFile file) {
        return upload(null, file);
    }

    /**
     * 根据URL获取fileKey
     *
     * @param url 文件url
     * @return 文件key
     */
    String getFileKeyFromUrl(String url);

    /**
     * 上传，不推荐使用，因为InputStream没有文件名
     *
     * @param request     上传请求对象
     * @param inputStream 文件输入流
     * @return 上传对象
     */
    UploadResult upload(UploadRequest request, InputStream inputStream);

    /**
     * 获取域名集合
     *
     * @return 域名集合
     */
    Set<String> getHosts();

    /**
     * 删除文件
     *
     * @param bucket  桶 不传用默认
     * @param fileKey 需要删除的文件
     */
    void deleteFile(BucketEnum bucket, String fileKey);

    /**
     * 删除文件
     *
     * @param fileKey 需要删除的文件
     */
    @Deprecated
    default void deleteFile(String fileKey) {
        deleteFile(null, fileKey);
    }

    /**
     * 获取文件流
     *
     * @param bucketEnum 桶枚举
     * @param fileKey    文件key
     * @return 输入流
     */
    InputStream getFileStream(BucketEnum bucketEnum, String fileKey);

    /**
     * 下载云文件到指定文件，存在则覆盖
     *
     * @param bucketEnum 桶名称, 不传用默认
     * @param fileKey    需要下载的文件
     * @param outputFile 输出到该文件
     */
    void downloadFile(BucketEnum bucketEnum, String fileKey, File outputFile);

    /**
     * 下载云文件到指定文件，存在则覆盖
     *
     * @param fileKey    需要下载的文件
     * @param outputFile 输出到该文件
     */
    @Deprecated
    default void downloadFile(String fileKey, File outputFile) {
        downloadFile(null, fileKey, outputFile);
    }

    /**
     * 根据bucket和fileKey获取文件下载URL，主要针对过期的链接
     *
     * @param bucket  桶
     * @param fileKey fileKey
     * @return 上传结果
     */
    UploadResult getURL(BucketEnum bucket, String fileKey);

    /**
     * 根据fileKey获取文件下载URL，主要针对过期的链接。目前亚马逊s3 7天过期
     *
     * @param fileKey fileKey
     * @return 上传结果
     */
    @Deprecated
    default UploadResult getURL(String fileKey) {
        return getURL(null, fileKey);
    }

    /**
     * 获取云存储授权信息,用于前端直接上传至云存储
     *
     * @param bucket 要上传的桶名称
     * @return 云存储授权信息
     */
    default CloudStorageAuthInfoResult getCloudStorageAuthInfo(String bucket) {
        return null;
    }

    /**
     * 获取默认桶
     *
     * @return 默认桶名称
     */
    default String getDefaultBucket() {
        return null;
    }
}