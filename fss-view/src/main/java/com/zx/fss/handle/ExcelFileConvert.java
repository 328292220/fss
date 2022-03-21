package com.zx.fss.handle;

import com.aspose.cells.Workbook;
import com.zx.fss.model.FileConvertInfo;
import com.zx.fss.util.FileAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@FileConvert(".xls|.xlsx")
@Service
public class ExcelFileConvert implements IFileConvert {
    @Autowired
    private FileAnalysisUtil fileAnalysisUtil;

    @Override
    public FileConvertInfo convertFile(FileConvertInfo fileConvertInfo) {
        try {
            fileConvertInfo.setConvertType("excel");
            Workbook wb = new Workbook(fileConvertInfo.getFilePath());
            fileConvertInfo.setPageNum(1);
            File file = new File(fileConvertInfo.getFileDirPath() + "convert.html");
            if(file.exists()){
                fileConvertInfo.setHaveConvert(true);
                return fileConvertInfo;
            }
            fileAnalysisUtil.convertFileToJPG(fileConvertInfo);
        } catch (Exception e) {
            fileConvertInfo.setErrorInfo("转换excel文档发生错误");
            e.printStackTrace();
        }
        return fileConvertInfo;
    }

}
