## Hobo社区
一个基于Spring Boot和MyBatis实现的的小型社区项目
[线上地址](http://www.hobosocool.top:81/),欢迎大伙们来交流、提建议、找bug，有问题可以直接留言！
### 使用技术栈

- 后端：Spring Boot、Mybatis等
- 前端：Thymeleaf、BootStrap、MySQL等

### 功能简述

- 表单注册登录,支持密码修改

- 第三方登录：支持GitHub、QQ

- 个人信息页面：可以上传头像，修改名称、签名

- 提问：支持markdown语法,支持上传图片

- 点赞、评论、二级评论

- 通知功能：点赞、评论或者二级评论后，会产生通知

- 热门标签：使用Spring Schedule定时任务，定时计算出标签的热度

- 7天、30天热门文章：根据评论数判定

- 相关问题：标签相关的文章、问题

### 参考文档
- [阿里云OSS使用教程](https://help.aliyun.com/document_detail/31883.html?spm=5176.8466010.bucket.4.7c451450a0B80C)
- [免费开源的markdown编辑器](http://editor.md.ipandao.com/)
- [Spring Boot 日志配置](https://blog.csdn.net/Inke88/article/details/75007649) 
- [QQ互联文档](https://wiki.connect.qq.com/%E5%87%86%E5%A4%87%E5%B7%A5%E4%BD%9C_oauth2-0)
- [Alibaba Cloud Toolkit Idea一键部署插件](https://help.aliyun.com/product/29966.html)

### 遇到的问题
[无法接收PUT、DELETE请求](https://blog.csdn.net/Soul_Ming/article/details/104761142)

### mvn命令
- 使maven忽略证书验证下载jar包
<<<<<<< HEAD
```shell 
mvn -Dmaven.wagon.http.ssl.insecure=true install
```
- mybatis generator自动生成代码
```bash 
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
`mvn -Dmaven.wagon.http.ssl.insecure=true install`

- mybatis generator自动生成mapper、model
`mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate`

- flyway maven命令
```bash
mvn flyway:migrate
mvn flyway:repair
```

### 更新日志
#### 2020.5.14
- 增加个人信息页面
- 支持上传头像、更改用户名和签名
- 邮箱验证码信息更改为Redis存储

#### 2020.4.25
- 增加了qq登录，置顶功能

#### 2020.4.22
- 增加邮箱注册功能
- 支持邮箱用户修改密码
- 美化界面

#### 2020.4.14
- 增加了点赞功能
- 美化界面

#### 2020.4.11
- 添加了按热度分类等

#### 2020.4.1
- 使用springboot定时器，增加热门标签功能

#### 2020.3.31
- 新增简陋的表单登录，增加标签库

#### 2020.3.29
- 增加富文本编译器，并实现阿里云oss，javaSDK存储上传的图片

