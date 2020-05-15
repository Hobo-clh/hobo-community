package com.ccsu.community.service;

import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import com.ccsu.community.utils.CodecUtils;
import com.ccsu.community.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 找回密码业务层
 * @author 华华
 */
@Slf4j
@Service
public class GbPwdService {

    @Autowired
    EmailUtils send;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

    private static final String CODE_PRE = "code-gbPwd";

    public ResultDTO sendCode(String email){
        int code = (int)((Math.random() * 9 + 1) * 100000);
        String title = "Hobo社区重置密码";
        String content = "【Hobo社区】你的验证码是"+code+"，此验证码用于重置密码，5分钟内有效，请勿泄露于他人";
        try {
            send.sendEmail(email,title,content);
            //将验证码和注册邮箱放入数据库中
//            Verify verify = new Verify();
//            //将验证码和注册邮箱放入数据库中
//            verify.setVerifyCode(code);
//            verify.setAccountId(email);
//            verifyMapper.insert(verify);
            redisTemplate.opsForValue().set(CODE_PRE + email, code, 5, TimeUnit.MINUTES);
            return ResultDTO.info(200,"邮件发送成功");
        }catch (MailException e){
            log.error("邮件发送出错" + e);
            return ResultDTO.errorOf(CustomizeErrorCode.INVALID_ADDRESSES);
        }
    }

    /**
     * 多线程五分钟删除验证码
     * @param accountId
     */
//    @Async
//    public void removeCode(String accountId){
//        RegisterService.removeCode(accountId, verifyMapper);
//    }

    /**
     * 判断验证码是否正确
     * @param email
     * @param code
     * @return
     */
    public ResultDTO checkCodePwd(String email, Integer code) {
        Integer redisCode = null;
        try {
            redisCode = (Integer) redisTemplate.opsForValue().get(CODE_PRE + email);
        } catch (Exception e) {
            log.error("从redis中获取验证码失败，异常信息：" + e);
        }
        if (redisCode != null && redisCode.equals(code)) {
            return ResultDTO.info(200,"验证成功");
        }
        return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_IS_ERROR);
    }

//    public ResultDTO verifyTheCode(String email, Integer code) {
//        VerifyExample verifyExample = new VerifyExample();
//        verifyExample.createCriteria()
//                .andVerifyCodeEqualTo(code)
//                .andAccountIdEqualTo(email);
//        List<Verify> verifies = verifyMapper.selectByExample(verifyExample);
//        if (verifies.size()==0) {
//            return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_IS_ERROR);
//        }
//        verifyMapper.deleteByExample(verifyExample);
//        return ResultDTO.info(200,"验证成功");
//    }

    /**
     * 验证用户是否注册了
     * @param email
     * @return
     */
    public boolean verifyUser(String email) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size()==0) {
            return false;
        }
        return true;
    }

    public ResultDTO modifyPwd(String email, String password) {
        User user = new User();
        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountIdEqualTo(email);
        //密码加密
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPwd(CodecUtils.md5Hex(password,salt));
        int update = userMapper.updateByExampleSelective(user, example);
        if (update==1){
            return ResultDTO.info(200,"密码修改成功");
        }
        return ResultDTO.info(302,"密码修改失败");
    }
}
