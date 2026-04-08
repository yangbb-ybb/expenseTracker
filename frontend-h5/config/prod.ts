export default {
  env: {
    NODE_ENV: '"production"'
  },
  defineConstants: {},
  mini: {},
  h5: {
    minify: {
      enable: true,
      terser: {
        enable: true,
        config: {
          compress: {
            drop_console: true,    // 移除 console.log
            drop_debugger: true,   // 移除 debugger
          },
          mangle: {
            safari10: true,
          },
          output: {
            comments: false,      // 移除注释
          }
        }
      },
      cssMinifier: 'cssnano',      // CSS 压缩器
      cssMinifierOptions: {
        preset: ['default', {
          discardComments: { removeAll: true },
        }],
      }
    }
  }
}
