/**
 * tabBar 可用页面白名单
 *
 * 后台下发的 tabConfig.pagePath 必须在此列表内，否则视为非法配置会被过滤掉。
 * 新增 tab 页 → 在这里加一行 → 后台就能选到。
 */
export const TAB_PAGES = [
  'pages/index/index',
  'pages/dataAnalyse/index',
  'pages/demo/demo',
] as const

export type TabPagePath = (typeof TAB_PAGES)[number]
