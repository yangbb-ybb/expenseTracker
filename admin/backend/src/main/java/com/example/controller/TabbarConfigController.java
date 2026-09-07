package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.result.Result;
import com.example.entity.po.TabbarConfig;
import com.example.entity.vo.TabbarConfigVO;
import com.example.service.TabbarConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * TabBar 动态配置控制器
 *
 * 前端 DynamicTabBar 启动时调用此接口拉取配置（无需登录）
 *
 * 路径：GET /api/tabbar/config
 *   - appVersion  客户端 app 版本（可选，用于版本范围过滤）
 *   - platform    平台（可选，h5 / weapp）
 *
 * 鉴权：JwtInterceptor 对无 Token 直接放行，因此本接口天然匿名可调
 */
@Tag(name = "TabBar 配置", description = "App 启动时拉取 TabBar 动态配置")
@RestController
@RequestMapping("/tabbar")
public class TabbarConfigController {

    @Resource
    private TabbarConfigService tabbarConfigService;

    @Operation(summary = "获取 TabBar 配置", description = "按 appVersion + platform 下发 TabBar 配置 + 升级元信息")
    @GetMapping(value = "/config", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<TabbarConfigVO> getConfig(
            @RequestParam(required = false) String appVersion,
            @RequestParam(required = false) String platform
    ) {
        TabbarConfigVO vo = tabbarConfigService.getConfig(appVersion, platform);
        return Result.success(vo);
    }

    // ========== 管理后台 CRUD ==========

    /**
     * 管理后台分页列表（含已删除的）
     *
     * GET /tabbar/admin/page?isDeleted=&platform=&keyword=&page=&size=
     *   - isDeleted: 不传=全部, 0=未删, 1=已删
     *   - platform:  不传=全部平台
     *   - keyword:   page_path / text 模糊匹配
     *   - page:      0-based
     *   - size:      默认 20
     */
    @Operation(summary = "管理后台 TabBar 列表", description = "分页查询所有 tab 配置（含已删除），支持按 platform + 关键字过滤")
    @GetMapping(value = "/admin/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<IPage<TabbarConfig>> adminPage(
            @RequestParam(required = false) Integer isDeleted,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return Result.success(tabbarConfigService.listForAdmin(isDeleted, platform, keyword, page, size));
    }

    /**
     * 软删除
     */
    @Operation(summary = "软删除 TabBar 配置", description = "is_deleted 置 1，幂等；H5 接口会立即不再下发该 tab")
    @DeleteMapping(value = "/admin/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Void> softDelete(@PathVariable Long id) {
        boolean ok = tabbarConfigService.softDelete(id);
        return ok ? Result.success() : Result.error("记录不存在或已删除");
    }

    /**
     * 恢复软删除
     */
    @Operation(summary = "恢复已删除的 TabBar 配置", description = "is_deleted 置 0，幂等")
    @PutMapping(value = "/admin/{id}/restore", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Void> restore(@PathVariable Long id) {
        boolean ok = tabbarConfigService.restore(id);
        return ok ? Result.success() : Result.error("记录不存在或未被删除");
    }
}
