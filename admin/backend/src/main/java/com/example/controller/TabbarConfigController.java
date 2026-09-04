package com.example.controller;

import com.example.common.result.Result;
import com.example.entity.vo.TabbarConfigVO;
import com.example.service.TabbarConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/config")
    public Result<TabbarConfigVO> getConfig(
            @RequestParam(required = false) String appVersion,
            @RequestParam(required = false) String platform
    ) {
        TabbarConfigVO vo = tabbarConfigService.getConfig(appVersion, platform);
        return Result.success(vo);
    }
}
