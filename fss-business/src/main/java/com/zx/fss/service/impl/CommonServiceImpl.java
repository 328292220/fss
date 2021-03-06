package com.zx.fss.service.impl;

import com.zx.fss.account.User;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.zx.fss.business.File;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.business.dto.DeleteDTO;
import com.zx.fss.constant.SpecialSymbolsUtil;
import com.zx.fss.service.CommonService;
import com.zx.fss.service.DirService;
import com.zx.fss.service.FileService;
import com.zx.fss.ustils.DownloadUtil;
import com.zx.fss.utils.LoginUserHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    DirService dirService;
    @Autowired
    FileService fileService;
    @Value("${server_ip}")
    String serverIp;

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
        fileList.stream().forEach(one->{
            String realPath = one.getRealPath();
            realPath = "file:"+ SpecialSymbolsUtil.separator+SpecialSymbolsUtil.separator+SpecialSymbolsUtil.separator+realPath;
            realPath = Base64.getEncoder().encodeToString(realPath.getBytes());
            realPath = "http://"+ serverIp+":9001/view/plaform/index.html?file=" + realPath;
            one.setRealPath(realPath);
        });

        Map<String,Object> map  = new HashMap<>();
        map.put("dirList",dirList);
        map.put("fileList",fileList);
        return map;
    }

    @Override
    public Result download(Map<String, Object> data) {
        User currentUser = LoginUserHolder.getCurrentUser();
        Integer id = (Integer) data.get("id");
        String type = (String) data.get("type");
        File file = null;
        if("file".equalsIgnoreCase(type)){
            file = fileService.getById(id);
            if(file == null){
                //Result.fail("文件不存在");
            }
            if(currentUser != null && file.getUserId() != currentUser.getUserId()){
                Result.fail("没有权限");
            }
            //DownloadUtil.download(new java.io.File(file.getRealPath()));
            DownloadUtil.download(file.getRealPath());
        }

        return null;

    }

    @Override
    @Transactional
    public void del(DeleteDTO data) {
        //删除目录
        if(!CollectionUtils.isEmpty(data.getDirIds())){
            data.getDirIds().stream().forEach(dirId->{
                dirService.del(dirId);
            });
        }
        //删除文件
        if(!CollectionUtils.isEmpty(data.getFileIds())){
            fileService.removeByIds(data.getFileIds());
        }
    }
}
