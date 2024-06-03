import './assets/main.css'

import { createApp } from 'vue'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css'

import { createPinia } from 'pinia'
import { useUserStore } from './store/userStore'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(ElementPlus, {
    locale: zhCn,
  })

export const userStore = useUserStore()

app.mount('#app')
