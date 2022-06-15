package com.zx.fss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zx.fss.account.User;
import com.zx.fss.api.ResultCode;
import com.zx.fss.business.Dir;
import com.zx.fss.business.File;
import com.zx.fss.business.FileType;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.constant.SpecialSymbolsUtil;
import com.zx.fss.enums.FileTypeEnum;
import com.zx.fss.exception.ResultException;
import com.zx.fss.mapper.FileMapper;
import com.zx.fss.service.DirService;
import com.zx.fss.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.fss.service.FileTypeService;
import com.zx.fss.ustils.FileUtil;
import com.zx.fss.utils.DateUtil;
import com.zx.fss.utils.LoginUserHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文件信息 服务实现类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Autowired
    FileTypeService fileTypeService;

    @Autowired
    @Lazy
    DirService dirService;

    @Value("${basePath}")
    private String basePath;

    @Override
    public List<File> queryByDirId(CommonDTO commonDTO) {
        User currentUser = LoginUserHolder.getCurrentUser();
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper();
        wrapper.eq(File::getUserId, currentUser.getUserId());
        wrapper.eq(File::getDirId,commonDTO.getParentId());
        if(StringUtils.isNotBlank(commonDTO.getOrderByType()) && "ASC".equals(commonDTO.getOrderByType().toUpperCase())){
            wrapper.orderByAsc(File::getLastUpdateDate);
        }
        if(StringUtils.isNotBlank(commonDTO.getOrderByType()) && "DESC".equals(commonDTO.getOrderByType().toUpperCase())){
            wrapper.orderByDesc(File::getLastUpdateDate);
        }

        List<File> list = this.list(wrapper);
        return list;
    }

    @Override
    @Transactional
    public void upload(Long parentId,MultipartFile... files) throws ResultException {
        //获取父目录
        Dir parentDir = dirService.getParentDir(parentId);

        User user = LoginUserHolder.getCurrentUser();
        String dirPath = basePath +
                SpecialSymbolsUtil.separator + user.getUserName() +
                SpecialSymbolsUtil.separator + DateUtil.format(new Date(),DateUtil.PATTERN_YYYYMMDD);
        java.io.File dir = new java.io.File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        for (MultipartFile file : files) {

            try {
//                //获取InputStream
//                InputStream in = file.getInputStream();
//                //根据文件头获取文件类型
//                String typeName = FileTypeEnum.getFileType(in);
//                //文件类型处理
//                FileType fileType = fileTypeService.add(typeName);
                //文件真实名称
                String fileName = file.getOriginalFilename();
                //文件保存的真实名称
                String realName = DateUtil.format(new Date(),DateUtil.PATTERN_YYYYMMDDHHMMSSSSS)+"_"+fileName;
                java.io.File dest = new java.io.File(dirPath, realName);
                //保存真实文件
                file.transferTo(dest);

                /**保存文件*/
                File myFile = new File();
                myFile.setFileSize(file.getSize());
                //myFile.setTypeId(fileType.getTypeId());
                myFile.setFileName(FileUtil.shortName(fileName));
                myFile.setExtName(FileUtil.extName(fileName));
                myFile.setUserId(user.getUserId());
                myFile.setDirId(parentDir.getDirId());
                myFile.setRealPath(dirPath+SpecialSymbolsUtil.separator+realName);
                this.save(myFile);
                log.info("上传成功");
            } catch (IOException e) {
                log.error(e.toString(), e);
                throw new ResultException(ResultCode.FILE_UPLOAD_EXCEPTION);
            }
        }
    }
}
