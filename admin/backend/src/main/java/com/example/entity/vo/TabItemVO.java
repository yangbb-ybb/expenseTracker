package com.example.entity.vo;

/**
 * 单个 Tab 配置项
 *
 * 对应前端 TabConfig 接口：
 *   - pagePath  白名单内的页面路径
 *   - status    active / deprecated / offline
 */
public class TabItemVO {

    /** 页面路径（前端白名单内的值） */
    private String pagePath;

    /** tab 显示文字 */
    private String text;

    /** 未选中图标 URL */
    private String icon;

    /** 选中图标 URL */
    private String iconActive;

    /** 排序号，升序 */
    private Integer order;

    /** 是否可见 */
    private Boolean visible;

    /** active=正常 / deprecated=即将下线 / offline=已下线 */
    private String status;

    public String getPagePath() { return pagePath; }
    public void setPagePath(String pagePath) { this.pagePath = pagePath; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getIconActive() { return iconActive; }
    public void setIconActive(String iconActive) { this.iconActive = iconActive; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
