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
-- 2. 交易流水表
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
-- 3. 添加外键约束（可选）
-- ----------------------------
ALTER TABLE `user_balance`
ADD CONSTRAINT `fk_user_balance_user_id`
FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE;

ALTER TABLE `transaction_record`
ADD CONSTRAINT `fk_transaction_record_user_id`
FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE;

-- =====================================================
--  使用说明
-- =====================================================
-- 1. 确保数据库中已有 sys_user 表
-- 2. 找到对应的数据库后，执行该脚本
-- 3. 外键约束是可选的，如果不需要可以删除第3部分
-- =====================================================
