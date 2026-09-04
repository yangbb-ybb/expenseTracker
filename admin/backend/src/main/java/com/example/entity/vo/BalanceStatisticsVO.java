package com.example.entity.vo;

import java.math.BigDecimal;

/**
 * 余额统计信息 VO
 *
 * 作用：返回余额统计信息
 */
public class BalanceStatisticsVO {

    /** 用户总数 */
    private Long totalUsers;

    /** 总余额 */
    private BigDecimal totalBalance;

    /** 总可用余额 */
    private BigDecimal totalAvailableBalance;

    /** 总冻结余额 */
    private BigDecimal totalFrozenBalance;

    /** 今日充值总额 */
    private BigDecimal todayRechargeAmount;

    /** 今日消费总额 */
    private BigDecimal todayConsumeAmount;

    /** 本月充值总额 */
    private BigDecimal monthRechargeAmount;

    /** 本月消费总额 */
    private BigDecimal monthConsumeAmount;

    // ========== Getter 和 Setter ==========

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public BigDecimal getTotalBalance() { return totalBalance; }
    public void setTotalBalance(BigDecimal totalBalance) { this.totalBalance = totalBalance; }

    public BigDecimal getTotalAvailableBalance() { return totalAvailableBalance; }
    public void setTotalAvailableBalance(BigDecimal totalAvailableBalance) { this.totalAvailableBalance = totalAvailableBalance; }

    public BigDecimal getTotalFrozenBalance() { return totalFrozenBalance; }
    public void setTotalFrozenBalance(BigDecimal totalFrozenBalance) { this.totalFrozenBalance = totalFrozenBalance; }

    public BigDecimal getTodayRechargeAmount() { return todayRechargeAmount; }
    public void setTodayRechargeAmount(BigDecimal todayRechargeAmount) { this.todayRechargeAmount = todayRechargeAmount; }

    public BigDecimal getTodayConsumeAmount() { return todayConsumeAmount; }
    public void setTodayConsumeAmount(BigDecimal todayConsumeAmount) { this.todayConsumeAmount = todayConsumeAmount; }

    public BigDecimal getMonthRechargeAmount() { return monthRechargeAmount; }
    public void setMonthRechargeAmount(BigDecimal monthRechargeAmount) { this.monthRechargeAmount = monthRechargeAmount; }

    public BigDecimal getMonthConsumeAmount() { return monthConsumeAmount; }
    public void setMonthConsumeAmount(BigDecimal monthConsumeAmount) { this.monthConsumeAmount = monthConsumeAmount; }
}
