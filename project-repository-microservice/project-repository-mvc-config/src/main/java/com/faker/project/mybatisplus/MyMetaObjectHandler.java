package com.faker.project.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 配置自动填充 拦截器
 * 可以修改属性 但是需要在原有类上添加 fill = FieldFill.xxx 例：RepositoryUser的create_time属性
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //设置创建时间和更新时间
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date().getTime(), metaObject);   //创建时间
        this.setFieldValByName("updateTime", new Date().getTime(), metaObject);   //更新时间
    }

    //update操作时填充方法
    @Override
    public void updateFill(MetaObject metaObject) { //填充 更新时间
        this.setFieldValByName("updateTime", new Date().getTime(), metaObject);
    }
}
