package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.po.TabbarConfig;
import com.example.entity.vo.TabbarConfigVO;

/**
 * TabBar 配置业务接口
 */
public interface TabbarConfigService {

    /**
     * 前端 H5 拉取 TabBar 配置（已过滤 is_deleted=1 的项）
     */
    TabbarConfigVO getConfig(String appVersion, String platform);

    /**
     * 管理后台分页列表（含已删除的）
     *
     * @param isDeleted null=全部, 0=未删, 1=已删
     * @param platform  null=全部平台
     * @param keyword   page_path / text 模糊匹配（可选）
     */
    IPage<TabbarConfig> listForAdmin(Integer isDeleted, String platform, String keyword, int page, int size);

    /**
     * 软删除
     * @return true=成功（幂等：重复删返回 false）
     */
    boolean softDelete(Long id);

    /**
     * 恢复软删除
     */
    boolean restore(Long id);
}
