package com.example.service;

import com.example.entity.vo.TabbarConfigVO;

/**
 * TabBar 配置业务接口
 *
 * 作用：按 appVersion + platform 拉取 TabBar 动态配置（含升级元信息）
 */
public interface TabbarConfigService {

    /**
     * 获取 TabBar 配置
     *
     * @param appVersion 客户端 app 版本（如 1.0.0），可空（空 = 不过滤版本范围）
     * @param platform   平台（h5 / weapp），可空（空 = 全部平台）
     * @return TabBar 配置（含升级元信息 + tab 列表）
     */
    TabbarConfigVO getConfig(String appVersion, String platform);
}
