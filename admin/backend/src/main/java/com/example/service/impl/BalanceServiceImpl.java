package com.example.service.impl;

import com.example.entity.po.TransactionRecord;
import com.example.entity.po.UserBalance;
import com.example.repository.TransactionRecordRepository;
import com.example.repository.UserBalanceRepository;
import com.example.service.BalanceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 余额业务实现类
 *
 * 作用：实现余额相关业务逻辑
 *
 * @Transactional 注解说明：
 *   - 默认开启事务管理
 *   - 如果方法执行失败，自动回滚
 */
@Service
public class BalanceServiceImpl implements BalanceService {

    @Resource
    private UserBalanceRepository userBalanceRepository;

    @Resource
    private TransactionRecordRepository transactionRecordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recharge(Long userId, BigDecimal amount, String orderNo, String description) {
        // 参数校验
        if (userId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // 确保余额记录存在
        UserBalance balance = ensureBalanceExists(userId);

        // 更新余额
        BigDecimal newBalance = balance.getBalance().add(amount);
        BigDecimal newAvailableBalance = balance.getAvailableBalance().add(amount);

        balance.setBalance(newBalance);
        balance.setAvailableBalance(newAvailableBalance);
        balance.setUpdateTime(LocalDateTime.now());

        userBalanceRepository.updateById(balance);

        // 记录流水
        createTransactionRecord(userId, 1, amount, newBalance, orderNo, description, 1);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean consume(Long userId, BigDecimal amount, String orderNo, String description) {
        // 参数校验
        if (userId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // 确保余额记录存在
        UserBalance balance = ensureBalanceExists(userId);

        // 检查余额是否充足
        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            return false;
        }

        // 更新余额
        BigDecimal newBalance = balance.getBalance().subtract(amount);
        BigDecimal newAvailableBalance = balance.getAvailableBalance().subtract(amount);

        balance.setBalance(newBalance);
        balance.setAvailableBalance(newAvailableBalance);
        balance.setUpdateTime(LocalDateTime.now());

        userBalanceRepository.updateById(balance);

        // 记录流水（消费为负数）
        BigDecimal negativeAmount = amount.negate();
        createTransactionRecord(userId, 2, negativeAmount, newBalance, orderNo, description, 1);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refund(Long userId, BigDecimal amount, String orderNo, String description) {
        // 参数校验
        if (userId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // 确保余额记录存在
        UserBalance balance = ensureBalanceExists(userId);

        // 更新余额
        BigDecimal newBalance = balance.getBalance().add(amount);
        BigDecimal newAvailableBalance = balance.getAvailableBalance().add(amount);

        balance.setBalance(newBalance);
        balance.setAvailableBalance(newAvailableBalance);
        balance.setUpdateTime(LocalDateTime.now());

        userBalanceRepository.updateById(balance);

        // 记录流水
        createTransactionRecord(userId, 3, amount, newBalance, orderNo, description, 1);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean freeze(Long userId, BigDecimal amount, String orderNo, String description) {
        // 参数校验
        if (userId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // 确保余额记录存在
        UserBalance balance = ensureBalanceExists(userId);

        // 检查可用余额是否充足
        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            return false;
        }

        // 更新余额
        BigDecimal newAvailableBalance = balance.getAvailableBalance().subtract(amount);
        BigDecimal newFrozenBalance = balance.getFrozenBalance().add(amount);

        balance.setAvailableBalance(newAvailableBalance);
        balance.setFrozenBalance(newFrozenBalance);
        balance.setUpdateTime(LocalDateTime.now());

        userBalanceRepository.updateById(balance);

        // 记录流水（冻结为负数）
        BigDecimal negativeAmount = amount.negate();
        createTransactionRecord(userId, 5, negativeAmount, balance.getBalance(), orderNo, "冻结：" + description, 1);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfreeze(Long userId, BigDecimal amount, String orderNo, String description) {
        // 参数校验
        if (userId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // 确保余额记录存在
        UserBalance balance = ensureBalanceExists(userId);

        // 检查冻结余额是否充足
        if (balance.getFrozenBalance().compareTo(amount) < 0) {
            return false;
        }

        // 更新余额
        BigDecimal newAvailableBalance = balance.getAvailableBalance().add(amount);
        BigDecimal newFrozenBalance = balance.getFrozenBalance().subtract(amount);

        balance.setAvailableBalance(newAvailableBalance);
        balance.setFrozenBalance(newFrozenBalance);
        balance.setUpdateTime(LocalDateTime.now());

        userBalanceRepository.updateById(balance);

        // 记录流水（解冻为正数）
        createTransactionRecord(userId, 5, amount, balance.getBalance(), orderNo, "解冻：" + description, 1);

        return true;
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        if (userId == null) {
            return null;
        }
        UserBalance balance = userBalanceRepository.selectById(userId);
        return balance != null ? balance.getBalance() : null;
    }

    @Override
    public BigDecimal getAvailableBalance(Long userId) {
        if (userId == null) {
            return null;
        }
        UserBalance balance = userBalanceRepository.selectById(userId);
        return balance != null ? balance.getAvailableBalance() : null;
    }

    /**
     * 确保余额记录存在
     */
    private UserBalance ensureBalanceExists(Long userId) {
        UserBalance balance = userBalanceRepository.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserBalance>()
                        .eq(UserBalance::getUserId, userId)
        );

        if (balance == null) {
            balance = new UserBalance();
            balance.setUserId(userId);
            balance.setBalance(BigDecimal.ZERO);
            balance.setAvailableBalance(BigDecimal.ZERO);
            balance.setFrozenBalance(BigDecimal.ZERO);
            balance.setCreateTime(LocalDateTime.now());
            balance.setUpdateTime(LocalDateTime.now());
            userBalanceRepository.insert(balance);
        }

        return balance;
    }

    /**
     * 创建交易流水记录
     */
    private void createTransactionRecord(Long userId, Integer type, BigDecimal amount,
                                         BigDecimal balanceAfter, String orderNo,
                                         String description, Integer status) {
        TransactionRecord record = new TransactionRecord();
        record.setUserId(userId);
        record.setType(type);
        record.setTransactionNo("TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        record.setAmount(amount);
        record.setBalanceAfter(balanceAfter);
        record.setOrderNo(orderNo);
        record.setDescription(description);
        record.setStatus(status);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        transactionRecordRepository.insert(record);
    }
}
