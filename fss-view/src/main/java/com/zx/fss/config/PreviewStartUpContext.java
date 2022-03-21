package com.zx.fss.config;

import com.zx.fss.util.FileAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PreviewStartUpContext implements ApplicationRunner {
    @Autowired
    private FileAnalysisUtil fileAnalysisUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        fileAnalysisUtil.initFileConvertRules();
    }
}
