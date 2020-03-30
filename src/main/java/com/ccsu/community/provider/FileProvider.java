package com.ccsu.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class FileProvider {
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    @Value("${aliyun.endpoint}")
    String endpoint;
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    @Value("${aliyun.accessKeyId}")
    String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    String accessKeySecret;
    @Value("${aliyun.bucketName}")
    String bucketName;
   // @Value("${aliyun.existTime}")
    Long existTime = 315360000000L;

    public String uploadFile(InputStream inputStream,String fileName) {

        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
//        String[] split = file.getName().split("\\.");
        String[] split = fileName.split("\\.");
        if (split.length>1){
            fileName = UUID.randomUUID().toString()+"."+split[1];
        }
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);
        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);
        //设置url的过期时间
        Date expiration = new Date(System.currentTimeMillis() + existTime);
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
        if (url!=null){
            // 关闭OSSClient。
            ossClient.shutdown();

            return url.toString();
        }else {
            // 关闭OSSClient。
            ossClient.shutdown();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }


    }



}
