package com.zx.fss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zx.fss.business.FileType;
import com.zx.fss.mapper.FileTypeMapper;
import com.zx.fss.service.FileTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件类型 服务实现类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Service
public class FileTypeServiceImpl extends ServiceImpl<FileTypeMapper, FileType> implements FileTypeService {

    @Override
    public FileType add(String name) {
        LambdaQueryWrapper<FileType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileType::getName,name);
        FileType one = this.getOne(wrapper);
        if(one == null){
            FileType fileType = new FileType();
            fileType.setName(name);
            this.save(fileType);
            one = fileType;
        }
        return one;

    }
}
