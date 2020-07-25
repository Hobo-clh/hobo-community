package com.ccsu.community;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author: LongHua
 * @date: 2020/7/9
 **/
public class FileTest {


    @Test
    public void test(){
        String outPath = "E:\\学习记录" + "\\test_file.txt";
        String inPath = "F:\\file" + "\\test_file.txt";

        FileTest.fileCopy(inPath,outPath );
    }

    public static void fileCopy(String inpath,String outPath){
        try {
            FileInputStream fis = new FileInputStream(inpath);
            FileOutputStream fos = new FileOutputStream(outPath);
            byte[] bt=new byte[1024];
            int len;
            //读取文件
            while ((len=fis.read(bt))!=-1){
                //写入内存
                fos.write(bt,0,len);
            }
            fos.flush();						//刷新
            fos.close();						//关闭流
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


