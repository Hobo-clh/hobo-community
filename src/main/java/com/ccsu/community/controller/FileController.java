package com.ccsu.community.controller;

import com.ccsu.community.dto.FileDTO;
import com.ccsu.community.provider.FileProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author 华华
 */
@Controller
public class FileController {

    @Autowired
    FileProvider fileProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        InputStream inputStream = file.getInputStream();

        FileDTO fileDTO = new FileDTO();
        String url = null;
        try {
            url = fileProvider.uploadFile(inputStream, file.getOriginalFilename());
            fileDTO.setSuccess(1);
            fileDTO.setUrl(url);
        }catch (Exception e){
            fileDTO.setSuccess(1);
            fileDTO.setUrl(url);
            fileDTO.setMessage("上传失败了");
            e.printStackTrace();
        }


        return fileDTO;
    }
}
