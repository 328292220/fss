package com.zx.fss.handle;

import com.zx.fss.model.FileConvertInfo;
import com.zx.fss.util.FileAnalysisUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@FileConvert(".pdf")
@Service
public class PDFFileConvert  implements IFileConvert {
    @Autowired
    private FileAnalysisUtil fileAnalysisUtil;

    @Override
    public FileConvertInfo convertFile(FileConvertInfo fileConvertInfo) {
        try {
            fileConvertInfo.setConvertType("pdf");
            PDDocument pdf = PDDocument.load(new File((fileConvertInfo.getFilePath())));
            fileConvertInfo.setPageNum(pdf.getNumberOfPages());
            File file = new File(fileConvertInfo.getFileDirPath() + "split_1.jpeg");
            if(file.exists()){
                fileConvertInfo.setHaveConvert(true);
                return fileConvertInfo;
            }
            fileAnalysisUtil.convertFileToJPG(fileConvertInfo);
        } catch (Exception e) {
            fileConvertInfo.setErrorInfo("转换pdf文档发生错误");
            e.printStackTrace();
        }
        return fileConvertInfo;
    }
}
