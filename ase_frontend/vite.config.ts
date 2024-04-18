import { fileURLToPath, URL } from 'node:url'

import { defineConfig, UserConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

import AutoImport from 'unplugin-auto-import/vite' // 自动导入
import Components from 'unplugin-vue-components/vite' // 组件注册
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers' // elementPlus
// @ts-ignore
import fs from 'fs'
// @ts-ignore
import dotenv from 'dotenv'

// https://vitejs.dev/config/
export default defineConfig(({ mode }: UserConfig): UserConfig => {
  // 根据环境变量加载环境变量文件
  const ASR_ENV = dotenv.parse(fs.readFileSync(`.env.${mode}`))
  return {
    base: ASR_ENV.VITE_PUBLIC_PATH,   // 环境路径
    server: {
      open: true, // 是否主动唤醒浏览器
      host: '0.0.0.0',
      port: 2400,
      https: false,
      proxy: {
        [ASR_ENV.VITE_BASE_API]: {
          target: `${ASR_ENV.VITE_BASE_PATH}`,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        },
      },
    },
    plugins: [
      vue(),
      AutoImport({
        resolvers: [ElementPlusResolver()],  //对于element puls的配置
        imports: [
        'vue', //自动引入的vue的ref等方法
        'vue-router',   //引入useRoute等方法
        {            //对于vue-router的type的扩展,配置后可以直接使用
          from: 'vue-router',
          imports: ['RouteLocationRaw',],
          type: true,
        },],
        dts: 'src/auto-imports.d.ts'
      }),
      Components({
        resolvers: [ElementPlusResolver()],   //对于element puls的配置
      }),
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          charset: false,
        },
      },
    },
    build: {
      outDir: 'dist',           // 指定输出路径
      assetsDir: 'static',      // 指定生成静态资源的存放路径
      minify: 'terser',         // 混淆器,terser构建后文件体积更小 ,boolean | 'terser' | 'esbuild',默认使用esbuild
      sourcemap: false,         // 是否产出soucemap.json
      manifest: false,          // 是否产出maifest.json
      // reportCompressedSize: true,
      chunkSizeWarningLimit: 1500,
      terserOptions: {
        compress: {
          drop_console: true,   // 生产环境移除console
          drop_debugger: true   // 生产环境移除debugger
        }
      },
    },
  }
})
