import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
import Taro from '@tarojs/taro'

// API 基础地址
const BASE_URL = process.env.TARO_APP_API_BASE_URL || 'https://api.example.com'

// 创建 axios 实例
const request: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 添加 token（从 storage 获取）
    const token = Taro.getStorageSync('token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }

    // 添加时间戳防止缓存（GET 请求）
    if (config.method === 'get' && !config.params) {
      config.params = { _t: Date.now() }
    }

    return config
  },
  (error: AxiosError) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response

    // 根据业务逻辑判断成功失败
    if (data.code === 200 || data.code === 0 || data.success === true) {
      return data.data ?? data
    }

    // token 过期
    if (data.code === 401 || data.code === 403) {
      Taro.removeStorageSync('token')
      Taro.showToast({ title: '登录已过期，请重新登录', icon: 'none' })
      // 可触发重新登录逻辑
    }

    Taro.showToast({ title: data.message || '请求失败', icon: 'none' })
    return Promise.reject(data)
  },
  (error: AxiosError) => {
    console.error('响应错误:', error)

    if (error.response) {
      const status = error.response.status

      switch (status) {
        case 400:
          Taro.showToast({ title: '请求参数错误', icon: 'none' })
          break
        case 401:
          Taro.showToast({ title: '未授权，请重新登录', icon: 'none' })
          Taro.removeStorageSync('token')
          break
        case 403:
          Taro.showToast({ title: '无访问权限', icon: 'none' })
          break
        case 404:
          Taro.showToast({ title: '请求地址不存在', icon: 'none' })
          break
        case 500:
          Taro.showToast({ title: '服务器错误', icon: 'none' })
          break
        default:
          Taro.showToast({ title: '网络请求失败', icon: 'none' })
      }
    } else if (error.code === 'ECONNABORTED') {
      Taro.showToast({ title: '请求超时', icon: 'none' })
    } else {
      Taro.showToast({ title: '网络连接失败', icon: 'none' })
    }

    return Promise.reject(error)
  }
)

// 封装请求方法
export const http = {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.get(url, config)
  },

  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return request.post(url, data, config)
  },

  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return request.put(url, data, config)
  },

  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.delete(url, config)
  },

  patch<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return request.patch(url, data, config)
  }
}

export default http
