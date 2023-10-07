##wd-common-spring
spring相关增强，提供自动装配，当前新增了多语言支持
## 使用方式
1、application.properties新增如下配置：

spring.messages.basename=资源路径存放地址，如：classpath:i18n/message;classpath:i18n/error，多个资源地址之间用分号隔开

2、在需要使用的地方注入MessageSourceUtil就行了，MessageSourceUtil里面提供了多个获取资源内信息的方法


