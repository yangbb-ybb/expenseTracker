package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户每日消费明细实体类
 *
 * 作用：存储用户每日消费明细数据
 *
 * 对应数据库表：user_daily_consume_detail
 */
@TableName("user_daily_consume_detail")
public class UserDailyConsumeDetail {

    /** 主键，每日消费记录ID */
    private Long id;

    /** 用户ID，关联 sys_user 表 */
    private Long userId;

    /** 消费日期 */
    private LocalDate consumeDate;

    /** 当日消费金额 */
    private BigDecimal consumeAmount;

    /** 消费类型/渠道：alipay=支付宝，wechat=微信，unionpay=银联，cash=现金等 */
    private String consumeType;

    /** 消费分类：entertainment=娱乐，shopping=购物，refund=退款，daily_necessities=日常开销，dining=餐饮，transport=交通，education=教育，medical=医疗，insurance=保险，other=其他，shop_out=购物支出 */
    private String consumeCategory;

    /** 备注/消费描述 */
    private String description;

    /** 是否删除：0=否，1=是 */
    private Integer isDeleted;

    /** 是否计入总计：0=计入，1=仅展示不计入 */
    private Integer isExcludedFromTotal;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getConsumeDate() { return consumeDate; }
    public void setConsumeDate(LocalDate consumeDate) { this.consumeDate = consumeDate; }

    public BigDecimal getConsumeAmount() { return consumeAmount; }
    public void setConsumeAmount(BigDecimal consumeAmount) { this.consumeAmount = consumeAmount; }

    public String getConsumeType() { return consumeType; }
    public void setConsumeType(String consumeType) { this.consumeType = consumeType; }

    public String getConsumeCategory() { return consumeCategory; }
    public void setConsumeCategory(String consumeCategory) { this.consumeCategory = consumeCategory; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

    public Integer getIsExcludedFromTotal() { return isExcludedFromTotal; }
    public void setIsExcludedFromTotal(Integer isExcludedFromTotal) { this.isExcludedFromTotal = isExcludedFromTotal; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
