import { navigateTo } from '@tarojs/taro'

export const router = {
  toDemo: () => navigateTo({ url: '/pages/demo/demo' }),
  back: () => navigateTo({ url: '/pages/index/index' }),
}
