package com.example.service.impl;

import com.example.entity.po.UserDailyConsumeDetail;
import com.example.repository.UserDailyConsumeDetailRepository;
import com.example.service.UserDailyConsumeDetailService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 用户每日消费明细业务实现类
 *
 * 作用：实现消费明细相关业务逻辑
 */
@Service
public class UserDailyConsumeDetailServiceImpl implements UserDailyConsumeDetailService {

    @Resource
    private UserDailyConsumeDetailRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDailyConsumeDetail addConsumeDetail(com.example.entity.dto.UserDailyConsumeDetailDTO dto) {
        // 检查是否存在相同的记录（用户+日期+类型+分类）
        UserDailyConsumeDetail existing = repository.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDailyConsumeDetail>()
                        .eq(UserDailyConsumeDetail::getUserId, dto.getUserId())
                        .eq(UserDailyConsumeDetail::getConsumeDate, LocalDate.parse(dto.getConsumeDate()))
                        .eq(UserDailyConsumeDetail::getConsumeType, dto.getConsumeType())
                        .eq(UserDailyConsumeDetail::getConsumeCategory, dto.getConsumeCategory())
                        .eq(UserDailyConsumeDetail::getIsDeleted, 0)
        );

        UserDailyConsumeDetail record = new UserDailyConsumeDetail();

        if (existing != null) {
            // 如果存在，累加金额
            BigDecimal newAmount = existing.getConsumeAmount().add(dto.getConsumeAmount());
            existing.setConsumeAmount(newAmount);
            existing.setUpdateTime(LocalDateTime.now());
            repository.updateById(existing);
            return existing;
        } else {
            // 如果不存在，创建新记录
            record.setUserId(dto.getUserId());
            record.setConsumeDate(LocalDate.parse(dto.getConsumeDate()));
            record.setConsumeAmount(dto.getConsumeAmount());
            record.setConsumeType(dto.getConsumeType());
            record.setConsumeCategory(dto.getConsumeCategory());
            record.setDescription(dto.getDescription());
            record.setIsDeleted(0);
            record.setIsExcludedFromTotal(dto.getIsExcludedFromTotal() != null ? dto.getIsExcludedFromTotal() : 0);
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            repository.insert(record);
            return record;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConsumeDetail(Long id) {
        UserDailyConsumeDetail record = repository.selectById(id);
        if (record == null || Objects.equals(record.getIsDeleted(), 1)) {
            return false;
        }
        record.setIsDeleted(1);
        record.setUpdateTime(LocalDateTime.now());
        return repository.updateById(record) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDailyConsumeDetail updateConsumeDetail(com.example.entity.dto.UserDailyConsumeDetailDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("更新时必须提供ID");
        }

        UserDailyConsumeDetail record = repository.selectById(dto.getId());
        if (record == null || Objects.equals(record.getIsDeleted(), 1)) {
            throw new RuntimeException("记录不存在或已被删除");
        }

        // 更新字段
        record.setConsumeAmount(dto.getConsumeAmount());
        record.setConsumeType(dto.getConsumeType());
        record.setConsumeCategory(dto.getConsumeCategory());
        record.setDescription(dto.getDescription());
        record.setIsExcludedFromTotal(dto.getIsExcludedFromTotal() != null ? dto.getIsExcludedFromTotal() : 0);
        record.setUpdateTime(LocalDateTime.now());

        repository.updateById(record);
        return record;
    }

    @Override
    public UserDailyConsumeDetail getById(Long id) {
        return repository.selectById(id);
    }

    @Override
    public List<UserDailyConsumeDetail> getListByUserAndDate(Long userId, String consumeDate) {
        return repository.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDailyConsumeDetail>()
                        .eq(UserDailyConsumeDetail::getUserId, userId)
                        .eq(UserDailyConsumeDetail::getConsumeDate, LocalDate.parse(consumeDate))
                        .eq(UserDailyConsumeDetail::getIsDeleted, 0)
                        .orderByDesc(UserDailyConsumeDetail::getConsumeAmount)
        );
    }
}
