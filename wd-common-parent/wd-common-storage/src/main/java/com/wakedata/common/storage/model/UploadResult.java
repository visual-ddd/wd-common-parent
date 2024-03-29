package com.wakedata.common.storage.model;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * @Desc 上传文件返回结果
 * @Author zkz
 * @Date 2021/12/8
 */
@Data
@Builder
@ToString
public class UploadResult {
    private Boolean success;
    /**
     * 文件唯一标识
     */
    private String fileKey;
    /**
     * 文件下载URL
     */
    private String localUrl;
    /**
     * CDN URL
     */
    private String cdnUrl;
    /**
     * 描述
     */
    private String uploadMsg;
    /**
     * 200*200的缩略图，只有genThumb参数为true才返回
     */
    private String thumbnailUrl;

    private String bucket;

    public String getFileUrl(){
        if (StringUtils.isEmpty(cdnUrl)) {
            return localUrl;
        }
        return cdnUrl;
    }
}
