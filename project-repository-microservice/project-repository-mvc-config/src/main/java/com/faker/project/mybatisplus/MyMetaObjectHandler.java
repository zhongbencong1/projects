package com.faker.project.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {//实现MetaObjectHandler
    //配置自动填充 拦截器
    //insert操作时填充方法
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);   //创建时间
        this.setFieldValByName("updateTime", new Date(), metaObject);   //更新时间
    }
    //update操作时填充方法
    @Override
    public void updateFill(MetaObject metaObject) { //填充 更新时间
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
