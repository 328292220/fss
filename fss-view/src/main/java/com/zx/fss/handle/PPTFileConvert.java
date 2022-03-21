package com.zx.fss.handle;


import com.aspose.slides.Presentation;
import com.zx.fss.model.FileConvertInfo;
import com.zx.fss.util.FileAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@FileConvert(".ppt|.pptx")
@Service
public class PPTFileConvert implements IFileConvert {
    @Autowired
    private FileAnalysisUtil fileAnalysisUtil;

    @Override
    public FileConvertInfo convertFile(FileConvertInfo fileConvertInfo) {
        try {
            fileConvertInfo.setConvertType("ppt");
            Presentation ppt = new Presentation(fileConvertInfo.getFilePath());
            fileConvertInfo.setPageNum(ppt.getSlides().size());
            File file = new File(fileConvertInfo.getFileDirPath() + "split_1.jpeg");
            if(file.exists()){
                fileConvertInfo.setHaveConvert(true);
                return fileConvertInfo;
            }
            fileAnalysisUtil.convertFileToJPG(fileConvertInfo);
        } catch (Exception e) {
            fileConvertInfo.setErrorInfo("转换ppt文档发生错误");
            e.printStackTrace();
        }
        return fileConvertInfo;
    }
}
