package com.ccsu.community;

import com.ccsu.community.utils.CodecUtils;
import com.ccsu.community.utils.EmailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
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
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);

    }

    @Value("${customize.defaultAvatar}")
    String defaultAvatarUrl;

    @Test
    public void test(){
        System.out.println(defaultAvatarUrl);
    }

    @Autowired
    EmailUtils sendEmail;

    @Test
    public void test2(){
        sendEmail.sendEmail("3067332706@qq.com","你好啊","发送邮件测试");
    }

    @Test
    public void testUUID(){
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);

        String uuid2 = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println(uuid2);
    }
    @Test
    public void pwd(){
        String salt = CodecUtils.generateSalt();
        System.out.println(salt);
        String pwd = CodecUtils.md5Hex("1234567890lnn",salt);
        System.out.println(pwd);
    }
}
