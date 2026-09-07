package com.example.service.impl;

import com.example.entity.po.TabbarConfig;
import com.example.entity.vo.TabItemVO;
import com.example.entity.vo.TabbarConfigVO;
import com.example.repository.TabbarConfigMapper;
import com.example.service.TabbarConfigService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TabBar 配置业务实现
 *
 * 数据流转：
 *   1) Mapper 按平台粗筛 + 未删除 + 按 order_num 升序查出全部行
 *   2) Service 在内存里做版本范围过滤 + 拆分「__GLOBAL__ 元信息行」与「tab 项」
 *   3) 组装成前端 TabbarConfigResponse 结构返回
 *
 * 注：tab 数量很小（个位到几十），内存里过滤毫无压力；
 *     版本比较实现放在 VersionComparator 工具里，方便单测。
 */
@Service
public class TabbarConfigServiceImpl implements TabbarConfigService {

    @Resource
    private TabbarConfigMapper tabbarConfigMapper;

    @Override
    public TabbarConfigVO getConfig(String appVersion, String platform) {
        // 平台为空时传 null，让 SQL 里 platform IS NULL 分支命中「全部平台」
        String platformParam = (platform == null || platform.trim().isEmpty()) ? null : platform.trim();

        List<TabbarConfig> all = tabbarConfigMapper.selectActiveConfigs(platformParam);

        TabbarConfigVO vo = new TabbarConfigVO();
        List<TabItemVO> tabs = new ArrayList<>();

        for (TabbarConfig cfg : all) {
            // 拆分元信息行
            if (TabbarConfig.GLOBAL_PAGE_PATH.equals(cfg.getPagePath())) {
                vo.setMinVersion(cfg.getMinVersion());
                vo.setForceUpdate(cfg.getForceUpdate() != null && cfg.getForceUpdate() == 1);
                vo.setUpgradeTip(cfg.getUpgradeTip());
                vo.setUpgradeUrl(cfg.getUpgradeUrl());
                continue;
            }

            // 版本范围过滤
            if (!isVersionInRange(appVersion, cfg.getMinAppVersion(), cfg.getMaxAppVersion())) {
                continue;
            }

            // visible 过滤（0 = 隐藏）
            if (cfg.getVisible() != null && cfg.getVisible() == 0) {
                continue;
            }

            tabs.add(toItemVO(cfg));
        }

        vo.setTabs(tabs);
        return vo;
    }

    /**
     * 判断 appVersion 是否落在 [min, max] 闭区间内
     * 任一端为 null/空 表示不限
     */
    private boolean isVersionInRange(String appVersion, String min, String max) {
        if ((min == null || min.isEmpty()) && (max == null || max.isEmpty())) {
            return true;
        }
        if (appVersion == null || appVersion.isEmpty()) {
            // 没传 appVersion 时，不强制过滤（按需可改成 false 严格匹配）
            return true;
        }
        if (min != null && !min.isEmpty() && VersionComparator.compare(appVersion, min) < 0) {
            return false;
        }
        if (max != null && !max.isEmpty() && VersionComparator.compare(appVersion, max) > 0) {
            return false;
        }
        return true;
    }

    private TabItemVO toItemVO(TabbarConfig cfg) {
        TabItemVO vo = new TabItemVO();
        vo.setPagePath(cfg.getPagePath());
        vo.setText(cfg.getText());
        vo.setIcon(cfg.getIcon());
        vo.setIconActive(cfg.getIconActive());
        vo.setOrder(cfg.getOrderNum());
        vo.setVisible(cfg.getVisible() == null || cfg.getVisible() != 0);
        vo.setStatus(cfg.getStatus());
        return vo;
    }

    // ========== 管理后台 ==========

    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<TabbarConfig> listForAdmin(Integer isDeleted, String platform, String keyword, int page, int size) {
        String platformParam = (platform == null || platform.trim().isEmpty()) ? null : platform.trim();
        List<TabbarConfig> all = tabbarConfigMapper.selectAdminList(isDeleted, platformParam, keyword);

        // 内存分页（tab 数量小，简单点）
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<TabbarConfig> p =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page + 1, size);
        p.setTotal(all.size());
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        p.setRecords(new ArrayList<>(all.subList(from, to)));
        return p;
    }

    @Override
    public boolean softDelete(Long id) {
        if (id == null) return false;
        return tabbarConfigMapper.softDelete(id) > 0;
    }

    @Override
    public boolean restore(Long id) {
        if (id == null) return false;
        return tabbarConfigMapper.restore(id) > 0;
    }
}
