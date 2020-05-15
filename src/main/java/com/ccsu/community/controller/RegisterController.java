package com.ccsu.community.controller;

import com.ccsu.community.dto.EmailUserDTO;
import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 华华
 */
@Controller
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @ResponseBody
    @PostMapping("/checkRegister")
    public Object verifyUser(@RequestParam String email) {
        boolean flag = registerService.checkRegister(email);
        if (flag) {
            return ResultDTO.info(200, "该邮箱可使用！");
        }
        return ResultDTO.info(303, "邮箱用户存在");
    }

    @ResponseBody
    @PostMapping("/register")
    public Object register(EmailUserDTO userDTO) {
        if (StringUtils.isBlank(userDTO.getAccountId()) && StringUtils.isBlank(userDTO.getPassword())) {
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_OR_PWD_BLANK);
        }
        if (userDTO.getVerityCode() == 0 || userDTO.getVerityCode() == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_IS_BLANK);
        }
        if (!registerService.checkRegister(userDTO.getAccountId())){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_IS_EXIST);
        }
        //检查验证码是否正确
        boolean b = registerService.checkCode(userDTO.getAccountId(), userDTO.getVerityCode());
        if (b) {
            return registerService.register(userDTO);
        }
        return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_IS_ERROR);
        //表示不能注册 邮箱名重复
    }

    /**
     * 发送验证码
     *
     * @param email
     * @return
     */
    @ResponseBody
    @PostMapping("/sendCode")
    public Object sendCode(@RequestParam("email") String email) {
        System.out.println(email);
        if (StringUtils.isBlank(email)) {
            return ResultDTO.info(210, "邮箱不能为空");
        }
        return registerService.sendCode(email);
    }
}
