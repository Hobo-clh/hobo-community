package com.ccsu.community.controller;

import com.ccsu.community.dto.FileDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import com.ccsu.community.model.User;
import com.ccsu.community.provider.FileProvider;
import com.ccsu.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 华华
 */
@Slf4j
@Controller
public class FileController {

    @Autowired
    FileProvider fileProvider;
    @Autowired
    UserService userService;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        System.out.println(file.getOriginalFilename());
        InputStream inputStream = null;
        FileDTO fileDTO = null;
        String url = null;
        try {
            inputStream = file.getInputStream();
            fileDTO = new FileDTO();
            url = fileProvider.uploadFile(inputStream, file.getOriginalFilename());
            fileDTO.setSuccess(1);
            fileDTO.setUrl(url);
        } catch (Exception e) {
            fileDTO.setSuccess(1);
            fileDTO.setUrl(url);
            fileDTO.setMessage("上传失败了");
            e.printStackTrace();
        }finally {
            try{
                if (inputStream!=null) {
                    inputStream.close();
                }
            }catch(IOException e){
                log.error("字节流关闭失败",e);
            }
        }
        return fileDTO;
    }

    /**
     * 上传头像 将头像上传到oss对象存储后，得到其url，修改user的数据库
     *
     * @param request
     * @return
     */
    @PostMapping("/file/avatar")
    public String uploadAvatar(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null) {
            return "redirect:/login";
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("avatar-image");

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String avatarUrl = fileProvider.uploadFile(inputStream, file.getOriginalFilename());
            userService.updateAvatar(user,avatarUrl);
        }catch (IOException e) {
            log.error("头像上传失败",e);
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }finally {
            if (inputStream!=null) {
                try{
                    inputStream.close();
                }catch(IOException e){
                    log.error("文件字节流关闭失败",e);
                }
            }
        }
        return "redirect:/profile/people";
    }
}
