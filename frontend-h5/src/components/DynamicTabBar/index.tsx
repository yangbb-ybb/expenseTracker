import { useEffect, useState } from 'react'
import { View, Image, Text } from '@tarojs/components'
import Taro from '@tarojs/taro'
import { getTabbarConfig, TabConfig, TabbarConfigResponse } from '@/api/tabbar'
import { TAB_PAGES } from '@/config/tabPages'
import homeIcon from '@/assets/images/tabbar/home.png'
import homeActiveIcon from '@/assets/images/tabbar/home-active.png'
import chartIcon from '@/assets/images/tabbar/chart.png'
import chartActiveIcon from '@/assets/images/tabbar/chart-active.png'
import './index.scss'

/** 后台可下发的 pagePath 白名单 */
const ALLOWED = new Set<string>(TAB_PAGES)

/**
 * 兜底配置（接口挂了 / 拉不到数据时使用）
 * 本地图片用 import 引入，webpack 会处理成正确 URL
 */
const FALLBACK: TabConfig[] = [
  {
    pagePath: 'pages/index/index',
    text: '首页',
    icon: homeIcon,
    iconActive: homeActiveIcon,
    order: 1,
    visible: true,
    status: 'active',
  },
  {
    pagePath: 'pages/dataAnalyse/index',
    text: '分析',
    icon: chartIcon,
    iconActive: chartActiveIcon,
    order: 2,
    visible: true,
    status: 'active',
  },
]

/** 升级相关元信息 */
interface UpgradeMeta {
  minVersion: string
  forceUpdate: boolean
  upgradeTip: string
  upgradeUrl: string
}

const DEFAULT_META: UpgradeMeta = {
  minVersion: '',
  forceUpdate: false,
  upgradeTip: '',
  upgradeUrl: '',
}

/**
 * 监听 Taro H5 路由变化，返回当前 pagePath
 * Taro 事件 payload: { toLocation: { path: '/pages/foo/index' } }
 */
function useCurrentPage(): string {
  const [path, setPath] = useState<string>(() => {
    try {
      return Taro.getCurrentInstance()?.page || ''
    } catch {
      return ''
    }
  })

  useEffect(() => {
    const handler = (data: { toLocation?: { path?: string } }) => {
      const newPath = data?.toLocation?.path || ''
      if (newPath) setPath(newPath.replace(/^\/+/, ''))
    }
    Taro.eventCenter.on('__taroRouterChange', handler)
    return () => {
      Taro.eventCenter.off('__taroRouterChange', handler)
    }
  }, [])

  return path
}

export default function DynamicTabBar() {
  const [tabs, setTabs] = useState<TabConfig[]>(FALLBACK)
  const [meta, setMeta] = useState<UpgradeMeta>(DEFAULT_META)
  const currentPath = useCurrentPage()

  useEffect(() => {
    let cancelled = false

    getTabbarConfig()
      .then((res: TabbarConfigResponse) => {
        if (cancelled || !res || !Array.isArray(res.tabs)) return

        setMeta({
          minVersion: res.minVersion || '',
          forceUpdate: !!res.forceUpdate,
          upgradeTip: res.upgradeTip || '',
          upgradeUrl: res.upgradeUrl || '',
        })

        // 强制升级弹窗
        if (res.forceUpdate) {
          Taro.showModal({
            title: '升级提示',
            content: res.upgradeTip || '当前版本过低，请升级后继续使用',
            showCancel: false,
            confirmText: '立即升级',
          }).then(() => {
            if (res.upgradeUrl) window.location.href = res.upgradeUrl
          })
          return
        }

        // 白名单过滤 + 过滤掉 offline + 按 order 排序
        const list = res.tabs
          .filter((t) => ALLOWED.has(t.pagePath))
          .filter((t) => t.status !== 'offline')
          .sort((a, b) => a.order - b.order)

        if (list.length) setTabs(list)
      })
      .catch(() => {
        // 拉失败就用 FALLBACK，不阻塞 UI
      })

    return () => {
      cancelled = true
    }
  }, [])

  // 只在 tab 页显示 tabBar
  const onTabPage = tabs.some((t) => t.pagePath === currentPath)
  if (!onTabPage) return null

  const handleClick = (tab: TabConfig) => {
    // deprecated：弹升级提示，用户确认升级则跳，否则继续访问
    if (tab.status === 'deprecated') {
      Taro.showModal({
        title: '功能即将下线',
        content: meta.upgradeTip || '请升级到最新版本以获得完整体验',
        confirmText: '立即升级',
        cancelText: '继续使用',
      }).then(({ confirm }) => {
        if (confirm && meta.upgradeUrl) {
          window.location.href = meta.upgradeUrl
        } else {
          doNavigate(tab.pagePath)
        }
      })
      return
    }

    doNavigate(tab.pagePath)
  }

  return (
    <View className='dyn-tabbar'>
      {tabs.map((tab) => {
        const active = tab.pagePath === currentPath
        return (
          <View
            key={tab.pagePath}
            className={`dyn-tabbar__item ${active ? 'is-active' : ''}`}
            onClick={() => handleClick(tab)}
          >
            <View className='dyn-tabbar__icon-wrap'>
              <Image
                src={active ? tab.iconActive : tab.icon}
                className='dyn-tabbar__icon'
                mode='aspectFit'
              />
              {tab.status === 'deprecated' && (
                <View className='dyn-tabbar__badge'>即将下线</View>
              )}
            </View>
            <Text className={`dyn-tabbar__text ${active ? 'is-active' : ''}`}>
              {tab.text}
            </Text>
          </View>
        )
      })}
    </View>
  )
}

/**
 * 切换 tab 页：通过 Taro router 触发路由变更
 * 优先用 redirectTo（替换当前页，不入栈），避免 navigateTo 的 10 页限制
 */
function doNavigate(pagePath: string) {
  const url = pagePath.startsWith('/') ? pagePath : `/${pagePath}`
  try {
    // redirectTo: 替换当前页，不入栈（行为最接近 tabBar 切换）
    Taro.redirectTo({ url }).catch(() => {
      // redirectTo 在某些场景下不可用，回退到 navigateTo
      Taro.navigateTo({ url })
    })
  } catch {
    Taro.navigateTo({ url })
  }
}
