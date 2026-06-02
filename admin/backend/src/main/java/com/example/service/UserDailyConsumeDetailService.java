package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.dto.consume.UserDailyConsumeDetailQueryDTO;
import com.example.entity.po.UserDailyConsumeDetail;

/**
 * 用户每日消费明细业务接口
 *
 * 作用：定义消费明细相关业务操作
 */
public interface UserDailyConsumeDetailService {

    /**
     * 添加消费明细
     *
     * @param dto 消费明细数据
     * @return 创建成功的记录
     */
    UserDailyConsumeDetail addConsumeDetail(com.example.entity.dto.consume.UserDailyConsumeDetailDTO dto);

    /**
     * 删除消费明细（软删除）
     *
     * @param id 消费明细ID
     * @return true-删除成功，false-记录不存在
     */
    boolean deleteConsumeDetail(Long id);

    /**
     * 修改消费明细
     *
     * @param dto 消费明细数据
     * @return 更新成功的记录
     */
    UserDailyConsumeDetail updateConsumeDetail(com.example.entity.dto.consume.UserDailyConsumeDetailDTO dto);

    /**
     * 根据ID查询消费明细
     *
     * @param id 消费明细ID
     * @return 消费明细记录，不存在返回 null
     */
    UserDailyConsumeDetail getById(Long id);

    /**
     * 根据用户和日期查询消费明细列表（不包含已删除的，分页）
     *
     * @param userId     用户ID
     * @param queryDTO   查询参数
     * @return 消费明细列表（分页）
     */
    IPage<UserDailyConsumeDetail> getListByUserAndDate(Long userId, UserDailyConsumeDetailQueryDTO queryDTO);

    /**
     * 统计用户总支出和总收入
     *
     * @param userId 用户ID
     * @return 统计数据
     */
    com.example.entity.dto.consume.UserDailyConsumeDetailStatisticsDTO getStatistics(Long userId);
}
