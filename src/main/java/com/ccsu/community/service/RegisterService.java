package com.ccsu.community.service;

import com.ccsu.community.dto.EmailUserDTO;
import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import com.ccsu.community.utils.CodecUtils;
import com.ccsu.community.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RegisterService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailUtils send;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     *  默认头像
     */
    @Value("${customize.defaultAvatar}")
    String defaultAvatarUrl;

    private static final String CODE_PRE = "code";


    /**
     * 检查邮箱是否注册
     */

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

    public Object register(EmailUserDTO userDTO) {
        User user = new User();
        user.setLoginName("邮箱用户_"+userDTO.getAccountId().substring(0,4));
        user.setAccountId(userDTO.getAccountId());
        user.setToken(UUID.randomUUID().toString());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setNotificationCount(0);
        //设置默认头像
        user.setAvatarUrl(defaultAvatarUrl);
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //对密码加密
        user.setPwd(CodecUtils.md5Hex(userDTO.getPassword(), salt));
        int flag = userMapper.insert(user);
        //注册成功
        if (flag==1){
            //登录成功将验证码删除
            removeCode(userDTO.getAccountId());
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
            //将验证码和注册邮箱放入数据库中
            try {
                redisTemplate.opsForValue().set(CODE_PRE + email, code, 5, TimeUnit.MINUTES);
            }catch (Exception e) {
                log.error("redis存入失败");
                e.printStackTrace();
            }
            return ResultDTO.info(200,"邮件发送成功");
        }catch (MailException e){
            log.error("邮件发送出错" + e);
            return ResultDTO.errorOf(CustomizeErrorCode.INVALID_ADDRESSES);
        }
    }

    /**
     * 删除验证码
     */
    @Async
    public void removeCode(String email){
        redisTemplate.delete(CODE_PRE + email);
    }

    /**
     * 判断验证码是否正确
     *
     * @param email
     * @param code
     * @return
     */
    public boolean checkCode(String email, Integer code) {
        Integer redisCode = null;
        try {
            redisCode = (Integer) redisTemplate.opsForValue().get(CODE_PRE + email);
        } catch (Exception e) {
            log.error("从redis中获取验证码失败，异常信息：" + e);
        }
        return redisCode != null && redisCode.equals(code);
    }

}
