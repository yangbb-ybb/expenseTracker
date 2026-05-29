package com.example.entity.dto.consume;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 消费明细分页查询 DTO
 *
 * 作用：接收前端分页查询请求参数
 */
public class UserDailyConsumeDetailQueryDTO {

    /** 消费日期（格式：yyyy-MM-dd，可选） */
    private String consumeDate;

    /** 页码（默认 1） */
    @Min(value = 1, message = "页码必须大于0")
    @Max(value = 10000, message = "页码不能超过10000")
    private Integer page = 1;

    /** 每页数量（默认 10，最大 100） */
    @Min(value = 1, message = "每页数量必须大于0")
    @Max(value = 100, message = "每页数量不能超过100")
    private Integer size = 10;

    // ========== Getter 和 Setter ==========

    public String getConsumeDate() { return consumeDate; }
    public void setConsumeDate(String consumeDate) { this.consumeDate = consumeDate; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
