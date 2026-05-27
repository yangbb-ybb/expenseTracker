package com.example.entity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 交易流水查询 DTO
 *
 * 作用：接收前端流水查询请求参数
 */
public class TransactionRecordQueryDTO {

    /** 用户ID（必填） */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 交易类型：1=充值，2=消费，3=退款，4=提现，5=冻结/解冻（可选） */
    private Integer type;

    /** 页码（默认 1） */
    @Min(value = 1, message = "页码必须大于0")
    @Max(value = 10000, message = "页码不能超过10000")
    private Integer page = 1;

    /** 每页数量（默认 10，最大 100） */
    @Min(value = 1, message = "每页数量必须大于0")
    @Max(value = 100, message = "每页数量不能超过100")
    private Integer size = 10;

    /** 开始时间（可选） */
    private String startTime;

    /** 结束时间（可选） */
    private String endTime;

    // ========== Getter 和 Setter ==========

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
