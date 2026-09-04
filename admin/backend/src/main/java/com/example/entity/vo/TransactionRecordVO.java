package com.example.entity.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易流水信息 VO
 *
 * 作用：返回给前端的流水记录信息
 */
public class TransactionRecordVO {

    /** 流水ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 交易类型：1=充值，2=消费，3=退款，4=提现，5=冻结/解冻 */
    private Integer type;

    /** 交易类型名称（如：充值、消费） */
    private String typeName;

    /** 流水号 */
    private String transactionNo;

    /** 变动金额 */
    private BigDecimal amount;

    /** 变动后余额 */
    private BigDecimal balanceAfter;

    /** 业务订单号 */
    private String orderNo;

    /** 交易描述 */
    private String description;

    /** 交易状态：1=成功，2=失败，3=处理中 */
    private Integer status;

    /** 交易状态名称 */
    private String statusName;

    /** 创建时间 */
    private LocalDateTime createTime;

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

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

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
