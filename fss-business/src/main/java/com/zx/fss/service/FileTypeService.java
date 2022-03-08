package com.zx.fss.service;

import com.zx.fss.business.FileType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文件类型 服务类
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-03-01
 */
public interface FileTypeService extends IService<FileType> {

    FileType add(String name);

}
