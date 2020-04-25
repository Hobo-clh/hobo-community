package com.ccsu.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截服务器异常 例如错误路径
 * @author 华华
 */
@Controller()
@RequestMapping("/error")
public class CustomizeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error/error";
    }

    /**
     * 通用拦截
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(HttpServletRequest request,
                                  Model model,
                                  Throwable e) {
        HttpStatus status = this.getStatus(request);
        if (status.is4xxClientError()){
            model.addAttribute("message","换个姿势试试看？？");
        }
        if(status.is5xxServerError()){
            model.addAttribute("message","服务器冒烟啦，等会再来噢~");

        }

        return new ModelAndView("error/error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
