package com.example.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * MyBatis Plus 自动填充配置
 *
 * 作用：当我们新增或修改数据时，自动填充时间字段，无需手动设置
 *
 * 什么时候触发？
 *   - 新增数据时（insert）：自动填充 createTime 和 updateTime
 *   - 修改数据时（update）：自动填充 updateTime
 *
 * 为什么需要这个？
 *   - 不用每次插入/修改时手动设置时间
 *   - 保证时间字段的准确性
 *   - 符合规范：数据库表的时间字段应该由数据库或框架管理
 *
 * @Component 注册为 Spring Bean，被 Spring 管理
 */
@Component  // 等价于 <bean id="mybatisPlusConfig" class="...MybatisPlusConfig"/>
public class MybatisPlusConfig implements MetaObjectHandler {

    /**
     * 新增数据时自动填充
     *
     * @param metaObject MyBatis 传入的对象，包含即将插入的数据
     *
     * strictInsertFill = 严格模式插入填充
     *   只有当字段值为 null 时才填充，避免覆盖手动设置的值
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充创建时间字段为当前时间
        // 参数：metaObject（数据对象），"createTime"（字段名），LocalDateTime.class（类型），LocalDateTime.now()（值）
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());

        // 填充更新时间字段为当前时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 修改数据时自动填充
     *
     * @param metaObject MyBatis 传入的对象，包含即将更新的数据
     *
     * strictUpdateFill = 严格模式更新填充
     *   只有当字段值为 null 时才填充，避免覆盖手动设置的值
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充更新时间字段为当前时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
