const webpack = require('webpack')
const { resolve } = require('path')
const MiniCssExtractPlugin = require("mini-css-extract-plugin")

module.exports = (env, argv) => {
    const isProduction = argv.mode === 'production'

    return {
        entry: './src/main/webapp/index.jsx',
        output: {
            path: resolve(__dirname, 'src', 'main', 'webapp', 'public'),
            filename: 'app.js',
            clean: isProduction
        },
        devServer: {
            port: 8080,
            static: {
                directory: resolve(__dirname, 'src', 'main', 'webapp', 'public')
            },
            hot: true
        },
        resolve: {
            extensions: ['.js', '.jsx'],
            alias: {
                modules: resolve(__dirname, 'node_modules')
            }
        },
        plugins: [
            new MiniCssExtractPlugin({
                filename: "app.css"
            })
        ],
        module: {
            rules: [{
                test: /\.jsx?$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            ['@babel/preset-env', { targets: { browsers: ['last 2 versions', 'not dead'] } }],
                            ['@babel/preset-react', { runtime: 'classic' }]
                        ]
                    }
                }
            }, {
                test: /\.css$/,
                use: [MiniCssExtractPlugin.loader, 'css-loader']
            }, {
                test: /\.(woff|woff2|ttf|eot|svg|png|jpg|gif)$/,
                type: 'asset/resource'
            }]
        },
        watchOptions: {
            aggregateTimeout: 300,
            ignored: /node_modules/,
            poll: 1000
        }
    }
}
