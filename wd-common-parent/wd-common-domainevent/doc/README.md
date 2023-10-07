# wd-common-domainevent
## 1.领域事件消费失败重试方案

### 1.1 技术方案
现在基于DDD领域建模改造后的代码，由于视图的写操作大部分都是基于领域事件去完成的，但是视图层的代码有时因为开发的过程会有一些bug，造成事件消费失败后，视图无法构建，wd-common-domainevent现在提供事件重试的机制，来确保本地消费事件失败后，依然能够再次重试。

<img src="https://i.postimg.cc/pRKcL5vb/image.png" style="zoom:200%">