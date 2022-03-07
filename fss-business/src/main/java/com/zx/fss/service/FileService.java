package com.zx.fss.service;

import com.zx.fss.business.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.fss.business.dto.CommonDTO;

import java.util.List;

/**
 * <p>
 * 文件信息 服务类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
public interface FileService extends IService<File> {

    List<File> queryByDirId(CommonDTO commonDTO);
}
