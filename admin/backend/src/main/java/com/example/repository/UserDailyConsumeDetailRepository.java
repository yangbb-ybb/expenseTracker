package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.po.UserDailyConsumeDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户每日消费明细 Mapper
 *
 * 作用：操作数据库的 user_daily_consume_detail 表
 */
@Mapper
public interface UserDailyConsumeDetailRepository extends BaseMapper<UserDailyConsumeDetail> {

}
