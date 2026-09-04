package com.example.service;

import java.math.BigDecimal;

/**
 * 余额业务接口
 *
 * 作用：定义余额相关业务操作
 */
public interface BalanceService {

    /**
     * 充值
     *
     * @param userId     用户ID
     * @param amount     充值金额（必须为正数）
     * @param orderNo    业务订单号
     * @param description 描述
     * @return true-成功，false-失败
     */
    boolean recharge(Long userId, BigDecimal amount, String orderNo, String description);

    /**
     * 消费（扣款）
     *
     * @param userId     用户ID
     * @param amount     消费金额（必须为正数）
     * @param orderNo    业务订单号
     * @param description 描述
     * @return true-成功，false-失败
     */
    boolean consume(Long userId, BigDecimal amount, String orderNo, String description);

    /**
     * 退款
     *
     * @param userId     用户ID
     * @param amount     退款金额（必须为正数）
     * @param orderNo    业务订单号
     * @param description 描述
     * @return true-成功，false-失败
     */
    boolean refund(Long userId, BigDecimal amount, String orderNo, String description);

    /**
     * 冻结金额
     *
     * @param userId     用户ID
     * @param amount     冻结金额
     * @param orderNo    业务订单号
     * @param description 描述
     * @return true-成功，false-失败
     */
    boolean freeze(Long userId, BigDecimal amount, String orderNo, String description);

    /**
     * 解冻金额
     *
     * @param userId     用户ID
     * @param amount     解冻金额
     * @param orderNo    业务订单号
     * @param description 描述
     * @return true-成功，false-失败
     */
    boolean unfreeze(Long userId, BigDecimal amount, String orderNo, String description);

    /**
     * 查询用户余额
     *
     * @param userId     用户ID
     * @return 余额，如果没有记录返回 null
     */
    java.math.BigDecimal getBalance(Long userId);

    /**
     * 查询可用余额
     *
     * @param userId     用户ID
     * @return 可用余额，如果没有记录返回 null
     */
    java.math.BigDecimal getAvailableBalance(Long userId);
}
