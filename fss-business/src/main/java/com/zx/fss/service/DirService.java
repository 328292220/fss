package com.zx.fss.service;

import com.zx.fss.api.RequestParameter;
import com.zx.fss.api.Result;
import com.zx.fss.business.Dir;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.fss.business.dto.CommonDTO;

import java.util.List;

/**
 * <p>
 * 文件夹 服务类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
public interface DirService extends IService<Dir> {

    void add(Dir dir);

    Result updateDir(Dir dir);

    void del(Long dirId);

    List<Dir> queryByParentId(CommonDTO commonDTO);

    /**
     * 获取父目录，parentId为空，则创建顶级目录
     * @param parentId
     * @return
     */
    Dir getParentDir(Long parentId);
}
