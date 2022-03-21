package com.zx.fss.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtil {
    private static final String BOUNDARY = "20140501";

    public static String httpJsonPost(String uri, String param) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri);
        post.setHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(param, StandardCharsets.UTF_8);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    public static String httpFormPost(String httpUrl, Map param) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            //connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的(form表单形式的参数实质也是key,value值的拼接，类似于get请求参数的拼接)
            os.write(createLinkString(param).getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }

        return prestr.toString();
    }

    public static void downLoadFromUrl(String fileUrl, String savePath) throws IOException {
        URL url = new URL(tranformStyle(fileUrl));
        URLConnection conn = url.openConnection();
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = FileUtil.convertStreamToByte(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + getFileNameFromFileUrl(fileUrl));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public static void uploadFile(String uploadUrl, String filePath, HashMap<String, Object> paramMap) throws IOException {
        File file = new File(filePath);
        URL url = new URL(uploadUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setConnectTimeout(3000); // 设置发起连接的等待时间，3s
        httpConn.setReadTimeout(30000); // 设置数据读取超时的时间，30s
        httpConn.setUseCaches(false); // 设置不使用缓存
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        OutputStream os = httpConn.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);

        String content = "--" + BOUNDARY + "\r\n";
        for (String key : paramMap.keySet()) {
            content += "Content-Disposition: form-data; name=" + key + "" + "\r\n\r\n";
            content += ConvertOp.convert2String(paramMap.get(key));
            content += "\r\n--" + BOUNDARY + "\r\n";
        }

        content += "Content-Disposition: form-data; name=\"file\"; filename=" + file.getName() + "\r\n";
        content += "Content-Type: application/octet-stream\r\n\r\n";
        bos.write(content.getBytes());

        // 开始写出文件的二进制数据
        FileInputStream fin = new FileInputStream(file);
        BufferedInputStream bfi = new BufferedInputStream(fin);
        byte[] buffer = new byte[4096];
        int bytes = bfi.read(buffer, 0, buffer.length);
        while (bytes != -1) {
            bos.write(buffer, 0, bytes);
            bytes = bfi.read(buffer, 0, buffer.length);
        }
        bfi.close();
        fin.close();
        bos.write(("\r\n--" + BOUNDARY).getBytes());
        bos.flush();
        bos.close();
        os.close();

        // 读取返回数据
        StringBuffer strBuf = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpConn.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            strBuf.append(line).append("\n");
        }
        String res = strBuf.toString();
        System.out.println(res);
        reader.close();
        httpConn.disconnect();

    }

    public static String getFileNameFromFileUrl(String fileUrl) throws IOException {
        String[] spliteArray = fileUrl.split("/");
        String fileName = spliteArray[spliteArray.length - 1];
        return fileName;
    }

    /**
     * 对中文字符进行UTF-8编码
     *
     * @param source 要转义的字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String tranformStyle(String source) throws UnsupportedEncodingException {
        char[] arr = source.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            char temp = arr[i];
            if (isChinese(temp)) {
                sb.append(URLEncoder.encode("" + temp, "UTF-8"));
                continue;
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 获取字符的编码值
     *
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static int getValue(char s) throws UnsupportedEncodingException {
        String temp = (URLEncoder.encode("" + s, "GBK")).replace("%", "");
        if (temp.equals(s + "")) {
            return 0;
        }
        char[] arr = temp.toCharArray();
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            try {
                int t = Integer.parseInt((arr[i] + ""), 16);
                total = total * 16 + t;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return total;
    }

    /**
     * 判断是不是中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }

}
