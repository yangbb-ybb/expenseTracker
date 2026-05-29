-- =====================================================
--  用户余额系统建表脚本
-- =====================================================

-- ----------------------------
-- 1. 用户余额表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_balance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，余额记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，关联 sys_user 表',
  `balance` DECIMAL(20,2) NOT NULL DEFAULT 0.00 COMMENT '当前余额',
  `available_balance` DECIMAL(20,2) NOT NULL DEFAULT 0.00 COMMENT '可用余额（未冻结部分）',
  `frozen_balance` DECIMAL(20,2) NOT NULL DEFAULT 0.00 COMMENT '冻结余额',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户余额表';

-- ----------------------------
-- 2. 用户每日消费表(个人现金无关)
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_daily_consume_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，每日消费记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，关联 sys_user 表',
  `consume_date` DATE NOT NULL COMMENT '消费日期',
  `consume_amount` DECIMAL(20,2) NOT NULL DEFAULT 0.00 COMMENT '当日消费金额',
  `consume_type` VARCHAR(32) DEFAULT NULL COMMENT '消费类型/渠道：alipay=支付宝，wechat=微信，unionpay=银联，cash=现金等',
  `consume_category` VARCHAR(32) DEFAULT NULL COMMENT '消费分类：entertainment=娱乐，shopping=购物，refund=退款，daily_necessities=日常开销，dining=餐饮，transport=交通，education=教育，medical=医疗，insurance=保险，other=其他，shop_out=购物支出',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '备注/消费描述',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0=否，1=是',
  `is_excluded_from_total` TINYINT DEFAULT 0 COMMENT '是否计入总计：0=计入，1=仅展示不计入',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date_type_category` (`user_id`, `consume_date`, `consume_type`, `consume_category`),
  KEY `idx_consume_date` (`consume_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_consume_type` (`consume_type`),
  KEY `idx_consume_category` (`consume_category`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户每日消费明细表';

-- ----------------------------
-- 3. 交易流水表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `transaction_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，流水ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，关联 sys_user 表',
  `type` TINYINT NOT NULL COMMENT '交易类型：1=充值，2=消费，3=退款，4=提现，5=转账',
  `transaction_no` VARCHAR(64) NOT NULL COMMENT '流水号（唯一，用于防止重复提交）',
  `amount` DECIMAL(20,2) NOT NULL DEFAULT 0.00 COMMENT '变动金额（正数表示收入，负数表示支出）',
  `balance_after` DECIMAL(20,2) NOT NULL DEFAULT 0.00 COMMENT '变动后余额',
  `order_no` VARCHAR(64) DEFAULT NULL COMMENT '关联业务订单号（如：商品订单号、充值订单号等）',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '交易描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '交易状态：1=成功，2=失败，3=处理中',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transaction_no` (`transaction_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易流水表';

-- ----------------------------
-- 4. 添加外键约束（可选）
-- ----------------------------
ALTER TABLE `user_balance`
ADD CONSTRAINT `fk_user_balance_user_id`
FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE;

ALTER TABLE `transaction_record`
ADD CONSTRAINT `fk_transaction_record_user_id`
FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE;

ALTER TABLE `user_daily_consume_detail`
ADD CONSTRAINT `fk_daily_consume_user_id`
FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE;

-- =====================================================
--  使用说明
-- =====================================================
-- 1. 确保数据库中已有 sys_user 表
-- 2. 找到对应的数据库后，执行该脚本
-- 3. 外键约束是可选的，如果不需要可以删除第3部分
-- =====================================================
