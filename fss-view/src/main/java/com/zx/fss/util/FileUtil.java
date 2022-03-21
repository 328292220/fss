package com.zx.fss.util;


import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    private static void copy(String f1, String f2) throws IOException {
        File file1=new File(f1);
        /*     File file2=new File(f2);*/

        File[] flist=file1.listFiles();
        for (File f : flist) {
            if(f.isFile()){
                copyFile(f.getPath(),f2+"/"+f.getName()); //调用复制文件的方法
                //System.out.println("原路径["+f.getPath()+"] 被复制路径["+f2+"/"+f.getName()+"]");
            }else if(f.isDirectory()){
                copyFileFolder(f.getPath(),f2+"/"+f.getName()); //调用复制文件夹的方法
                //System.out.println("原路径["+f.getPath()+"] 被复制路径["+f2+"/"+f.getName()+"]");
            }
        }
    }

    /**
     * 复制文件夹
     * @throws IOException
     */
    public static void copyFileFolder(String sourceFolderPath,String targetFolderPath) throws IOException {
        //创建文件夹
        File file=new File(targetFolderPath);
        if(!file.exists()){
            file.mkdirs();
        }
        copy(sourceFolderPath,targetFolderPath);
    }

    /**
     * 复制文件
     * @throws IOException
     */
    public static void copyFile(String sourceFilePath, String tagretFilePath) throws IOException {
        try {
            File file=new File(tagretFilePath);
            if(file.exists()){
                file.delete();
            }
            InputStream in = new FileInputStream(sourceFilePath);
            OutputStream out = new FileOutputStream(tagretFilePath);
            byte[] buffer = new byte[2048];
            int nBytes = 0;
            while ((nBytes = in.read(buffer)) > 0) {
                out.write(buffer, 0, nBytes);
            }
            out.flush();
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean delete(String fileName) {
        File file =new File(fileName);
        if (!file.exists()) {
            //System.out.println("删除文件失败:" + fileName +"不存在！");
            return false;
        }else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName：要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file =new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                //System.out.println("删除单个文件" + fileName +"成功！");
                return true;
            }else {
                //System.out.println("删除单个文件" + fileName +"失败！");
                return false;
            }
        }else {
            //System.out.println("删除单个文件失败：" + fileName +"不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir：要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile =new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir +"不存在！");
            return false;
        }
        boolean flag =true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i =0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            //System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            //System.out.println("删除目录" + dir +"成功！");
            return true;
        }else {
            return false;
        }
    }


    public static byte[] convertStreamToByte(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 将图片转换成base64格式
     *
     * @param imagefile
     * @return
     */
    public static String convertImageToBase64(File imagefile) throws IOException {
        String type = StringUtils.substring(imagefile.getName(), imagefile.getName().lastIndexOf(".") + 1);
        BufferedImage image = ImageIO.read(imagefile);
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            System.out.println(imageBytes.length);
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    /**
     * 从地址下载文件并返回Bsse64格式
     *
     * @param imageurl
     * @return
     */
    public static String convertImageFileToBase64FromUrl(String imageurl) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        String imageString = null;
        try {
            // 创建URL
            URL url = new URL(imageurl);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容放到内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            is.close();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(data.toByteArray());
            return imageString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
