import http from '@/utils/request'

// 用户相关
export const userApi = {
  login: (data: { username: string; password: string }) => http.post('/login', data),
  logout: () => http.post('/logout'),
  getUserInfo: () => http.get('/user/info'),
  updateUserInfo: (data: any) => http.put('/user/info', data)
}

// 示例接口
export const demoApi = {
  getList: (params?: { page?: number; pageSize?: number }) =>
    http.get('/demo/list', { params }),
  getDetail: (id: string) => http.get(`/demo/${id}`),
  create: (data: any) => http.post('/demo', data),
  update: (id: string, data: any) => http.put(`/demo/${id}`, data),
  delete: (id: string) => http.delete(`/demo/${id}`)
}

export default { userApi, demoApi }
