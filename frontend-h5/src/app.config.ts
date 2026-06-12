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
      },
      {
        pagePath: 'pages/dataAnalyse/index',
        text: '分析',
      },
    ],
  },
})
