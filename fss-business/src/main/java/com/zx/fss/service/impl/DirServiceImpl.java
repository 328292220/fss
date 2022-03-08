package com.zx.fss.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.fss.account.User;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.zx.fss.business.File;
import com.zx.fss.business.dto.CommonDTO;
import com.zx.fss.constant.SpecialSymbolsUtil;
import com.zx.fss.exception.BusinessException;
import com.zx.fss.mapper.DirMapper;
import com.zx.fss.service.DirService;
import com.zx.fss.service.FileService;
import com.zx.fss.utils.LoginUserHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 文件夹 服务实现类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
@Service
public class DirServiceImpl extends ServiceImpl<DirMapper, Dir> implements DirService {

    @Autowired
    @Lazy
    FileService fileService;

    @Override
    @Transactional
    public void add(Dir dir) {
        //获取父目录
        Dir parentDir = getParentDir(dir.getParentId());
        //构建新目录
        Dir newDir = createNewDir(parentDir,dir);
        this.save(newDir);
    }

    @Override
    public Result updateDir(Dir dir) {
        LambdaQueryWrapper<Dir> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dir::getDirId,dir.getDirId());
        wrapper.eq(Dir::getIsDeleted,0L);
        Dir dbDir = this.getOne(wrapper);
        if(Objects.isNull(dbDir)){
            return Result.fail("无需要更新的数据，请刷新页面后再试");
        }
        if(dbDir.getObjectVersionNumber() != dir.getObjectVersionNumber()){
            return Result.fail("版本号不一致，请刷新页面后再试");
        }
        User currentUser = LoginUserHolder.getCurrentUser();
        if(dbDir.getName().equals(currentUser.getUserName())){
            return Result.fail("非法操作，不能修改根目录");
        }


        //构建临时父目录对象
        String parenPath = dbDir.getPath().substring(0,dbDir.getPath().lastIndexOf(SpecialSymbolsUtil.separator));
        Dir tempParentDir = Dir.builder().dirId(dbDir.getParentId()).build();
        tempParentDir.setPath(parenPath);
        //获取需要修改的对象
        Dir newDir = createNewDir(tempParentDir, dir);
        newDir.setDirId(dbDir.getDirId());
        newDir.setObjectVersionNumber(dbDir.getObjectVersionNumber());
        this.updateById(newDir);
        return Result.success();

    }

    @Override
    @Transactional
    public void del(Long dirId) {
        //获取所有子目录
        List<Dir> children = getChildren(dirId, null);
        //获取所有目录及其子目录dirId
        List<Long> dirIds = children.stream().map(Dir::getDirId).collect(Collectors.toList());
        dirIds.add(dirId);
        //通过dirId删除目录
        this.removeByIds(dirIds);

        //todo 删除文件操作
        //通过dirId删除文件
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper();
        wrapper.in(File::getDirId,dirIds);
        fileService.remove(wrapper);

    }

    @Override
    public List<Dir> queryByParentId(CommonDTO commonDTO) {
        User currentUser = LoginUserHolder.getCurrentUser();
        LambdaQueryWrapper<Dir> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dir::getUserId, currentUser.getUserId());
        wrapper.eq(Dir::getParentId,commonDTO.getParentId());
        if(StringUtils.isNotBlank(commonDTO.getOrderByType()) && "ASC".equals(commonDTO.getOrderByType().toUpperCase())){
            wrapper.orderByAsc(Dir::getLastUpdateDate);
        }
        if(StringUtils.isNotBlank(commonDTO.getOrderByType()) && "DESC".equals(commonDTO.getOrderByType().toUpperCase())){
            wrapper.orderByDesc(Dir::getLastUpdateDate);
        }
        List<Dir> list = this.list(wrapper);
        return list;
    }

    /**
     * 获取所有子目录
     * @param parentId
     * @param resultList
     * @return
     */

    private List<Dir> getChildren(Long parentId,List<Dir> resultList) {
        if(CollectionUtils.isEmpty(resultList)){
            resultList = new ArrayList<>();
        }
        User currentUser = LoginUserHolder.getCurrentUser(User.class);
        LambdaQueryWrapper<Dir> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dir::getUserId, currentUser.getUserId());
        wrapper.eq(Dir::getParentId,parentId);
        wrapper.eq(Dir::getIsDeleted,0L);
        List<Dir> children = this.list(wrapper);
        if(!CollectionUtils.isEmpty(children)){
            resultList.addAll(children);
            List<Dir> finalResultList = resultList;
            children.stream().forEach(one -> getChildren(one.getDirId(), finalResultList));
        }
        return resultList;
    }


    /**
     * 获取父目录(数据库没有则新增)
     * @param parentId
     * @return
     */
    @Override
    public Dir getParentDir(Long parentId) {
        User currentUser = LoginUserHolder.getCurrentUser(User.class);
        LambdaQueryWrapper<Dir> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dir::getUserId, currentUser.getUserId());
        if(parentId == null){
            wrapper.isNull(Dir::getParentId);
        } else {
            wrapper.eq(Dir::getDirId,parentId);

        }
        //这里条件不能加is_deleted,因为可以还原删除的目录
        Dir dir = this.getOne(wrapper);
        if(Objects.isNull(dir)){
            if(parentId == null){
                //没有父级，说明用户第一次创建文件夹，那么默认创建一个用户根目录（用户名命名）
                dir = Dir.builder()
                        .userId(LoginUserHolder.getCurrentUser(User.class).getUserId())
                        .name(currentUser.getUserName())
                        .path("")
                        .build();
                this.save(dir);
            }else {
                throw new BusinessException(500,"非法参数值",null);
            }
        }
        return dir;

    }

    private Dir createNewDir(Dir parentDir, Dir dir) {
        User currentUser = LoginUserHolder.getCurrentUser(User.class);
        //判断当前父目录下有没有相同名字目录
        LambdaQueryWrapper<Dir> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dir::getUserId, currentUser.getUserId());
        wrapper.eq(Dir::getParentId,parentDir.getDirId());
        wrapper.eq(Dir::getName,dir.getName());
        if(dir.getDirId() != null){
            //修改，需要排除自己
            wrapper.ne(Dir::getDirId,dir.getDirId());
        }
        //TODO 还原时需要判断是否重名
        wrapper.eq(Dir::getIsDeleted,0L);
        Dir dbDir = this.getOne(wrapper);
        String dirName = dir.getName();
        if(Objects.nonNull(dbDir)){
            //有重命名
            dirName += "_" + DateUtil.format(new Date(),"yyyyMMdd_HHmmss");
        }
        Dir newDir = Dir.builder().build();
        newDir.setUserId(currentUser.getUserId());
        newDir.setParentId(parentDir.getDirId());
        newDir.setName(dirName);
        newDir.setPath(parentDir.getPath()+SpecialSymbolsUtil.separator+dir.getName());
        return newDir;

    }
}
