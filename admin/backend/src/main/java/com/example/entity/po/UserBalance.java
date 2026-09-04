package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额实体类
 *
 * 作用：存储用户的余额数据
 *
 * 对应数据库表：user_balance
 */
@TableName("user_balance")
public class UserBalance {

    /** 主键，余额记录ID */
    private Long id;

    /** 用户ID，关联 sys_user 表 */
    private Long userId;

    /** 当前余额 */
    private BigDecimal balance;

    /** 可用余额（未冻结部分） */
    private BigDecimal availableBalance;

    /** 冻结余额 */
    private BigDecimal frozenBalance;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }

    public BigDecimal getFrozenBalance() { return frozenBalance; }
    public void setFrozenBalance(BigDecimal frozenBalance) { this.frozenBalance = frozenBalance; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
