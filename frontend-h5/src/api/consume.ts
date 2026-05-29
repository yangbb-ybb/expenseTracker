import http from '@/utils/request'

// 消费明细接口
export const consumeApi = {
  /**
   * 查询消费明细列表（分页）
   * @param params - page: 页码, size: 每页数量
   */
  getList: (params?: { page?: number; size?: number; date?: string }) =>
    http.get('/userDailyConsumeDetail/list', { params }),

  /**
   * 添加消费明细
   * @param data - 消费明细数据
   */
  create: (data: any) => http.post('/userDailyConsumeDetail', data),

  /**
   * 删除消费明细
   * @param id - 消费明细ID
   */
  delete: (id: number) => http.delete(`/userDailyConsumeDetail/${id}`),

  /**
   * 获取消费明细详情
   * @param id - 消费明细ID
   */
  getDetail: (id: number) => http.get(`/userDailyConsumeDetail/${id}`),

  /**
   * 更新消费明细
   * @param data - 消费明细数据
   */
  update: (data: any) => http.put('/userDailyConsumeDetail', data)
}

export default consumeApi
