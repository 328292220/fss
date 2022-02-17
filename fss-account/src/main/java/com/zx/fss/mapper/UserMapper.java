package com.zx.fss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zx.fss.account.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author guoenhong@crb.cn
 * @since 2022-02-08
 */
//@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<String> getRolesByUserId(Long userId);
}
