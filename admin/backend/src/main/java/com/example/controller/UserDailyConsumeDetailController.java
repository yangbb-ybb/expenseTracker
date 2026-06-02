package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.result.Result;
import com.example.entity.dto.consume.UserDailyConsumeDetailDTO;
import com.example.entity.dto.consume.UserDailyConsumeDetailQueryDTO;
import com.example.entity.po.UserDailyConsumeDetail;
import com.example.service.UserDailyConsumeDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 用户每日消费明细控制器
 *
 * 作用：处理消费明细相关的 HTTP 请求
 */
@Tag(name = "用户消费明细", description = "用户每日消费明细的增删改查")
@RestController
@RequestMapping("/userDailyConsumeDetail")
public class UserDailyConsumeDetailController {

    @Resource
    private UserDailyConsumeDetailService service;

    /**
     * 添加消费明细
     *
     * POST /userDailyConsumeDetail
     *
     * @param dto 消费明细数据
     * @return 消费明细记录
     */
    @Operation(summary = "添加消费明细", description = "添加一笔用户消费明细记录")
    @PostMapping
    public Result<UserDailyConsumeDetail> addConsumeDetail(
            HttpServletRequest request,
            @Valid @RequestBody UserDailyConsumeDetailDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录或 Token 无效");
        }
        dto.setUserId(userId);
        UserDailyConsumeDetail record = service.addConsumeDetail(dto);
        return Result.success(record);
    }

    /**
     * 删除消费明细（软删除）
     *
     * DELETE /userDailyConsumeDetail/{id}
     *
     * @param id 消费明细ID
     * @return 操作结果
     */
    @Operation(summary = "删除消费明细", description = "软删除一笔消费明细记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteConsumeDetail(@PathVariable Long id) {
        boolean success = service.deleteConsumeDetail(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("记录不存在或已被删除");
        }
    }

    /**
     * 修改消费明细
     *
     * PUT /userDailyConsumeDetail
     *
     * @param dto 消费明细数据
     * @return 更新后的消费明细记录
     */
    @Operation(summary = "修改消费明细", description = "修改一笔消费明细记录")
    @PutMapping
    public Result<UserDailyConsumeDetail> updateConsumeDetail(@Valid @RequestBody UserDailyConsumeDetailDTO dto) {
        UserDailyConsumeDetail record = service.updateConsumeDetail(dto);
        return Result.success(record);
    }

    /**
     * 根据ID查询消费明细
     *
     * GET /userDailyConsumeDetail/{id}
     *
     * @param id 消费明细ID
     * @return 消费明细记录
     */
    @Operation(summary = "查询消费明细详情", description = "根据ID查询消费明细记录")
    @GetMapping("/{id}")
    public Result<UserDailyConsumeDetail> getById(@PathVariable Long id) {
        UserDailyConsumeDetail record = service.getById(id);
        if (record == null) {
            return Result.error("记录不存在");
        }
        return Result.success(record);
    }

    /**
     * 查询消费明细列表（分页）
     *
     * GET /userDailyConsumeDetail/list
     *
     * @param request  HTTP 请求（用于获取 userId）
     * @param queryDTO 消费明细查询参数（日期可选）
     * @return 消费明细列表（分页）
     */
    @Operation(summary = "查询消费明细列表", description = "分页查询用户消费明细列表，支持按日期筛选，userId 从 Token 中自动获取")
    @GetMapping("/list")
    public Result<IPage<UserDailyConsumeDetail>> getList(
            HttpServletRequest request,
            UserDailyConsumeDetailQueryDTO queryDTO
    ) {
        // 从请求属性中获取 userId（由 JwtInterceptor 设置）
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录或 Token 无效");
        }

        IPage<UserDailyConsumeDetail> page = service.getListByUserAndDate(userId, queryDTO);
        return Result.success(page);
    }

    /**
     * 统计用户总支出和总收入
     *
     * GET /userDailyConsumeDetail/statistics
     *
     * @param request      HTTP 请求（用于获取 userId）
     * @param consumeDate 日期（格式：yyyy-MM 或 yyyy-MM-dd，可选）
     * @return 统计数据
     */
    @Operation(summary = "统计用户收支", description = "统计用户总支出和总收入，支持按日期筛选，userId 从 Token 中自动获取")
    @GetMapping("/statistics")
    public Result<com.example.entity.dto.consume.UserDailyConsumeDetailStatisticsDTO> getStatistics(
            HttpServletRequest request,
            @RequestParam(required = false) String consumeDate
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录或 Token 无效");
        }

        com.example.entity.dto.consume.UserDailyConsumeDetailStatisticsDTO statistics = service.getStatistics(userId, consumeDate);
        return Result.success(statistics);
    }
}
