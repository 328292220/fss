package com.zx.fss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zx.fss.account.User;
import com.zx.fss.business.Dir;
import com.zx.fss.business.File;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.mapper.FileMapper;
import com.zx.fss.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.fss.utils.LoginUserHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文件信息 服务实现类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

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
}
