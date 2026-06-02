package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.dto.consume.UserDailyConsumeDetailQueryDTO;
import com.example.entity.po.UserDailyConsumeDetail;
import com.example.repository.UserDailyConsumeDetailRepository;
import com.example.service.UserDailyConsumeDetailService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public UserDailyConsumeDetail addConsumeDetail(com.example.entity.dto.consume.UserDailyConsumeDetailDTO dto) {
        UserDailyConsumeDetail record = new UserDailyConsumeDetail();
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
    public UserDailyConsumeDetail updateConsumeDetail(com.example.entity.dto.consume.UserDailyConsumeDetailDTO dto) {
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
    public com.example.entity.dto.consume.UserDailyConsumeDetailStatisticsDTO getStatistics(Long userId) {
        return getStatistics(userId, null);
    }

    @Override
    public com.example.entity.dto.consume.UserDailyConsumeDetailStatisticsDTO getStatistics(Long userId, String consumeDate) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserDailyConsumeDetail> expenseWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        expenseWrapper.eq("user_id", userId)
                .eq("consume_type", "expense")
                .eq("is_deleted", 0);

        // 如果指定了日期，按日期查询
        if (consumeDate != null && !consumeDate.isEmpty()) {
            try {
                // 尝试解析为完整日期（yyyy-MM-dd）
                if (consumeDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    LocalDate date = LocalDate.parse(consumeDate);
                    expenseWrapper.eq("consume_date", date);
                } else if (consumeDate.matches("\\d{4}-\\d{2}")) {
                    // 解析为月份（yyyy-MM），查询该月的所有记录
                    LocalDate startDate = LocalDate.parse(consumeDate + "-01");
                    LocalDate endDate = startDate.plusMonths(1).minusDays(1);
                    expenseWrapper.between("consume_date", startDate, endDate);
                } else {
                    throw new IllegalArgumentException("日期格式错误，应为：yyyy-MM-dd 或 yyyy-MM");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("日期格式错误，应为：yyyy-MM-dd 或 yyyy-MM");
            }
        }

        expenseWrapper.select("COALESCE(SUM(consume_amount), 0) as total");
        Object expenseResult = repository.selectObjs(expenseWrapper).stream().findFirst().orElse(BigDecimal.ZERO);

        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserDailyConsumeDetail> incomeWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        incomeWrapper.eq("user_id", userId)
                .eq("consume_type", "income")
                .eq("is_deleted", 0);

        // 如果指定了日期，按日期查询
        if (consumeDate != null && !consumeDate.isEmpty()) {
            try {
                // 尝试解析为完整日期（yyyy-MM-dd）
                if (consumeDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    LocalDate date = LocalDate.parse(consumeDate);
                    incomeWrapper.eq("consume_date", date);
                } else if (consumeDate.matches("\\d{4}-\\d{2}")) {
                    // 解析为月份（yyyy-MM），查询该月的所有记录
                    LocalDate startDate = LocalDate.parse(consumeDate + "-01");
                    LocalDate endDate = startDate.plusMonths(1).minusDays(1);
                    incomeWrapper.between("consume_date", startDate, endDate);
                }
            } catch (Exception e) {
                // 不会到这里，上面已经验证过
            }
        }

        incomeWrapper.select("COALESCE(SUM(consume_amount), 0) as total");
        Object incomeResult = repository.selectObjs(incomeWrapper).stream().findFirst().orElse(BigDecimal.ZERO);

        com.example.entity.dto.consume.UserDailyConsumeDetailStatisticsDTO statistics =
                new com.example.entity.dto.consume.UserDailyConsumeDetailStatisticsDTO();
        statistics.setTotalExpense(new BigDecimal(expenseResult.toString()));
        statistics.setTotalIncome(new BigDecimal(incomeResult.toString()));
        statistics.setConsumeDate(consumeDate);
        return statistics;
    }

    @Override
    public IPage<UserDailyConsumeDetail> getListByUserAndDate(Long userId, UserDailyConsumeDetailQueryDTO queryDTO) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDailyConsumeDetail> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        wrapper.eq(UserDailyConsumeDetail::getUserId, userId);
        wrapper.eq(UserDailyConsumeDetail::getIsDeleted, 0);

        // 如果指定了日期，按日期查询（支持日期或月份格式）
        if (queryDTO.getConsumeDate() != null && !queryDTO.getConsumeDate().isEmpty()) {
            try {
                String consumeDate = queryDTO.getConsumeDate();
                // 尝试解析为完整日期（yyyy-MM-dd）
                if (consumeDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    LocalDate date = LocalDate.parse(consumeDate);
                    wrapper.eq(UserDailyConsumeDetail::getConsumeDate, date);
                } else if (consumeDate.matches("\\d{4}-\\d{2}")) {
                    // 解析为月份（yyyy-MM），查询该月的所有记录
                    LocalDate startDate = LocalDate.parse(consumeDate + "-01");
                    LocalDate endDate = startDate.plusMonths(1).minusDays(1);
                    wrapper.between(UserDailyConsumeDetail::getConsumeDate, startDate, endDate);
                } else {
                    throw new IllegalArgumentException("日期格式错误，应为：yyyy-MM-dd 或 yyyy-MM");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("日期格式错误，应为：yyyy-MM-dd 或 yyyy-MM");
            }
        }

        // 排序
        wrapper.orderByDesc(UserDailyConsumeDetail::getConsumeAmount);

        // 分页查询
        Page<UserDailyConsumeDetail> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return repository.selectPage(page, wrapper);
    }
}
