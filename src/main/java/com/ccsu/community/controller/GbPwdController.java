package com.ccsu.community.controller;

import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.service.GbPwdService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 华华
 */
@Controller
public class GbPwdController {

    @Autowired
    GbPwdService gbPwdService;
    /**
     * 找回密码页面
     * @return
     */
    @RequestMapping("/gbPwd")
    public String goGbPwd(){
        return "gbPwd";
    }

    @ResponseBody
    @PostMapping("/verifyUser")
    public Object verifyUser(@RequestParam String email){
        boolean flag = gbPwdService.verifyUser(email);
        if (!flag) {
            return ResultDTO.info(301,"该邮箱还未注册！");
        }
        return ResultDTO.info(200,"邮箱用户存在");
    }
    /**
     * 发送验证码
     * @param email
     * @return
     */
    @ResponseBody
    @PostMapping("/gbPwd")
    public Object sendCode(@RequestParam String email){
        if (StringUtils.isBlank(email)){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_IS_BLANK);
        }
        return gbPwdService.sendCode(email);
    }

    /**
     * 判断验证码是否正确
     */
    @ResponseBody
    @PostMapping("/verifyCode")
    public ResultDTO verifyTheCode(@RequestParam String email,
                                   @RequestParam Integer code){
        if (StringUtils.isBlank(email)){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_IS_BLANK);
        }
        if (code==null||code==0) {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_IS_BLANK);
        }
        return gbPwdService.checkCodePwd(email,code);
    }

    /**
     * 修改密码
     * @param email
     * @param password
     * @return
     */
    @ResponseBody
    @PostMapping("/modifyPwd")
    public ResultDTO modifyPwd(@RequestParam String email,
                               @RequestParam String password){
        if (StringUtils.isBlank(password)){
            return ResultDTO.info(302,"密码不能为空");
        }
        return gbPwdService.modifyPwd(email,password);
    }
}
