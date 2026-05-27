package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.po.UserBalance;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户余额 Mapper
 *
 * 作用：操作数据库的 user_balance 表
 */
@Mapper
public interface UserBalanceRepository extends BaseMapper<UserBalance> {

}
