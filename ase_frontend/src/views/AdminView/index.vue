<template>
  <div class="home-container">
    <div class="home-left">
      <AdminNavbar @updateActiveTab="UpdateActiveTab"></AdminNavbar>
    </div>
    <div class="home-right">
      <component :is="activeComponent" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import AdminNavbar from '../../components/AdminNavbar/index.vue'
  import router from '../../router'
  import ConferenceReview from './component/ConferenceReview.vue'

  const tabs = [
    {
      id: 0,
      name: '审核会议',
      component: markRaw(ConferenceReview)
    }
  ]

  const state = reactive({
    activeTabIndex: 0,
    activeComponent: null
  })
  let { activeTabIndex, activeComponent } = toRefs(state)

  watchEffect(() => {
    const path = router.currentRoute.value.path
    if (path === '/admin') {
      const defaultActive = router.currentRoute.value.meta.defaultActive
      const activeQuery = router.currentRoute.value.query.active

      const pageName = activeQuery || defaultActive
      switch (pageName) {
        case 'conference':
          activeTabIndex.value = 0
          break
        default:
          activeTabIndex.value = 0
      }

      activeComponent.value = tabs[activeTabIndex.value].component
    }
  })

  const UpdateActiveTab = (activeTab: number) => {
    if (activeTab === 0) {
      router.push({ path: '/admin', query: { active: 'conference' } })
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
