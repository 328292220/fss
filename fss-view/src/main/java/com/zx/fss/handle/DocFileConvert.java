package com.zx.fss.handle;

import com.aspose.words.Document;
import com.zx.fss.model.FileConvertInfo;
import com.zx.fss.util.FileAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@FileConvert(".doc|.docx")
@Service
public class DocFileConvert implements IFileConvert {
    @Autowired
    private FileAnalysisUtil fileAnalysisUtil;

    @Override
    public FileConvertInfo convertFile(FileConvertInfo fileConvertInfo) {
        try {
            fileConvertInfo.setConvertType("doc");
            Document doc = new Document(fileConvertInfo.getFilePath());
            fileConvertInfo.setPageNum(doc.getPageCount());
            File file = new File(fileConvertInfo.getFileDirPath() + "split_1.jpeg");
            if(file.exists()){
                fileConvertInfo.setHaveConvert(true);
                return fileConvertInfo;
            }
            fileAnalysisUtil.convertFileToJPG(fileConvertInfo);
        } catch (Exception e) {
            fileConvertInfo.setErrorInfo("转换doc文档发生错误");
            e.printStackTrace();
        }
        return fileConvertInfo;
    }
}
