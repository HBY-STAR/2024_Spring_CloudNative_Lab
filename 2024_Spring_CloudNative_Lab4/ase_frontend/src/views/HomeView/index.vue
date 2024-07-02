<template>
  <div class="home-container">
    <div class="home-left">
      <Navbar @updateActiveTab="UpdateActiveTab"></Navbar>
    </div>
    <div class="home-right">
      <component :is="activeComponent" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import Navbar from '../../components/Navbar/index.vue'
  import ConferenceOverview from '../ConferenceOverview/index.vue'
  import MyConference from '../MyConference/index.vue'
  import MyPaper from '../MyPaper/index.vue'
  import MyReview from '../MyReview/index.vue'
  import MyProfile from '../MyProfile/index.vue'
  import MyNotification from '../MyNotification/index.vue'
  import PaperDetail from '@/views/MyPaper/component/PaperDetail.vue'
  import ConferenceDetail from '@/views/MyConference/component/ConferenceDetail.vue'
  import ConferencePaper from '@/views/MyConference/component/ConferencePaper.vue'
  import router from '../../router'

  const tabs = [
    {
      id: 0,
      name: '会议概览',
      component: markRaw(ConferenceOverview)
    },
    {
      id: 1,
      name: '我的会议',
      component: markRaw(MyConference)
    },
    {
      id: 2,
      name: '我的投稿',
      component: markRaw(MyPaper)
    },
    {
      id: 3,
      name: '个人中心',
      component: markRaw(MyProfile)
    },
    {
      id: 4,
      name: '通知',
      component: markRaw(MyNotification)
    },
    {
      id: 5,
      name: '我的审稿',
      component: markRaw(MyReview)
    }
  ]

  const state = reactive({
    activeTabIndex: 0,
    activeComponent: null
  })
  let { activeTabIndex, activeComponent } = toRefs(state)

  watchEffect(() => {
    const path = router.currentRoute.value.path
    if (path === '/') {
      const defaultActive = router.currentRoute.value.meta.defaultActive
      const activeQuery = router.currentRoute.value.query.active

      const pageName = activeQuery || defaultActive
      switch (pageName) {
        case 'overview':
          activeTabIndex.value = 0
          break
        case 'conference':
          activeTabIndex.value = 1
          break
        case 'paper':
          activeTabIndex.value = 2
          break
        case 'profile':
          activeTabIndex.value = 3
          break
        case 'notification':
          activeTabIndex.value = 4
          break
        case 'review':
          activeTabIndex.value = 5
          break
        default:
          activeTabIndex.value = 0
      }

      activeComponent.value = tabs[activeTabIndex.value].component
    } else if (path === '/paper-detail') {
      activeTabIndex.value = router.currentRoute.value.query.level === 'person' ? 2 : 1
      activeComponent.value = markRaw(PaperDetail)
    } else if (path === '/conference-detail') {
      activeTabIndex.value = 1
      activeComponent.value = markRaw(ConferenceDetail)
    } else if (path === '/conference-paper') {
      activeTabIndex.value = 1
      activeComponent.value = markRaw(ConferencePaper)
    }
  })

  const UpdateActiveTab = (activeTab: number) => {
    if (activeTab === 0) {
      router.push({ path: '/', query: { active: 'overview' } })
    } else if (activeTab === 1) {
      router.push({ path: '/', query: { active: 'conference' } })
    } else if (activeTab === 2) {
      router.push({ path: '/', query: { active: 'paper' } })
    } else if (activeTab === 3) {
      router.push({ path: '/', query: { active: 'profile' } })
    } else if (activeTab === 4) {
      router.push({ path: '/', query: { active: 'notification' } })
    } else if (activeTab === 5){
      router.push({ path: '/', query: { active: 'review' }})
    }
  }
</script>

<style scoped>
  .home-container {
    height: 100%;
    display: flex;
    justify-content: space-between;
    background: url('../../assets/imgs/background.jpg') center no-repeat;
  }

  .home-left {
    width: 20%;
    border-radius: 20px;
  }

  .home-right {
    background-color: white;
    width: 76%;
    height: 95%;
    margin-top: 1%;
    margin-right: 2%;
    border-radius: 20px;
    padding: 20px;
  }
</style>

<style>
  .el-breadcrumb__inner.is-link:hover {
    color: var(--color-theme) !important;
  }
</style>
