package com.ccsu.community;

import com.ccsu.community.utils.CustomizeEmail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
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
    @Value("${customize.defaultAvatar}")
    String defaultAvatarUrl;

    @Test
    public void test(){
        System.out.println(defaultAvatarUrl);
    }

    @Autowired
    CustomizeEmail sendEmail;

    @Test
    public void test2(){
        sendEmail.sendEmail("3067332706@qq.com","你好啊","发送邮件测试");
    }
}
