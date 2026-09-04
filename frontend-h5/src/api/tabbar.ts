import http from '@/utils/request'
// @ts-ignore - 直接读 package.json，tsconfig 已开 resolveJsonModule
import pkg from '../../package.json'

/** tab 单项配置 */
export interface TabConfig {
  pagePath: string
  text: string
  icon: string
  iconActive: string
  order: number
  visible: boolean
  status: 'active' | 'deprecated' | 'offline'
}

/** 后台下发的完整配置 */
export interface TabbarConfigResponse {
  minVersion: string
  forceUpdate: boolean
  upgradeTip: string
  upgradeUrl: string
  tabs: TabConfig[]
}

/** 当前 app 版本（构建时由 webpack 静态打入） */
export const APP_VERSION: string = (pkg as any).version || '1.0.0'

/**
 * 获取 tabBar 配置（按 appVersion + platform 下发，支持老版本兼容）
 */
export function getTabbarConfig(): Promise<TabbarConfigResponse> {
  return http.get('/api/tabbar/config', {
    params: {
      appVersion: APP_VERSION,
      platform: 'h5',
    },
  })
}
