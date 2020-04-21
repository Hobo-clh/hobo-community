package com.ccsu.community.service;

import com.ccsu.community.dto.MyUserDTO;
import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.mapper.VerifyMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import com.ccsu.community.model.Verify;
import com.ccsu.community.model.VerifyExample;
import com.ccsu.community.utils.CustomizeEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RegisterService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    CustomizeEmail send;
    @Autowired
    VerifyMapper verifyMapper;
    //默认头像
    @Value("${customize.defaultAvatar}")
    String defaultAvatarUrl;


    //检查邮箱是否注册
    public boolean checkRegister(String accountId){
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(accountId);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size()==0){
            return true;
        }
        return false;
    }

    public Object register(MyUserDTO userDTO) {
        VerifyExample verifyExample = new VerifyExample();
        verifyExample.createCriteria()
                .andAccountIdEqualTo(userDTO.getAccountId())
                .andVerifyCodeEqualTo(userDTO.getVerityCode());
        List<Verify> verifies = verifyMapper.selectByExample(verifyExample);
        if (verifies.size()==0){
            //验证码错误
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_IS_ERROR);
        }
        User user = new User();
        user.setLoginName("邮箱用户_"+userDTO.getAccountId());
        user.setPwd(userDTO.getPassword());
        user.setAccountId(userDTO.getAccountId());
        user.setToken(UUID.randomUUID().toString());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setNotificationCount(0);
        //设置默认头像
        user.setAvatarUrl(defaultAvatarUrl);
        int flag = userMapper.insert(user);
        //注册成功
        if (flag==1){
            //登录成功将验证码删除
            VerifyExample example = new VerifyExample();
            example.createCriteria()
                    .andAccountIdEqualTo(userDTO.getAccountId());
            verifyMapper.deleteByExample(example);
            return ResultDTO.info(200,"注册成功");
        }
        //注册失败
        return ResultDTO.errorOf(CustomizeErrorCode.REGISTER_FAIL);
    }

    /**
     * 发送邮件
     * @param email
     * @return
     */
    public ResultDTO sendCode(String email){
        //随机六位数验证码
        if (!checkRegister(email)){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_IS_EXIST);
        }
        try {
            int code = (int)((Math.random() * 9 + 1) * 100000);
            String title = "注册验证码";
            String content = "【Hobo社区】欢迎加入Hobo社区！ 您的验证码是："+ code + "，请在5分钟内完成注册。";
            send.sendEmail(email,title,content);
            Verify verify = new Verify();
            //将验证码和注册邮箱放入数据库中
            verify.setVerifyCode(code);
            verify.setAccountId(email);
            verifyMapper.insert(verify);
            return ResultDTO.info(200,"邮件发送成功");
        }catch (MailException e){
            log.error("邮件发送出错" + e);
            return ResultDTO.errorOf(CustomizeErrorCode.INVALID_ADDRESSES);
        }

    }

    //使用多线程五分钟后清除验证码数据
    @Async
    public void removeCode(String accountId){
        removeCode(accountId, verifyMapper);
    }

    static void removeCode(String accountId, VerifyMapper verifyMapper) {
        try {
            System.out.println("线程开始"+System.currentTimeMillis());
            Thread.sleep(1000*60*5);
            VerifyExample verifyExample = new VerifyExample();
            verifyExample.createCriteria()
                    .andAccountIdEqualTo(accountId);
            verifyMapper.deleteByExample(verifyExample);
            System.out.println("线程结束"+System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
