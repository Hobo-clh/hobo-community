package com.ccsu.community;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//
//import org.mybatis.generator.api.MyBatisGenerator;
//import org.mybatis.generator.config.Configuration;
//import org.mybatis.generator.config.xml.ConfigurationParser;
//import org.mybatis.generator.exception.InvalidConfigurationException;
//import org.mybatis.generator.exception.XMLParserException;
//import org.mybatis.generator.internal.DefaultShellCallback;

@SpringBootTest
class CommunityApplicationTests {

    @Test
    void contextLoads() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FvLHxafgZSyqXUwEHeF";
        String accessKeySecret = "7eLMMin3XKzG9AKceYwglz11PKZHAI";
        String bucketName = "hobo-community";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建OSSClient实例。
        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        String content = "Hello OSS";
        ossClient.putObject(bucketName, "hello world", new ByteArrayInputStream(content.getBytes()));

        // 创建存储空间。
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        ossClient.shutdown();

    }

//  public static void main(String[] args) {
//
//                List<String> warnings = new ArrayList<String>();
//                boolean overwrite = true;
//                String genCfg = "/generatorConfig.xml";
//                File configFile = new File(CommunityApplicationTests.class.getResource(genCfg).getFile());
//                ConfigurationParser cp = new ConfigurationParser(warnings);
//                Configuration config = null;
//                try {
//                    config = cp.parseConfiguration(configFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (XMLParserException e) {
//                    e.printStackTrace();
//                }
//                DefaultShellCallback callback = new DefaultShellCallback(overwrite);
//                MyBatisGenerator myBatisGenerator = null;
//                try {
//                    myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//                } catch (InvalidConfigurationException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    myBatisGenerator.generate(null);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }


}
