package com.example.entity.dto.consume;

import java.math.BigDecimal;

/**
 * 用户消费统计 DTO
 */
public class UserDailyConsumeDetailStatisticsDTO {

    /** 总支出 */
    private BigDecimal totalExpense;

    /** 总收入 */
    private BigDecimal totalIncome;

    public BigDecimal getTotalExpense() { return totalExpense; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
}
