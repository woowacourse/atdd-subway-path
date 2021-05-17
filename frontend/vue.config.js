const path = require('path');

module.exports = {
  outputDir: path.resolve(__dirname, '../src/main/resources/static/'),
  devServer: {
    proxy: {
      '/': {
        target: 'http://localhost:8080/',
        ws: true,
        changeOrigin: true,
      },
    },
  },
  transpileDependencies: [
    'vuetify'
  ]
}
