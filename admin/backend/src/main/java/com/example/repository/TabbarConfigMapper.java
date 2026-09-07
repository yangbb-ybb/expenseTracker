package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.po.TabbarConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TabBar 配置 Mapper
 *
 * 继承 BaseMapper 拿到通用 CRUD；
 * 自定义 selectActiveConfigs 只做「平台 + 未删除」的粗筛，
 * 版本号范围（min/maxAppVersion）在 Service 层做语义化版本比较后过滤。
 */
@Mapper
public interface TabbarConfigMapper extends BaseMapper<TabbarConfig> {

    /**
     * 按平台拉取所有未删除的 tabbar 配置（含 __GLOBAL__ 行）
     * platform = NULL 时匹配所有平台
     *
     * @param platform 平台标识（h5 / weapp），传 null 查全部
     * @return 配置列表（已按 order_num 升序）
     */
    List<TabbarConfig> selectActiveConfigs(@Param("platform") String platform);

    /**
     * 管理后台分页查询（含已删除的）
     *
     * @param isDeleted null=全部, 0=未删除, 1=已删除
     * @param platform  null=全部平台
     * @param keyword   page_path / text 模糊匹配（可选）
     * @return 配置列表（按 order_num 升序）
     */
    List<TabbarConfig> selectAdminList(@Param("isDeleted") Integer isDeleted,
                                       @Param("platform") String platform,
                                       @Param("keyword") String keyword);

    /**
     * 软删除：is_deleted 置 1（幂等）
     * @return 影响行数
     */
    int softDelete(@Param("id") Long id);

    /**
     * 恢复软删除：is_deleted 置 0
     * @return 影响行数
     */
    int restore(@Param("id") Long id);
}
