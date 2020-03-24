# community
spring-boot-community

## mvn命令
- 使maven忽略证书验证
```shell 
mvn -Dmaven.wagon.http.ssl.insecure=true
```
- mybatis generator自动生成代码
```bash 
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
- flyway maven命令
```bash
mvn flyway:migrate
mvn flyway:repair
```