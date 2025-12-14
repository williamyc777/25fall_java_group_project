const {defineConfig} = require('@vue/cli-service')
// const AutoImport = require('unplugin-auto-import/webpack')
// const Components = require('unplugin-vue-components/webpack')
// const {ElementPlusResolver} = require('unplugin-vue-components/resolvers')


module.exports = defineConfig({
    transpileDependencies: true,
    publicPath: '/',
    lintOnSave: false,
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8082',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        },
        // client: {
        //     overlay: {
        //         warnings: false,
        //         errors: true,
        //     },
        // },
        // add to avoid refresh 404
        historyApiFallback: true,
    },
    // plugins: [
    //     AutoImport({resolvers: [ElementPlusResolver()]}),
    //     Components({resolvers: [ElementPlusResolver()]}),
    // ],
})
