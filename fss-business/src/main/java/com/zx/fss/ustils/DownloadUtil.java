package com.zx.fss.ustils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Slf4j
public class DownloadUtil {
//    public static void download(File file) {
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        response.reset();
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
//        try {
//            FileUtil.readFileToResponse(response, file);
//        } catch (IOException e) {
//            log.error("下载文件保存", e);
//            throw new BusinessException(400, e.getMessage(), null);
//        }
//
//
//    }

    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    public static void downloadLocal(HttpServletResponse response) throws FileNotFoundException {
        // 下载本地文件
        String fileName = "Operator.doc".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream("c:/Operator.doc");// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadNet(HttpServletResponse response) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL("windine.blogdriver.com/logo.gif");

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("c:/abc.gif");

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //支持在线打开文件的一种方式
    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            //下载Excel时:
            //getResponse().setContentType("application/msexcel");
        }
    }

    /**支持中文名称:
     * 文件下载
     */
//    public static void download(File file) {
//        try {
//            String fileName = file.getName();
//            //支持中文
//            fileName = URLEncoder.encode(fileName, "UTF-8");
//            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            response.reset();
//            //response.setContentType(request.getServletContext().getMimeType(fileName));
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//            //response.setHeader("Content-Disposition", "inline;filename=" + fileName);
//            InputStream in = new FileInputStream(file);
//            OutputStream out = response.getOutputStream();
//
//            byte[] b = new byte[1024];
//            int length = 0;
//            while ((length = in.read(b)) != -1) {
//                out.write(b, 0, length);
//            }
//            in.close();
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @param path 指想要下载的文件的路径
     * @param response
     * @功能描述 下载文件:将输入流中的数据循环写入到响应输出流中，而不是一次性读取到内存
     */
//    public static void download(String path)  {
//        try{
//            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//            // 读到流中
//            InputStream inputStream = new FileInputStream(path);// 文件的存放路径
//            response.reset();
//            response.setContentType("application/octet-stream");
//            String filename = new File(path).getName();
//            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
//            ServletOutputStream outputStream = response.getOutputStream();
//            byte[] b = new byte[1024];
//            int len;
//            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
//            while ((len = inputStream.read(b)) > 0) {
//                outputStream.write(b, 0, len);
//            }
//            inputStream.close();
//            outputStream.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    public static void download(String path)  {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        ServletOutputStream out =null;
        ByteArrayOutputStream baos = null;
        try {
            // 读到流中
            InputStream inStream = new FileInputStream(path);// 文件的存放路径
            byte[] buffer = new byte[1024];
            int len;
            baos = new ByteArrayOutputStream();
            while ((len=inStream.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            String filename = new File(path).getName();
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + baos.size());
            response.setHeader("filename", filename);
            response.setContentType("application/octet-stream");
            out = response.getOutputStream();
            out.write(baos.toByteArray());

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
            try {
                baos.flush();
                baos.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

    }



}
