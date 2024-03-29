## 对象存储
目前已接入 oss(阿里云)，obs(华为云)，cos(腾讯云)，aws s3(亚马逊)。可支持文件上传，下载，删除，获取文件下载URL（针对已过期链接）。其中图片上传时可支持返回缩略图URL

## 使用方式
**具体使用方法可参考测试用例，*FileStorageTest.java*，*test/resources/application.yml***

**1、按照如下配置**

```yaml
storage:
  type: obs # 使用的存储类型，可配置oss, obs, cos, s3。配置后自动注入对应实现类

# 下面是具体存储类型的配置
aliyun: # 阿里云
  oss:
    internalEndpoint: 内网域名，没有可和endpoint一致
    endpoint: 外网域名，如：oss-cn-shenzhen.aliyuncs.com
    secretId:
    secretKey: 
    bucket-configs: # 桶配置
      - key: 
        readPermission: private或public
        name: 桶名称
        cdnUrl: CDN域名，如：https://mhealth-cdn-test.yuexiuproperty.cn
        isDefault: true or false 是否默认桶（必须有一个默认桶）
      - key: 
        readPermission: 
        name: 
        cdnUrl:
        isDefault:

huawei: # 华为云
  obs:
    internalEndpoint: 内网域名，没有可和endpoint一致
    endpoint: 外网域名，如：oss-cn-shenzhen.aliyuncs.com
    accessKey:
    secretKey:
    bucket-configs: # 桶配置
      - key:
        readPermission: private或public
        name: 桶名称
        cdnUrl: CDN域名，如：https://mhealth-cdn-test.yuexiuproperty.cn
        isDefault: true or false 是否默认桶（必须有一个默认桶）
      - key:
        readPermission:
        name:
        cdnUrl:
        isDefault:



tencent: # 腾讯云
  cos:
    internalEndpoint: 内网域名，没有可和endpoint一致
    endpoint: 外网域名，如：oss-cn-shenzhen.aliyuncs.com
    accessKeyId:
    accessKeySecret:
    bucket-configs: # 桶配置
      - key:
        readPermission: private或public
        name: 桶名称
        cdnUrl: CDN域名，如：https://mhealth-cdn-test.yuexiuproperty.cn
        isDefault: true or false 是否默认桶（必须有一个默认桶）
      - key:
        readPermission:
        name:
        cdnUrl:
        isDefault:



amazon: # 亚马逊
  s3:
    internalEndpoint: 内网域名，没有可和endpoint一致
    endpoint: 外网域名，如：oss-cn-shenzhen.aliyuncs.com
    accessKeyId:
    accessKeySecret:
    region: 不填默认 cn-northwest-1
    bucket-configs: # 桶配置
      - key:
        readPermission: private或public
        name: 桶名称
        cdnUrl: CDN域名，如：https://mhealth-cdn-test.yuexiuproperty.cn
        isDefault: true or false 是否默认桶（必须有一个默认桶）
      - key:
        readPermission:
        name:
        cdnUrl:
        isDefault:

```
**2、直接注入接口即可使用**
```java
@Resource
private FileStorageService fileStorageService;
```

