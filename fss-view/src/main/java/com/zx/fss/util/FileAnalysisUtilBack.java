package com.zx.fss.util;

import com.aspose.cells.HtmlSaveOptions;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.slides.ISlide;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.zx.fss.handle.FileConvert;
import com.zx.fss.handle.IFileConvert;
import com.zx.fss.model.FileConvertInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class FileAnalysisUtilBack {
    private Map<String, IFileConvert> handleConvertRulesMap = new HashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${preview.imageExt}")
    private String imageExt;
    @Value("${preview.vedioExt}")
    private String vedioExt;
    @Value("${preview.txtExt}")
    private String txtExt;
    private String tempfilesPath;
    private String analysis = "analysis/";

    private static Map<String, String> types;
    private static List<String> extensions = new ArrayList<String>();
    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    public FileAnalysisUtilBack(){
        tempfilesPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                +"static/tempfiles/";
    }

    public void initFileConvertRules() {
        try {
            Map<String, IFileConvert> res = applicationContext.getBeansOfType(IFileConvert.class);
            for (Map.Entry en : res.entrySet()) {
                IFileConvert service = (IFileConvert) en.getValue();
                FileConvert fileConvert = (FileConvert) en.getValue().getClass().getAnnotation(FileConvert.class);
                String[] convertTypelist = fileConvert.value().split("\\|");
                for (String fileExt : convertTypelist) {
                    handleConvertRulesMap.put(fileExt, service);
                }
            }
            System.out.println("系统初始化文件转换策略成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getAllFileType();
        initContentType();
        initExtentsion();
    }

    public String readImageAndVedioFile(String fileUrl) throws Exception {
        URL url = new URL(HttpUtil.tranformStyle(fileUrl));
        URLConnection conn = url.openConnection();
        String fileGuid = "";
        String realUrl = conn.getURL().toString();
        String fileExt = getFileNameExtFromUrl(realUrl);
        fileGuid = EncryptUtil.encryptByMD5(fileUrl);
        String analysisPath = tempfilesPath;
        File saveDir = new File(analysisPath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        analysisPath = tempfilesPath + analysis;
        saveDir = new File(analysisPath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        analysisPath = analysisPath + fileGuid + "/";
        saveDir = new File(analysisPath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileGuid + fileExt);
        if (!file.exists()) {
            file.createNewFile();
            InputStream inputStream = conn.getInputStream();
            byte[] getData = FileUtil.convertStreamToByte(inputStream);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return fileGuid;
    }

    public String readFile(String fileUrl) throws Exception {
        URL url = new URL(HttpUtil.tranformStyle(fileUrl));
        URLConnection conn = url.openConnection();
        String fileGuid = "";
        String realUrl = conn.getURL().toString();
        String fileExt = getFileNameExtFromUrl(realUrl);
        if (imageExt.contains(fileExt) || vedioExt.contains(fileExt)) {
            fileGuid = "onlineresource|" + fileUrl;
            return fileGuid;
        }
        if (handleConvertRulesMap.containsKey(fileExt) || txtExt.contains(fileExt)) {
            fileGuid = EncryptUtil.encryptByMD5(fileUrl);
            String analysisPath = tempfilesPath;
            File saveDir = new File(analysisPath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            analysisPath = tempfilesPath+ analysis;
            saveDir = new File(analysisPath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            analysisPath = analysisPath + fileGuid + "/";
            saveDir = new File(analysisPath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileGuid + fileExt);
            if (!file.exists()) {
                file.createNewFile();
                InputStream inputStream = conn.getInputStream();
                byte[] getData = FileUtil.convertStreamToByte(inputStream);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(getData);
                if (fos != null) {
                    fos.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return fileGuid;
    }

    public String getFileNameExtFromUrl(String fileUrl) throws IOException {
        //fileUrl = "file:///D:/TEMP3/fss_files/admin/20220308161925502_DB%E4%BF%AE%E6%94%B9SQL.txt";
        URL obj = new URL(fileUrl);
        URLConnection conn = obj.openConnection();
        String contentType = conn.getContentType();
        String disposition = conn.getHeaderField("Content-disposition");
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        byteOutputStream.write(bytes, 0, 50);
        byte[] byteArray = byteOutputStream.toByteArray();
        inputStream.close();
        byteOutputStream.close();
        String fileType = getFileType(fileUrl, disposition, contentType, byteArray);
        return fileType;
    }

    ///region 读取文件后缀

    public static String getFileType(String url, String disposition, String contentType, byte[] byteArray) {
        String ext = null;
        ext = getTypeByExtenssion(url);
        {
            if (ext != null)
                return ext;
        }
        ext = getTypeByDisposition(disposition);
        {
            if (ext != null)
                return ext;
        }
        ext = getTypeByContentType(contentType);
        {
            if (ext != null)
                return ext;
        }
        ext = getFileTypeByStream(byteArray);
        {
            if (ext != null)
                return ext;
        }

        return ".html";
    }

    private static String getFileTypeByStream(byte[] b) {
        if (ArrayUtils.isNotEmpty(b)) {
            b = ArrayUtils.subarray(b, 0, 50);
            String filetypeHex = String.valueOf(getFileHexString(b));
            Iterator<Map.Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
            while (entryiterator.hasNext()) {
                Map.Entry<String, String> entry = entryiterator.next();
                String fileTypeHexValue = entry.getValue();
                if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getTypeByExtenssion(String linkUrl) {
        if (linkUrl == null)
            return null;
        linkUrl = linkUrl.toLowerCase();
        for (String ext : extensions) {
            if (linkUrl.endsWith(ext)) {
                return ext;
            }
        }
        return null;
    }

    private static String getTypeByDisposition(String disposition) {
        String ext = null;
        if (!StringUtils.isEmpty(disposition)) {
            disposition = StringUtils.replace(disposition, "\"", "");
            String[] strs = disposition.split(";");
            for (String string : strs) {
                if (string.toLowerCase().indexOf("filename=") >= 0) {
                    ext = StringUtils.substring(string, string.lastIndexOf("."));
                    break;
                }
            }
        }
        return ext;
    }

    private static String getTypeByContentType(String contentType) {
        if (types.containsKey(contentType))
            return types.get(contentType);
        return null;
    }

    // 通过文件头判断会出现重复的情况
    private static void getAllFileType() {
        FILE_TYPE_MAP.put(".pdf", "255044462D312E"); // Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put(".doc", "D0CF11E0"); // MS Word
        FILE_TYPE_MAP.put(".xls", "D0CF11E0"); // MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE_MAP.put(".jpg", "FFD8FF"); // JPEG (jpg)
        FILE_TYPE_MAP.put(".png", "89504E47"); // PNG (png)
        FILE_TYPE_MAP.put(".gif", "47494638"); // GIF (gif)
        FILE_TYPE_MAP.put(".tif", "49492A00"); // TIFF (tif)
        FILE_TYPE_MAP.put(".bmp", "424D"); // Windows Bitmap (bmp)
        FILE_TYPE_MAP.put(".dwg", "41433130"); // CAD (dwg)
        FILE_TYPE_MAP.put(".html", "68746D6C3E"); // HTML (html)
        FILE_TYPE_MAP.put(".rtf", "7B5C727466"); // Rich Text Format (rtf)
        FILE_TYPE_MAP.put(".xml", "3C3F786D6C");
        FILE_TYPE_MAP.put(".zip", "504B0304"); // docx的文件头与zip的一样
        FILE_TYPE_MAP.put(".rar", "52617221");
        FILE_TYPE_MAP.put(".psd", "38425053"); // Photoshop (psd)
        FILE_TYPE_MAP.put(".eml", "44656C69766572792D646174653A"); // Email
        FILE_TYPE_MAP.put(".dbx", "CFAD12FEC5FD746F"); // Outlook Express (dbx)
        FILE_TYPE_MAP.put(".pst", "2142444E"); // Outlook (pst)
        FILE_TYPE_MAP.put(".mdb", "5374616E64617264204A"); // MS Access (mdb)
        FILE_TYPE_MAP.put(".wpd", "FF575043"); // WordPerfect (wpd)
        FILE_TYPE_MAP.put(".eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put(".ps", "252150532D41646F6265");
        FILE_TYPE_MAP.put(".qdf", "AC9EBD8F"); // Quicken (qdf)
        FILE_TYPE_MAP.put(".pwl", "E3828596"); // Windows Password (pwl)
        FILE_TYPE_MAP.put(".wav", "57415645"); // Wave (wav)
        FILE_TYPE_MAP.put(".avi", "41564920");
        FILE_TYPE_MAP.put(".ram", "2E7261FD"); // Real Audio (ram)
        FILE_TYPE_MAP.put(".rm", "2E524D46"); // Real Media (rm)
        FILE_TYPE_MAP.put(".mpg", "000001BA"); //
        FILE_TYPE_MAP.put(".mov", "6D6F6F76"); // Quicktime (mov)
        FILE_TYPE_MAP.put(".asf", "3026B2758E66CF11"); // Windows Media (asf)
        FILE_TYPE_MAP.put(".mid", "4D546864"); // MIDI (mid)
    }

    // 对应的http contenttype
    private static void initContentType() {
        types = new HashMap<String, String>();
        types.put("application/pdf", ".pdf");
        types.put("application/msword", ".doc");
        types.put("text/plain", ".txt");
        types.put("application/javascript", ".js");
        types.put("application/x-xls", ".xls");
        types.put("application/-excel", ".xls");
        types.put("text/html", ".html");
        types.put("application/x-rtf", ".rtf");
        types.put("application/x-ppt", ".ppt");
        types.put("image/jpeg", ".jpg");
        types.put("application/vnd.openxmlformats-officedocument.wordprocessingml.template", ".docx");
        types.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
        types.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
        types.put("message/rfc822", ".eml");
        types.put("application/xml", ".xml");
    }

    //自定义需要匹配链接的文件后缀，不满足的可以自行添加
    private static void initExtentsion() {
        extensions.add(".pdf");
        extensions.add(".doc");
        extensions.add(".txt");
        extensions.add(".xls");
        extensions.add(".html");
        extensions.add(".rtf");
        extensions.add(".mht");
        extensions.add(".rar");
        extensions.add(".ppt");
        extensions.add(".jpg");
        extensions.add(".docx");
        extensions.add(".xlsx");
        extensions.add(".pptx");
        extensions.add(".eml");
        extensions.add(".zip");
        extensions.add(".docm");
        extensions.add(".xlsm");
        extensions.add(".xlsb");
        extensions.add(".dox");
        extensions.add(".csv");

        extensions.add(".pdf");
        extensions.add(".doc");
        extensions.add(".xls");
        extensions.add(".jpg");
        extensions.add(".png");
        extensions.add(".gif");
        extensions.add(".tif");
        extensions.add(".bmp");
        extensions.add(".dwg");
        extensions.add(".html");
        extensions.add(".rtf");
        extensions.add(".xml");
        extensions.add(".zip");
        extensions.add(".rar");
        extensions.add(".psd");
        extensions.add(".eml");
        extensions.add(".dbx");
        extensions.add(".pst");
        extensions.add(".mdb");
        extensions.add(".wpd");
        extensions.add(".eps");
        extensions.add(".ps");
        extensions.add(".qdf");
        extensions.add(".pwl");
        extensions.add(".wav");
        extensions.add(".avi");
        extensions.add(".ram");
        extensions.add(".rm");
        extensions.add(".mpg");
        extensions.add(".mov");
        extensions.add(".asf");
        extensions.add(".mid");
        extensions.add(".mp4");
    }
    ///endregion

    public String getFileNameExtFromPath(String filePath) throws IOException {
        String[] spliteArray = filePath.split("\\.");
        String fileExt = "." + spliteArray[spliteArray.length - 1];
        return fileExt.toLowerCase();
    }

    public FileConvertInfo convertFile(String fileGuid) throws Exception {
        if (fileGuid.startsWith("onlineresource")) {
            String fileUrl = fileGuid.replace("onlineresource|", "");
            URL url = new URL(HttpUtil.tranformStyle(fileUrl));
            URLConnection conn = url.openConnection();
            String realUrl = conn.getURL().toString();
            String fileExt = getFileNameExtFromUrl(realUrl);
            FileConvertInfo fileConvertInfo = new FileConvertInfo();
            if (imageExt.contains(fileExt)) {
                fileConvertInfo.setConvertType("image");
            } else if (vedioExt.contains(vedioExt)) {
                fileConvertInfo.setConvertType("vedio");
            }
            return fileConvertInfo;
        } else {
            String tempFileDicPath = tempfilesPath + analysis + fileGuid + "/";
            File fileDic = new File(tempFileDicPath);
            File[] flist = fileDic.listFiles();
            String filePath = "";
            String fileExt = "";
            String fileName = "";
            for (File f : flist) {
                if (f.isFile() && f.getName().startsWith(fileGuid)) {
                    filePath = f.getAbsolutePath();
                    fileExt = getFileNameExtFromPath(filePath);
                    fileName = f.getName();
                    break;
                }
            }
            FileConvertInfo fileConvertInfo = new FileConvertInfo();
            fileConvertInfo.setFileGuid(fileGuid);
            fileConvertInfo.setFileName(fileName);
            fileConvertInfo.setFileDirPath(tempFileDicPath);
            fileConvertInfo.setFilePath(filePath);
            fileConvertInfo.setFileExt(fileExt);
            if (txtExt.contains(fileExt)) {
                fileConvertInfo.setConvertType("txt");
                return fileConvertInfo;
            } else {
                return handleConvertRulesMap.get(fileExt).convertFile(fileConvertInfo);
            }
        }
    }

    public String getFileContent(String fileGuid) {
        String fileContent = "";
        try {
            String tempFileDicPath = tempfilesPath + analysis + fileGuid + "/";
            File fileDic = new File(tempFileDicPath);
            File[] flist = fileDic.listFiles();
            if (flist.length > 0) {
                File file = flist[0];
                String encoding = "UTF-8";

                Long filelength = file.length();
                byte[] filecontent = new byte[filelength.intValue()];
                FileInputStream in = new FileInputStream(file);
                in.read(filecontent);
                fileContent =  new String(filecontent, encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    @Async
    public void convertFileToJPG(FileConvertInfo fileConvertInfo) throws Exception {
        try {
            switch (fileConvertInfo.getConvertType()) {
                case "doc":
                    Document doc = new Document(fileConvertInfo.getFilePath());
                    for (int i = 0; i < doc.getPageCount(); i++) {
                        Document extractedPage = doc.extractPages(i, 1);
                        extractedPage.save(fileConvertInfo.getFileDirPath() + "split_" + (i + 1) + ".jpeg", SaveFormat.JPEG);
                    }
                    break;
                case "ppt":
                    Presentation ppt = new Presentation(fileConvertInfo.getFilePath());
                    for (int i = 0; i < ppt.getSlides().size(); i++) {
                        ISlide slide = ppt.getSlides().get_Item(i);
                        int height = (int) (ppt.getSlideSize().getSize().getHeight() - 150);
                        int width = (int) (ppt.getSlideSize().getSize().getWidth() - 150);
                        BufferedImage image = slide.getThumbnail(new java.awt.Dimension(width, height));
                        //每一页输出一张图片
                        File outImage = new File(fileConvertInfo.getFileDirPath() + "split_" + (i + 1) + ".jpeg");
                        ImageIO.write(image, "jpeg", outImage);
                    }
                    break;
                case "excel":
                    ///region 转化图片(废弃)
                    //Workbook wb = new Workbook(fileConvertInfo.getFilePath());
                    //ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
                    //imgOptions.setImageFormat(ImageFormat.getJpeg());
                    //for (int i = 0; i < wb.getWorksheets().getCount(); i++) {
                    // Worksheet sheet = wb.getWorksheets().get(i);
                    //SheetRender sr = new SheetRender(sheet, imgOptions);
                    //sr.toImage(i, fileConvertInfo.getFileDirPath() + "split_" + (i + 1) + ".jpeg");
                    //}
                    //endregion
                    Workbook wb = new Workbook(fileConvertInfo.getFilePath());
                    HtmlSaveOptions opts = new HtmlSaveOptions();
                    opts.setExportWorksheetCSSSeparately(true);
                    opts.setExportSimilarBorderStyle(true);
                    Worksheet ws = wb.getWorksheets().get(0);
                    wb.save(fileConvertInfo.getFileDirPath() + "convert.html", opts);
                    break;
                case "pdf":
                    PDDocument pdf = PDDocument.load(new File((fileConvertInfo.getFilePath())));
                    int pageCount = pdf.getNumberOfPages();
                    PDFRenderer renderer = new PDFRenderer(pdf);
                    for (int i = 0; i < pageCount; i++) {
                        BufferedImage image = renderer.renderImage(i, 1.25f); // 第二个参数越大生成图片分辨率越高，转换时间也就越长
                        ImageIO.write(image, "JPEG", new File(fileConvertInfo.getFileDirPath() + "split_" + (i + 1) + ".jpeg"));
                    }
                    pdf.close();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
