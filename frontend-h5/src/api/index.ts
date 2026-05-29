import http from '@/utils/request'
import consumeApi from './consume'

// 用户相关
export const userApi = {
  login: (data: { username: string; password: string }) => http.post('/login', data),
  wxLogin: (data: { code: string }) => http.post('/login/wx', data),
  aliLogin: (data: { authCode: string }) => http.post('/login/ali', data),
  ttLogin: (data: { code: string }) => http.post('/login/tt', data),
  sendSms: (data: { phone: string }) => http.post('/sms/send', data),
  smsLogin: (data: { phone: string; code: string }) => http.post('/login/sms', data),
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

export default { userApi, demoApi, consumeApi }
