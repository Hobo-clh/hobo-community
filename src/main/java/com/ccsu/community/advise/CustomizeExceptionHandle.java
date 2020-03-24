package com.ccsu.community.advise;

import com.alibaba.fastjson.JSON;
import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//拦截服务器内部异常 处理mvc请求异常
@ControllerAdvice
public class CustomizeExceptionHandle {

    @ResponseBody
    @ExceptionHandler({Exception.class})
    Object handle(HttpServletRequest request,
                        HttpServletResponse response,
                        Throwable e, Model model) {

        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO = null;
            //返回json
            if (e instanceof CustomizeException) {
//                resultDTO = ResultDTO.errorOf((CustomizeException) e);
                return ResultDTO.errorOf((CustomizeException) e);
            } else {
//                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
//            PrintWriter writer = null;
//            try {
//                response.setContentType("application/json");
//                request.setCharacterEncoding("UTF-8");
//                writer = response.getWriter();
//                writer.write(JSON.toJSONString(resultDTO));
//            } catch (IOException ex) {
//            } finally {
//                if(writer!=null){
//                    writer.close();
//                }
//            }
        } else {
            //返回错误页面
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
                model.addAttribute("code", ((CustomizeException) e).getCode());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
                model.addAttribute("code",CustomizeErrorCode.SYS_ERROR.getCode());
            }
            return new ModelAndView("error/error");
        }
    }
}
