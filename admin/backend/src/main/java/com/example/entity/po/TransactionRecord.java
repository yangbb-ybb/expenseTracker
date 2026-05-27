package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易流水实体类
 *
 * 作用：记录用户余额变动的详细流水
 *
 * 对应数据库表：transaction_record
 */
@TableName("transaction_record")
public class TransactionRecord {

    /** 主键，流水ID */
    private Long id;

    /** 用户ID，关联 sys_user 表 */
    private Long userId;

    /** 交易类型：1=充值，2=消费，3=退款，4=提现，5=转账 */
    private Integer type;

    /** 流水号（唯一，用于防止重复提交） */
    private String transactionNo;

    /** 变动金额（正数表示收入，负数表示支出） */
    private BigDecimal amount;

    /** 变动后余额 */
    private BigDecimal balanceAfter;

    /** 关联业务订单号（如：商品订单号、充值订单号等） */
    private String orderNo;

    /** 交易描述 */
    private String description;

    /** 交易状态：1=成功，2=失败，3=处理中 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getTransactionNo() { return transactionNo; }
    public void setTransactionNo(String transactionNo) { this.transactionNo = transactionNo; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
