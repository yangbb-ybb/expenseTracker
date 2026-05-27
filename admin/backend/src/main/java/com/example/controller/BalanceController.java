package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.entity.dto.balance.*;
import com.example.entity.po.TransactionRecord;
import com.example.entity.vo.BalanceStatisticsVO;
import com.example.entity.vo.BalanceVO;
import com.example.entity.vo.TransactionRecordVO;
import com.example.repository.TransactionRecordRepository;
import com.example.repository.UserBalanceRepository;
import com.example.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 余额管理控制器
 *
 * 作用：处理余额相关的 HTTP 请求
 */
@Tag(name = "余额管理", description = "用户余额充值、消费、退款、冻结、解冻等操作")
@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Resource
    private BalanceService balanceService;

    @Resource
    private TransactionRecordRepository transactionRecordRepository;

    @Resource
    private UserBalanceRepository userBalanceRepository;

    /**
     * 查询用户余额
     *
     * GET /balance
     *
     * @param userId     用户ID（作为查询参数传递）
     * @return           余额信息
     */
    @Operation(summary = "查询用户余额", description = "查询指定用户的当前余额和可用余额")
    @GetMapping
    public Result<BalanceVO> getBalance(@RequestParam Long userId) {
        BigDecimal balance = balanceService.getBalance(userId);
        BigDecimal availableBalance = balanceService.getAvailableBalance(userId);

        BalanceVO vo = new BalanceVO();
        vo.setUserId(userId);
        vo.setBalance(balance != null ? balance : BigDecimal.ZERO);
        vo.setAvailableBalance(availableBalance != null ? availableBalance : BigDecimal.ZERO);

        return Result.success(vo);
    }

    /**
     * 充值
     *
     * POST /balance/recharge
     *
     * @param dto        充值参数
     * @return           操作结果
     */
    @Operation(summary = "充值", description = "为用户账户充值，增加余额")
    @PostMapping("/recharge")
    public Result<Void> recharge(@Valid @RequestBody BalanceRechargeDTO dto) {
        boolean success = balanceService.recharge(
                dto.getUserId(),
                dto.getAmount(),
                dto.getOrderNo(),
                dto.getDescription()
        );

        if (success) {
            return Result.success();
        } else {
            return Result.error("充值失败，金额必须大于0");
        }
    }

    /**
     * 消费（扣款）
     *
     * POST /balance/consume
     *
     * @param dto        消费参数
     * @return           操作结果
     */
    @Operation(summary = "消费", description = "从用户账户扣除余额，用于购买商品或服务")
    @PostMapping("/consume")
    public Result<Void> consume(@Valid @RequestBody BalanceConsumeDTO dto) {
        boolean success = balanceService.consume(
                dto.getUserId(),
                dto.getAmount(),
                dto.getOrderNo(),
                dto.getDescription()
        );

        if (success) {
            return Result.success();
        } else {
            return Result.error("消费失败，余额不足或金额不正确");
        }
    }

    /**
     * 退款
     *
     * POST /balance/refund
     *
     * @param dto        退款参数
     * @return           操作结果
     */
    @Operation(summary = "退款", description = "将金额退还到用户账户")
    @PostMapping("/refund")
    public Result<Void> refund(@Valid @RequestBody BalanceRefundDTO dto) {
        boolean success = balanceService.refund(
                dto.getUserId(),
                dto.getAmount(),
                dto.getOrderNo(),
                dto.getDescription()
        );

        if (success) {
            return Result.success();
        } else {
            return Result.error("退款失败，金额必须大于0");
        }
    }

    /**
     * 冻结金额
     *
     * POST /balance/freeze
     *
     * @param dto        冻结参数
     * @return           操作结果
     */
    @Operation(summary = "冻结金额", description = "冻结用户账户中的部分金额，冻结后不可使用")
    @PostMapping("/freeze")
    public Result<Void> freeze(@Valid @RequestBody BalanceFreezeDTO dto) {
        boolean success = balanceService.freeze(
                dto.getUserId(),
                dto.getAmount(),
                dto.getOrderNo(),
                dto.getDescription()
        );

        if (success) {
            return Result.success();
        } else {
            return Result.error("冻结失败，可用余额不足或金额不正确");
        }
    }

    /**
     * 解冻金额
     *
     * POST /balance/unfreeze
     *
     * @param dto        解冻参数
     * @return           操作结果
     */
    @Operation(summary = "解冻金额", description = "解冻用户账户中已冻结的金额，解冻后可正常使用")
    @PostMapping("/unfreeze")
    public Result<Void> unfreeze(@Valid @RequestBody BalanceUnfreezeDTO dto) {
        boolean success = balanceService.unfreeze(
                dto.getUserId(),
                dto.getAmount(),
                dto.getOrderNo(),
                dto.getDescription()
        );

        if (success) {
            return Result.success();
        } else {
            return Result.error("解冻失败，冻结余额不足或金额不正确");
        }
    }

    /**
     * 查询交易流水（分页）
     *
     * GET /balance/records
     *
     * @param dto        流水查询参数
     * @return           流水记录列表（分页）
     */
    @Operation(summary = "查询交易流水", description = "分页查询用户的交易流水，支持按类型、时间范围筛选")
    @GetMapping("/records")
    public Result<IPage<TransactionRecordVO>> getTransactionRecords(TransactionRecordQueryDTO dto) {
        // 构建查询条件
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TransactionRecord> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        wrapper.eq(TransactionRecord::getUserId, dto.getUserId());

        // 按类型筛选
        if (dto.getType() != null) {
            wrapper.eq(TransactionRecord::getType, dto.getType());
        }

        // 按时间范围筛选
        if (dto.getStartTime() != null && !dto.getStartTime().isEmpty()) {
            try {
                LocalDateTime start = LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                wrapper.ge(TransactionRecord::getCreateTime, start);
            } catch (Exception e) {
                return Result.error("开始时间格式错误，应为：yyyy-MM-dd HH:mm:ss");
            }
        }

        if (dto.getEndTime() != null && !dto.getEndTime().isEmpty()) {
            try {
                LocalDateTime end = LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                wrapper.le(TransactionRecord::getCreateTime, end);
            } catch (Exception e) {
                return Result.error("结束时间格式错误，应为：yyyy-MM-dd HH:mm:ss");
            }
        }

        // 排序
        wrapper.orderByDesc(TransactionRecord::getCreateTime);

        // 分页查询
        Page<TransactionRecord> page = new Page<>(dto.getPage(), dto.getSize());
        IPage<TransactionRecord> recordPage = transactionRecordRepository.selectPage(page, wrapper);

        // 转换为 VO
        IPage<TransactionRecordVO> voPage = recordPage.convert(record -> {
            TransactionRecordVO vo = new TransactionRecordVO();
            vo.setId(record.getId());
            vo.setUserId(record.getUserId());
            vo.setType(record.getType());
            vo.setTypeName(getTypeName(record.getType()));
            vo.setTransactionNo(record.getTransactionNo());
            vo.setAmount(record.getAmount());
            vo.setBalanceAfter(record.getBalanceAfter());
            vo.setOrderNo(record.getOrderNo());
            vo.setDescription(record.getDescription());
            vo.setStatus(record.getStatus());
            vo.setStatusName(getStatusName(record.getStatus()));
            vo.setCreateTime(record.getCreateTime());
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 获取交易类型名称
     */
    private String getTypeName(Integer type) {
        if (type == null) return "未知";
        return switch (type) {
            case 1 -> "充值";
            case 2 -> "消费";
            case 3 -> "退款";
            case 4 -> "提现";
            case 5 -> "冻结/解冻";
            default -> "未知";
        };
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 1 -> "成功";
            case 2 -> "失败";
            case 3 -> "处理中";
            default -> "未知";
        };
    }

    /**
     * 查询余额统计信息
     *
     * GET /balance/statistics
     *
     * @return           统计信息
     */
    @Operation(summary = "查询余额统计", description = "查询系统整体余额统计信息，包括总余额、今日消费、本月充值等")
    @GetMapping("/statistics")
    public Result<BalanceStatisticsVO> getBalanceStatistics() {
        BalanceStatisticsVO vo = new BalanceStatisticsVO();

        // 统计用户总数
        Long totalUsers = userBalanceRepository.selectCount(null);
        vo.setTotalUsers(totalUsers);

        // 统计总余额
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.entity.po.UserBalance> balanceWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        java.util.List<com.example.entity.po.UserBalance> balances = userBalanceRepository.selectList(balanceWrapper);

        BigDecimal totalBalance = BigDecimal.ZERO;
        BigDecimal totalAvailableBalance = BigDecimal.ZERO;
        BigDecimal totalFrozenBalance = BigDecimal.ZERO;

        for (com.example.entity.po.UserBalance balance : balances) {
            totalBalance = totalBalance.add(balance.getBalance() != null ? balance.getBalance() : BigDecimal.ZERO);
            totalAvailableBalance = totalAvailableBalance.add(balance.getAvailableBalance() != null ? balance.getAvailableBalance() : BigDecimal.ZERO);
            totalFrozenBalance = totalFrozenBalance.add(balance.getFrozenBalance() != null ? balance.getFrozenBalance() : BigDecimal.ZERO);
        }

        vo.setTotalBalance(totalBalance);
        vo.setTotalAvailableBalance(totalAvailableBalance);
        vo.setTotalFrozenBalance(totalFrozenBalance);

        // 今日充值和消费
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        BigDecimal todayRecharge = getTransactionAmountByTypeAndTime(1, todayStart, todayEnd); // 1=充值
        BigDecimal todayConsume = getTransactionAmountByTypeAndTime(2, todayStart, todayEnd).negate(); // 2=消费，取绝对值

        vo.setTodayRechargeAmount(todayRecharge);
        vo.setTodayConsumeAmount(todayConsume);

        // 本月充值和消费
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = monthStart.plusMonths(1);

        BigDecimal monthRecharge = getTransactionAmountByTypeAndTime(1, monthStart, monthEnd);
        BigDecimal monthConsume = getTransactionAmountByTypeAndTime(2, monthStart, monthEnd).negate();

        vo.setMonthRechargeAmount(monthRecharge);
        vo.setMonthConsumeAmount(monthConsume);

        return Result.success(vo);
    }

    /**
     * 根据类型和时间范围统计交易金额
     */
    private BigDecimal getTransactionAmountByTypeAndTime(Integer type, LocalDateTime startTime, LocalDateTime endTime) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TransactionRecord> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(TransactionRecord::getType, type)
               .eq(TransactionRecord::getStatus, 1)  // 只统计成功的交易
               .ge(TransactionRecord::getCreateTime, startTime)
               .lt(TransactionRecord::getCreateTime, endTime);

        java.util.List<TransactionRecord> records = transactionRecordRepository.selectList(wrapper);

        BigDecimal total = BigDecimal.ZERO;
        for (TransactionRecord record : records) {
            total = total.add(record.getAmount() != null ? record.getAmount() : BigDecimal.ZERO);
        }
        return total;
    }
}
