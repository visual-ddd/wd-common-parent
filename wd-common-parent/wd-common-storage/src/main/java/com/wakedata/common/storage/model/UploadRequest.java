package com.wakedata.common.storage.model;

import com.wakedata.common.storage.enums.BucketEnum;
import com.wakedata.common.storage.enums.FileTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @Desc 上传文件参数
 * @Author zkz
 * @Date 2021/12/8
 */
@Data
@Builder
public class UploadRequest {
    /**
     * 文件名，建议传参
     */
    private String fileName;
    /**
     * 文件唯一标识，建议传参且同一个buctet不重复，不传会生成UUID但是没有后缀名
     */
    private String fileKey;
    /**
     * 指定该Object被下载时的名称，建议不包含中文
     */
    String disposition;
    /**
     * 需要上传的桶（需要和配置文件（-key）一致），不传会用默认的
     */
    BucketEnum bucket;
    /**
     * 是否需要生成缩略图（200*200），只针对图片/视频类型,视频截取第一帧
     */
    boolean genThumb;

    /**
     * 文件类型枚举
     */
    FileTypeEnum fileTypeEnum;
}