package com.zx.fss.config.mybatis;

import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zx.fss.BaseEntity;
import com.zx.fss.account.User;
import com.zx.fss.utils.LoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        if (metaObject.getOriginalObject() instanceof BaseEntity) {
            Long userId = -1L;//没有用户
            User userInfo = LoginUserHolder.getCurrentUser(User.class);
            if(userInfo != null){
                userId = userInfo.getUserId();
            }
            this.setFieldValByName("createdBy", userId , metaObject);
            this.setFieldValByName("lastUpdatedBy", userId, metaObject);
            this.setFieldValByName("creationDate", new Date(), metaObject);
            this.setFieldValByName("lastUpdateDate", new Date(), metaObject);
        }
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        if (metaObject.getOriginalObject() instanceof BaseEntity) {
            Long userId = -1L;//没有用户
            User userInfo = LoginUserHolder.getCurrentUser(User.class);
            if(userInfo != null){
                userId = userInfo.getUserId();
            }
            this.setFieldValByName("lastUpdatedBy", userId, metaObject);
            this.setFieldValByName("last_update_date",new Date(),metaObject);
        }

    }
}
