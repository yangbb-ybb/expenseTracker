export default {
  env: {},
  defineConstants: {},
  mini: {},
  h5: {
    hot: true,
    miniCssExtractPluginOption: {
      ignoreOrder: true
    },
    devServer: {
      port: 3000,
      client: {
        overlay: false
      }
    }
  }
}
