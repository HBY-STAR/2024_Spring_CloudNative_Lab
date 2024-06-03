import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView/index.vue'
import LoginView from '../views/LoginView/index.vue'
import RegisterView from '../views/RegisterView/index.vue'
import AdminView from '../views/AdminView/index.vue'
import { getToken, getUsername } from '@/utils/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        title: '首页',
        requiresAuth: true,
        defaultActive: 'overview'
      }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: {
        title: '登录页',
        keepAlive: true
      }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: {
        title: '注册页',
        keepAlive: true
      }
    },
    {
      path: '/paper-contribute',
      name: 'paperContribution',
      component: HomeView,
      meta: {
        title: '会议投稿',
        requiresAuth: true
      }
    },
    {
      path: '/paper-detail',
      name: 'paperDetail',
      component: HomeView,
      meta: {
        title: '投稿详情',
        requiresAuth: true
      }
    },
    {
      path: '/conference-detail',
      name: 'conferenceDetail',
      component: HomeView,
      meta: {
        title: '会议详情',
        requiresAuth: true
      }
    },
    {
      path: '/conference-paper',
      name: 'conferencePaper',
      component: HomeView,
      meta: {
        title: '会议投稿',
        requiresAuth: true
      }
    },
    {
      path: '/admin',
      name: 'Admin',
      component: AdminView,
      meta: {
        title: '管理员页面',
        requiresAuth: true
      }
    }
  ]
})

// 登录拦截
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !getToken()) {
    // 如果路由需要身份验证并且用户未登录，重定向到登录页
    //next('/login')
    next()
  } else {
    // 否则，继续导航
    next()
  }
})

export default router
