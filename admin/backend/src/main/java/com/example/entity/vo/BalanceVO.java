package com.example.entity.vo;

import java.math.BigDecimal;

/**
 * 余额信息 VO
 *
 * 作用：返回给前端的余额信息
 */
public class BalanceVO {

    /** 用户ID */
    private Long userId;

    /** 当前余额 */
    private BigDecimal balance;

    /** 可用余额 */
    private BigDecimal availableBalance;

    // ========== Getter 和 Setter ==========

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }
}
