package com.zx.fss.controller;

import com.zx.fss.constant.SpecialSymbolsUtil;
import com.zx.fss.util.ConvertOp;
import com.zx.fss.util.FileAnalysisUtil;
import com.zx.fss.util.FileUtil;
import com.zx.fss.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

@Controller
public class ViewController {
    @Autowired
    private FileAnalysisUtil fileAnalysisUtil;

    @RequestMapping("/view/{fileUrl}")
    public void image(@PathVariable(value = "fileUrl") String fileUrl, HttpServletResponse response) throws IOException {
//        String file = ConvertOp.convert2String(fileUrl);
//        byte[] decode = Base64.getDecoder().decode(file);
//        file = new String(decode);
//        file = file.replace("file:"
//                + SpecialSymbolsUtil.separator
//                + SpecialSymbolsUtil.separator
//                + SpecialSymbolsUtil.separator,"");
//        //想要返回图片的路径
//        FileInputStream fis = new FileInputStream(file) ;
//        //得到文件大小
//        int size = fis.available();
//        byte data[] = new byte[size] ;
//        fis.read(data) ;
//        fis.close();
//        //设置返回的文件类型
//        response.setContentType("image/jpeg");
//        OutputStream os = response.getOutputStream() ;
//        os.write(data);
//        os.flush();
//        os.close();

        //设置返回的文件类型
        response.setContentType("image/jpeg");
        String file = ConvertOp.convert2String(fileUrl);
        byte[] decode = Base64.getDecoder().decode(file);
        file = new String(decode);
        URL url = new URL(HttpUtil.tranformStyle(file));
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        byte[] getData = FileUtil.convertStreamToByte(inputStream);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(getData);
        if (outputStream != null) {
            outputStream.close();
        }

        if (inputStream != null) {
            inputStream.close();
        }
    }

    @RequestMapping("/image2/{fileUrl}")
    public void image2(@PathVariable(value = "fileUrl") String fileUrl, HttpServletResponse response) throws Exception {
        String file = ConvertOp.convert2String(fileUrl);
        byte[] decode = Base64.getDecoder().decode(file);
        file = new String(decode);
        String fileGuid = fileAnalysisUtil.readImageAndVedioFile(file);
    }


}
