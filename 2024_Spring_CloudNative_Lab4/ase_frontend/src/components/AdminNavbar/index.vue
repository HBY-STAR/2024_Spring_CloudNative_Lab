<template>
  <el-menu :default-active="selectedItem" class="nav-menu" active-text-color="#61ac85" @select="handleMenuSelect">
    <div>
      <div class="nav-title nav-item">RevuSage</div>
      <el-menu-item index="1" class="nav-item">
        <el-icon><House /></el-icon>
        &nbsp;&nbsp;
        <template #title>审&nbsp;&nbsp;核&nbsp;&nbsp;会&nbsp;&nbsp;议</template>
      </el-menu-item>
    </div>
    <div class="nav-bottom nav-item">
      <div class="nav-bottom-left">
        <ElDropdown trigger="click" style="height: 70px; line-height: 70px" @command="handleCommand">
          <div class="nav-dropdown">
            <el-avatar :icon="UserFilled" />
            <el-icon style="margin-left: 10px"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <ElDropdownMenu>
              <ElDropdownItem command="logout">退出登录</ElDropdownItem>
            </ElDropdownMenu>
          </template>
        </ElDropdown>
      </div>
    </div>
  </el-menu>
</template>

<script setup lang="ts">
  import { onActivated, reactive, toRefs, watchEffect } from 'vue'
  import { House, ArrowDown, UserFilled } from '@element-plus/icons-vue'
  import { ElDropdown, ElDropdownItem, ElDropdownMenu, ElMessageBox } from 'element-plus'
  import { delToken, delUsername, getUsername } from '../../utils/auth'
  import router from '../../router'
  import { userStore } from '../../main'

  const state = reactive({
    username: '',
    selectedItem: '1'
  })
  const { username, selectedItem } = toRefs(state)

  onActivated(() => {
    username.value = getUsername()
  })

  watchEffect(() => {
    const path = router.currentRoute.value.path
    if (path === '/') {
      const defaultActive = router.currentRoute.value.meta.defaultActive
      const activeQuery = router.currentRoute.value.query.active

      const pageName = activeQuery || defaultActive
      switch (pageName) {
        case 'conference':
          selectedItem.value = '1'
          break
        default:
          selectedItem.value = '1'
      }
    }
  })

  const emit = defineEmits(['UpdateActiveTab'])

  const handleMenuSelect = (key: string, keyPath: string[]) => {
    selectedItem.value = key
    handleSelect(parseInt(key) - 1)
  }

  const handleSelect = (value: number) => {
    emit('UpdateActiveTab', value)
  }

  const handleCommand = (value: any) => {
    if (value === 'logout') {
      ElMessageBox.confirm('确认退出登录?', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      })
        .then(() => {
          delToken()
          delUsername()
          userStore.setUsername('')
          router.push({ path: '/login' })
        })
        .catch(() => {})
    }
  }
</script>

<style scoped>
  .nav-menu {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    border-radius: 20px;
  }

  .nav-item {
    width: 100%;
    text-align: center;
    font-size: 1.1rem;
    display: flex;
    justify-content: center;
    height: 70px;
    /* border-bottom: 1px solid var(--vt-c-divider-light-1); */
  }

  .nav-title {
    font-family: 'KKSAKAI', sans-serif;
    font-size: 3rem;
    color: var(--color-text-light);
    background-color: var(--color-icon);
    margin-bottom: 10px;
    border-top-right-radius: 20px;
    /* border: none; */
  }

  .nav-bottom {
    display: flex;
    font-size: 20px;
    line-height: 70px;
    justify-content: space-between;
    color: var(--color-text-light);
    background-color: var(--color-icon);
    border-bottom-right-radius: 20px;
  }

  .nav-bottom-left {
    margin-left: 20px;
  }

  .nav-dropdown {
    font-size: 17px;
    cursor: pointer;
    color: var(--color-text-light);
  }
</style>
