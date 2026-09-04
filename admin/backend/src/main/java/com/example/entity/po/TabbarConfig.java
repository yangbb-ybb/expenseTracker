package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * TabBar 动态配置实体
 *
 * 对应数据库表：tabbar_config
 *
 * 说明：
 *   - page_path = '__GLOBAL__' 的行承载全局升级元信息（minVersion/forceUpdate/upgradeTip/upgradeUrl），
 *     不会出现在前端 tabs 列表里
 *   - 其余行每行 = 一个 tab 项
 */
@TableName("tabbar_config")
public class TabbarConfig {

    /** 主键ID */
    private Long id;

    /** 目标平台：h5 / weapp；NULL = 全部平台 */
    private String platform;

    /** 生效的最低 app 版本（含），NULL = 不限 */
    private String minAppVersion;

    /** 生效的最高 app 版本（含），NULL = 不限 */
    private String maxAppVersion;

    /** 页面路径，__GLOBAL__ 表示全局升级元信息行 */
    private String pagePath;

    /** tab 文字 */
    private String text;

    /** 未选中图标 URL */
    private String icon;

    /** 选中图标 URL */
    private String iconActive;

    /** 排序号，升序 */
    private Integer orderNum;

    /** 是否可见 0:隐藏 1:可见 */
    private Integer visible;

    /** active=正常 / deprecated=即将下线 / offline=已下线 */
    private String status;

    /** __GLOBAL__ 行专用：是否强制升级 0:否 1:是 */
    private Integer forceUpdate;

    /** __GLOBAL__ 行专用：升级提示文案 */
    private String upgradeTip;

    /** __GLOBAL__ 行专用：升级跳转地址 */
    private String upgradeUrl;

    /** __GLOBAL__ 行专用：最低支持版本提示 */
    private String minVersion;

    /** 逻辑删除 0:未删除 1:已删除 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    // ========== 常量：全局元信息行的 pagePath ==========

    /** 全局升级元信息行的 pagePath 哨兵值 */
    public static final String GLOBAL_PAGE_PATH = "__GLOBAL__";

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getMinAppVersion() { return minAppVersion; }
    public void setMinAppVersion(String minAppVersion) { this.minAppVersion = minAppVersion; }

    public String getMaxAppVersion() { return maxAppVersion; }
    public void setMaxAppVersion(String maxAppVersion) { this.maxAppVersion = maxAppVersion; }

    public String getPagePath() { return pagePath; }
    public void setPagePath(String pagePath) { this.pagePath = pagePath; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getIconActive() { return iconActive; }
    public void setIconActive(String iconActive) { this.iconActive = iconActive; }

    public Integer getOrderNum() { return orderNum; }
    public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }

    public Integer getVisible() { return visible; }
    public void setVisible(Integer visible) { this.visible = visible; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getForceUpdate() { return forceUpdate; }
    public void setForceUpdate(Integer forceUpdate) { this.forceUpdate = forceUpdate; }

    public String getUpgradeTip() { return upgradeTip; }
    public void setUpgradeTip(String upgradeTip) { this.upgradeTip = upgradeTip; }

    public String getUpgradeUrl() { return upgradeUrl; }
    public void setUpgradeUrl(String upgradeUrl) { this.upgradeUrl = upgradeUrl; }

    public String getMinVersion() { return minVersion; }
    public void setMinVersion(String minVersion) { this.minVersion = minVersion; }

    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
