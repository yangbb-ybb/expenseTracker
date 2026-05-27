package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.po.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易流水 Mapper
 *
 * 作用：操作数据库的 transaction_record 表
 */
@Mapper
public interface TransactionRecordRepository extends BaseMapper<TransactionRecord> {

}
