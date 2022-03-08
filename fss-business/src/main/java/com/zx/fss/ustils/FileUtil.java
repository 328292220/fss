package com.zx.fss.ustils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUtil {
    //获取文件名
    public static String shortName(String fileName){
        if(fileName.contains(".")){
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    //获取文件扩展名
    public static String extName(String fileName){
        if(fileName.contains(".")){
            String[] split = fileName.split("\\.");
            fileName = split[split.length-1];
        }else {
            return null;
        }
        return fileName;
    }

    //读取文件，返回流
    static public void readFileToResponse(HttpServletResponse response,File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        byte[] buffer = new byte[1024];
        int len = 0;
        // 只要没读完，不断的读取
        while ((len = bufferedInputStream.read(buffer)) != -1) {
            response.getWriter().write(len);
        }
        bufferedInputStream.close();
        fileInputStream.close();
    }
}
