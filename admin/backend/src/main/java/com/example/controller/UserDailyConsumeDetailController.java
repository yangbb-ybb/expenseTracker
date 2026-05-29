package com.example.controller;

import com.example.common.result.Result;
import com.example.entity.dto.UserDailyConsumeDetailDTO;
import com.example.entity.po.UserDailyConsumeDetail;
import com.example.service.UserDailyConsumeDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户每日消费明细控制器
 *
 * 作用：处理消费明细相关的 HTTP 请求
 */
@Tag(name = "用户消费明细", description = "用户每日消费明细的增删改查")
@RestController
@RequestMapping("/daily-consume")
public class UserDailyConsumeDetailController {

    @Resource
    private UserDailyConsumeDetailService service;

    /**
     * 添加消费明细
     *
     * POST /daily-consume
     *
     * @param dto 消费明细数据
     * @return 消费明细记录
     */
    @Operation(summary = "添加消费明细", description = "添加一笔用户消费明细记录，如果相同用户+日期+类型+分类已存在，则累加金额")
    @PostMapping
    public Result<UserDailyConsumeDetail> addConsumeDetail(@Valid @RequestBody UserDailyConsumeDetailDTO dto) {
        UserDailyConsumeDetail record = service.addConsumeDetail(dto);
        return Result.success(record);
    }

    /**
     * 删除消费明细（软删除）
     *
     * DELETE /daily-consume/{id}
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
     * PUT /daily-consume
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
     * GET /daily-consume/{id}
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
     * 根据用户和日期查询消费明细列表
     *
     * GET /daily-consume/list
     *
     * @param userId     用户ID
     * @param consumeDate 消费日期（格式：yyyy-MM-dd）
     * @return 消费明细列表
     */
    @Operation(summary = "查询消费明细列表", description = "根据用户和日期查询消费明细列表")
    @GetMapping("/list")
    public Result<List<UserDailyConsumeDetail>> getList(
            @RequestParam Long userId,
            @RequestParam String consumeDate
    ) {
        List<UserDailyConsumeDetail> list = service.getListByUserAndDate(userId, consumeDate);
        return Result.success(list);
    }
}
