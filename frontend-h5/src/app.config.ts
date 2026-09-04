export default defineAppConfig({
  pages: [
    'pages/index/index',
    'pages/dataAnalyse/index',
    'pages/demo/demo',
  ],
  window: {
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#fff',
    navigationBarTitleText: 'frontend-h5',
    navigationBarTextStyle: 'black'
  },
  // tabBar 由 DynamicTabBar 组件运行时从后台拉取渲染
  // 这里不写 tabBar 配置，否则 Taro 会渲染一份静态的并盖住自定义组件
})
