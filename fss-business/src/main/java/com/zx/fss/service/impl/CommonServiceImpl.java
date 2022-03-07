package com.zx.fss.service.impl;

import com.zx.fss.business.Dir;
import com.zx.fss.business.File;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.service.CommonService;
import com.zx.fss.service.DirService;
import com.zx.fss.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CommonServiceImpl implements CommonService {

    DirService dirService;
    FileService fileService;

    @Override
    public Map<String, Object> query(CommonDTO commonDTO) {
        if(commonDTO.getParentId() == null){
            //父目录为空，那么获取的是顶级目录下的文件合文件夹，需要先把顶级目录ID查出来
            //获取父目录
            Dir parentDir = dirService.getParentDir(null);
            commonDTO.setParentId(parentDir.getDirId());
        }

        //获取目录
        List<Dir> dirList = dirService.queryByParentId(commonDTO);

        //获取文件
        List<File> fileList = fileService.queryByDirId(commonDTO);

        Map<String,Object> map  = new HashMap<>();
        map.put("dirList",dirList);
        map.put("fileList",fileList);
        return map;
    }
}
