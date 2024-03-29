package com.wakedata.common.storage.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 获取云存储授权信息的响应对象,用于前端直接上传文件
 *
 * @author zhuhao
 * @date 2022/4/24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CloudStorageAuthInfoResult {
    /***云存储应用ID*/
    String accessKeyId;
    /***云存储应用秘钥*/
    String accessKeySecret;
    /***存储的桶名称*/
    String bucket;
    /***存储的区域*/
    String region;
    /***临时访问token*/
    String token;
    /***临时token过期时间*/
    String expiration;
}
