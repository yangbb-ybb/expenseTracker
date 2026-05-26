import Taro from '@tarojs/taro'
import { userApi } from '@/api'
import { showPhoneLoginModal } from '@/components/PhoneLoginModal/modal'

const TOKEN_KEY = 'token'

export function getToken(): string | undefined {
  try {
    return Taro.getStorageSync(TOKEN_KEY) || undefined
  } catch {
    return undefined
  }
}

export function setToken(token: string): void {
  Taro.setStorageSync(TOKEN_KEY, token)
}

export function removeToken(): void {
  Taro.removeStorageSync(TOKEN_KEY)
}

export function isLoggedIn(): boolean {
  return !!getToken()
}

/**
 * 判断是否为特定 App 内嵌 H5（通过 UA 识别）
 */
function isSpecificApp(): boolean {
  if (typeof window === 'undefined') return false
  const ua = window.navigator.userAgent.toLowerCase()
  // TODO: 按实际 App 的 UA 特征修改
  return ua.includes('myapp') || ua.includes('specific-app')
}

/**
 * 执行登录，按平台自动选择登录方式：
 * - 微信：静默登录
 * - 支付宝：静默登录
 * - 抖音：静默登录
 * - 特定 App：自定义逻辑
 * - 其他：手机号登录弹窗
 */
export async function doLogin(): Promise<string> {
  const env = Taro.getEnv()

  if (env === Taro.ENV_TYPE.WEAPP) {
    const { code } = await Taro.login()
    const res: any = await userApi.wxLogin({ code })
    const token = res?.token ?? res?.data?.token
    if (!token) throw new Error('微信登录失败')
    setToken(token)
    return token
  }

  if (env === Taro.ENV_TYPE.ALIPAY) {
    const { authCode } = await (Taro as any).getAuthCode({ scopes: 'auth_base' })
    const res: any = await userApi.aliLogin({ authCode })
    const token = res?.token ?? res?.data?.token
    if (!token) throw new Error('支付宝登录失败')
    setToken(token)
    return token
  }

  if (env === Taro.ENV_TYPE.TT) {
    const { code } = await Taro.login()
    const res: any = await userApi.ttLogin({ code })
    const token = res?.token ?? res?.data?.token
    if (!token) throw new Error('抖音登录失败')
    setToken(token)
    return token
  }

  // 这里待定
  if (isSpecificApp()) {
    // TODO: 按实际业务实现，例如通过 JSBridge 调 App 原生登录
    return Promise.reject(new Error('特定 App 登录逻辑未实现'))
  }

  // 首先完成手机号登录
  return showPhoneLoginModal()
}

/**
 * 确保用户已登录：有 token 直接返回，没有则尝试登录
 */
export async function ensureLoggedIn(): Promise<string> {
  const token = getToken()
  if (token) {
    return token
  }
  return doLogin()
}
