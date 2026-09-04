package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper（数据访问层）
 *
 * 作用：操作数据库的 user 表
 *
 * 为什么叫 Mapper？
 *   负责和数据库"对话"，映射 SQL 到对象
 *
 * @Mapper 标记这是一个 MyBatis Mapper 接口
 *         MyBatis Plus 会自动创建实现类
 *
 * BaseMapper<User> 提供的基础 CRUD 方法：
 *   - selectById(id)          根据ID查询
 *   - selectList(wrapper)     条件查询
 *   - insert(entity)           新增
 *   - updateById(entity)      修改
 *   - deleteById(id)          删除
 *   等等... 不用写 SQL，MyBatis Plus 自动提供
 */
@Mapper  // 标记为 MyBatis Mapper 接口
public interface UserRepository extends BaseMapper<User> {

    // 不需要写任何方法！
    // MyBatis Plus 自动提供增删改查

}
