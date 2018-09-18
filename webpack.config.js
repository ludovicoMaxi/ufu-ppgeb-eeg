const webpack = require('webpack')
const { resolve } = require('path')
const MiniCssExtractPlugin = require("mini-css-extract-plugin")

module.exports = {
    entry: './src/main/webapp/index.jsx',
    output: {
        path: resolve(__dirname, 'src', 'main', 'webapp', 'public'),
        filename: './app.js'
    },
    devServer: {
        port: 8080,
        contentBase: resolve(__dirname, 'src', 'main', 'webapp', 'public')
    },
    resolve: {
        extensions: ['*', '.js', '.jsx'],
        alias: {
            modules: resolve(__dirname, 'node_modules'),
            jquery: 'modules/admin-lte/bower_components/jquery/dist/jquery.min.js',
            bootstrap: 'modules/admin-lte/bower_components/bootstrap/js/bootstrap.js'
        }
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery',
            'window.jQuery': 'jquery'
        }),
        new MiniCssExtractPlugin({
            filename: "app.css"
        })
    ],
    module: {
        rules: [{
            test: /.js[x]?$/,
            loader: 'babel-loader',
            exclude: /node_modules/,
            query: {
                presets: ['es2015', 'react'],
                plugins: ['transform-object-rest-spread']
            }
        }, {
            test: /\.css$/,
            use: ['style-loader', MiniCssExtractPlugin.loader, 'css-loader']
        }, {
            test: /\.woff|.woff2|.ttf|.eot|.svg|.png|.jpg*.*$/,
            loader: 'file-loader'
        }]
    },
    watchOptions: {
        aggregateTimeout: 300, // The default
        ignored: /node_modules/,
        poll: 1000
    }
}