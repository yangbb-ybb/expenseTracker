package com.example.entity.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 用户每日消费明细 添加/更新 DTO
 *
 * 作用：接收前端添加/更新消费明细的请求参数
 */
public class UserDailyConsumeDetailDTO {

    /** 主键，更新时必须提供 */
    private Long id;

    /** 用户ID */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 消费日期 */
    @NotNull(message = "消费日期不能为空")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "消费日期格式应为：yyyy-MM-dd")
    private String consumeDate;

    /** 消费金额 */
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal consumeAmount;

    /** 消费类型/渠道 */
    @Pattern(regexp = "^(alipay|wechat|unionpay|cash|other)$", message = "消费类型只能是：alipay, wechat, unionpay, cash, other")
    private String consumeType;

    /** 消费分类 */
    @NotNull(message = "消费分类不能为空")
    @Pattern(regexp = "^(entertainment|shopping|refund|daily_necessities|dining|transport|education|medical|insurance|other|shop_out)$", message = "消费分类只能是：entertainment, shopping, refund, daily_necessities, dining, transport, education, medical, insurance, other, shop_out")
    private String consumeCategory;

    /** 备注/消费描述 */
    @Size(max = 255, message = "备注长度不能超过255个字符")
    private String description;

    /** 是否计入总计：0=计入，1=仅展示不计入 */
    @Min(value = 0, message = "isExcludedFromTotal 必须为 0 或 1")
    @Max(value = 1, message = "isExcludedFromTotal 必须为 0 或 1")
    private Integer isExcludedFromTotal;

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getConsumeDate() { return consumeDate; }
    public void setConsumeDate(String consumeDate) { this.consumeDate = consumeDate; }

    public BigDecimal getConsumeAmount() { return consumeAmount; }
    public void setConsumeAmount(BigDecimal consumeAmount) { this.consumeAmount = consumeAmount; }

    public String getConsumeType() { return consumeType; }
    public void setConsumeType(String consumeType) { this.consumeType = consumeType; }

    public String getConsumeCategory() { return consumeCategory; }
    public void setConsumeCategory(String consumeCategory) { this.consumeCategory = consumeCategory; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getIsExcludedFromTotal() { return isExcludedFromTotal; }
    public void setIsExcludedFromTotal(Integer isExcludedFromTotal) { this.isExcludedFromTotal = isExcludedFromTotal; }
}
