# frontend-h5

Taro 多端 H5 项目，基于 React + TypeScript + NutUI。

## 环境要求

- **Node.js** >= 18（推荐 Node 20+）
- **npm** >= 8

## 开发

```bash
# 安装依赖
npm install

# H5 开发（热重载）
npm run dev:h5

# 微信小程序开发
npm run dev:weapp
```

## 打包

```bash
# H5 打包
npm run build:h5

# 微信小程序打包
npm run build:weapp
```

## 项目结构

```
src/
  app.config.ts    # Taro 全局配置（页面路由、窗口样式）
  app.tsx          # 应用根组件
  app.scss         # 全局样式
  pages/           # 页面目录
    index/         # 首页
    demo/          # Demo 页
  router/          # 路由跳转封装
  store/           # Zustand 状态管理
config/
  index.ts         # Taro 构建配置
  dev.ts           # 开发环境配置
  prod.ts          # 生产环境配置
```

## 路由说明

Taro 使用声明式路由，在 `app.config.ts` 的 `pages` 数组中配置。第一个页面为默认首页。

编程式跳转使用 `router` 对象：

```ts
import { router } from '@/router'

router.toDemo()   // 跳转到 Demo 页
router.back()     // 返回首页
```

## 状态管理

使用 Zustand，示例 store 位于 `src/store/counter.ts`：

```ts
import { useCounterStore } from '@/store'

const { count, inc, dec } = useCounterStore()
```

## 添加新页面

1. 在 `src/pages/` 下创建页面目录，如 `src/pages/example/`
2. 添加 `example.config.ts`（页面配置）和 `example.tsx`（页面组件）
3. 在 `app.config.ts` 的 `pages` 数组中添加路径
