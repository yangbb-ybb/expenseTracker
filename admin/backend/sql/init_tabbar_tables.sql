-- TabBar 动态配置表
--
-- 设计说明：
--   - 一条 page_path = '__GLOBAL__' 的记录用于下发「升级元信息」
--     （minVersion / forceUpdate / upgradeTip / upgradeUrl），不在前端 tabs 列表里渲染
--   - 其余记录每行 = 一个 tab 项
--   - appVersion 范围字段（min_app_version / max_app_version）支持语义化版本号（如 1.0.0 / 1.2.3）
--     NULL 表示不限，闭区间
--   - platform 字段精确匹配（h5 / weapp / mp 等），不传则匹配所有
--
-- 新增 tab 页步骤：
--   1) 在 frontend-h5 的 src/config/tabPages.ts 加白名单
--   2) 在本表加一行配置（pagePath = 白名单里的某个值）
USE backend;

CREATE TABLE IF NOT EXISTS `tabbar_config` (
    `id`                 BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `platform`           VARCHAR(20)  DEFAULT NULL COMMENT '目标平台：h5 / weapp；NULL = 全部平台',
    `min_app_version`    VARCHAR(20)  DEFAULT NULL COMMENT '生效的最低 app 版本（含），NULL = 不限',
    `max_app_version`    VARCHAR(20)  DEFAULT NULL COMMENT '生效的最高 app 版本（含），NULL = 不限',
    `page_path`          VARCHAR(100) NOT NULL COMMENT '页面路径，__GLOBAL__ 表示全局升级元信息行',
    `text`               VARCHAR(50)  DEFAULT NULL COMMENT 'tab 文字',
    `icon`               VARCHAR(255) DEFAULT NULL COMMENT '未选中图标 URL',
    `icon_active`        VARCHAR(255) DEFAULT NULL COMMENT '选中图标 URL',
    `order_num`          INT          DEFAULT 0    COMMENT '排序号，升序',
    `visible`            TINYINT      DEFAULT 1    COMMENT '是否可见 0:隐藏 1:可见',
    `status`             VARCHAR(20)  DEFAULT 'active' COMMENT 'active=正常 / deprecated=即将下线 / offline=已下线',
    `force_update`       TINYINT      DEFAULT 0    COMMENT '__GLOBAL__ 行专用：是否强制升级 0:否 1:是',
    `upgrade_tip`        VARCHAR(255) DEFAULT NULL COMMENT '__GLOBAL__ 行专用：升级提示文案',
    `upgrade_url`        VARCHAR(255) DEFAULT NULL COMMENT '__GLOBAL__ 行专用：升级跳转地址',
    `min_version`        VARCHAR(20)  DEFAULT NULL COMMENT '__GLOBAL__ 行专用：最低支持版本提示',
    `is_deleted`         TINYINT      DEFAULT 0    COMMENT '逻辑删除 0:未删除 1:已删除',
    `create_time`        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_platform_page` (`platform`, `page_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='TabBar 动态配置表';

-- 初始化：全局升级元信息行（page_path = __GLOBAL__）
INSERT INTO `tabbar_config`
    (`platform`, `page_path`, `order_num`, `visible`, `status`, `force_update`, `upgrade_tip`, `upgrade_url`, `min_version`)
VALUES
    (NULL, '__GLOBAL__', 0, 1, 'active', 0, '请升级到最新版本以获得完整体验', '', '');

-- 初始化：首页 tab
INSERT INTO `tabbar_config`
    (`platform`, `min_app_version`, `max_app_version`, `page_path`, `text`, `icon`, `icon_active`, `order_num`, `visible`, `status`)
VALUES
    ('h5', NULL, NULL, 'pages/index/index', '首页',
     'https://img.ixintu.com/upload/jpg/20210522/3bf6c0fb39bde9b5e8c5b9b5e8b5e8b5.jpg',
     'https://img.ixintu.com/upload/jpg/20210522/3bf6c0fb39bde9b5e8c5b9b5e8b5e8b5.jpg',
     1, 1, 'active');

-- 初始化：分析 tab
INSERT INTO `tabbar_config`
    (`platform`, `min_app_version`, `max_app_version`, `page_path`, `text`, `icon`, `icon_active`, `order_num`, `visible`, `status`)
VALUES
    ('h5', NULL, NULL, 'pages/dataAnalyse/index', '分析',
     'https://img.ixintu.com/upload/jpg/20210522/3bf6c0fb39bde9b5e8c5b9b5e8b5e8b5.jpg',
     'https://img.ixintu.com/upload/jpg/20210522/3bf6c0fb39bde9b5e8c5b9b5e8b5e8b5.jpg',
     2, 1, 'active');
