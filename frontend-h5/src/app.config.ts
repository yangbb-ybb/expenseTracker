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
  tabBar: {
    color: '#999999',
    selectedColor: 'var(--theme-primary)',
    backgroundColor: '#ffffff',
    borderStyle: 'black',
    list: [
      {
        pagePath: 'pages/index/index',
        text: '首页',
        iconPath: 'assets/images/tabbar/home.png',
        selectedIconPath: 'assets/images/tabbar/home-active.png',
      },
      {
        pagePath: 'pages/dataAnalyse/index',
        text: '分析',
        iconPath: 'assets/images/tabbar/chart.png',
        selectedIconPath: 'assets/images/tabbar/chart-active.png',
      },
    ],
  },
})
