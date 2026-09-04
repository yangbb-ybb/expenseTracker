package com.example.entity.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * TabBar 动态配置响应
 *
 * 对应前端 TabbarConfigResponse 接口
 */
public class TabbarConfigVO {

    /** 最低支持版本（用于前端做软提示） */
    private String minVersion;

    /** 是否触发强制升级 */
    private Boolean forceUpdate;

    /** 升级提示文案 */
    private String upgradeTip;

    /** 升级跳转地址 */
    private String upgradeUrl;

    /** tab 列表 */
    private List<TabItemVO> tabs = new ArrayList<>();

    public String getMinVersion() { return minVersion; }
    public void setMinVersion(String minVersion) { this.minVersion = minVersion; }

    public Boolean getForceUpdate() { return forceUpdate; }
    public void setForceUpdate(Boolean forceUpdate) { this.forceUpdate = forceUpdate; }

    public String getUpgradeTip() { return upgradeTip; }
    public void setUpgradeTip(String upgradeTip) { this.upgradeTip = upgradeTip; }

    public String getUpgradeUrl() { return upgradeUrl; }
    public void setUpgradeUrl(String upgradeUrl) { this.upgradeUrl = upgradeUrl; }

    public List<TabItemVO> getTabs() { return tabs; }
    public void setTabs(List<TabItemVO> tabs) { this.tabs = tabs; }
}
