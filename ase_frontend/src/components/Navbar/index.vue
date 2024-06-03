<template>
  <el-menu :default-active="selectedItem" class="nav-menu" active-text-color="#61ac85" @select="handleMenuSelect">
    <div>
      <div class="nav-title nav-item">RevuSage</div>
      <el-menu-item index="1" class="nav-item">
        <el-icon><House /></el-icon>
        &nbsp;&nbsp;
        <template #title>会&nbsp;&nbsp;议&nbsp;&nbsp;概&nbsp;&nbsp;览</template>
      </el-menu-item>
      <el-menu-item index="2" class="nav-item">
        <el-icon><Monitor /></el-icon>
        &nbsp;&nbsp;
        <template #title>我&nbsp;&nbsp;的&nbsp;&nbsp;会&nbsp;&nbsp;议</template>
      </el-menu-item>
      <el-menu-item index="3" class="nav-item">
        <el-icon><Message /></el-icon>
        &nbsp;&nbsp;
        <template #title>我&nbsp;&nbsp;的&nbsp;&nbsp;投&nbsp;&nbsp;稿</template>
      </el-menu-item>
      <el-menu-item index="6" class="nav-item">
        <el-icon><Message /></el-icon>
        &nbsp;&nbsp;
        <template #title>我&nbsp;&nbsp;的&nbsp;&nbsp;审&nbsp;&nbsp;稿</template>
      </el-menu-item>
      <el-menu-item index="4" class="nav-item">
        <el-icon><User /></el-icon>
        &nbsp;&nbsp;
        <template #title>个&nbsp;&nbsp;人&nbsp;&nbsp;中&nbsp;&nbsp;心</template>
      </el-menu-item>
    </div>
    <div class="nav-bottom nav-item">
      <div class="nav-bottom-left">
        <ElDropdown trigger="click" style="height: 70px; line-height: 70px" @command="handleCommand">
          <div class="nav-dropdown">
            <el-avatar :icon="UserFilled" />
            <!-- <el-icon style="margin-top: 20px;"><Avatar /></el-icon>&nbsp;&nbsp;
                  <ElTooltip :content="username" style="font-size: 18px;" :disabled="username?.length <= 10">
                    <span>{{ username?.length > 10 ? username.substring(0, 10)+'...' : username }}</span>
                  </ElTooltip> -->
            <el-icon style="margin-left: 10px"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <ElDropdownMenu>
              <ElDropdownItem command="logout">注销用户</ElDropdownItem>
              <!-- <ElDropdownItem command="logoff" divided><span style="color: red">注销用户</span></ElDropdownItem> -->
            </ElDropdownMenu>
          </template>
        </ElDropdown>
      </div>
      <div style="font-size: 25px; margin-left: -85px;font-style: italic; font-weight: bold; font-family: 'KKSAKAI', sans-seri">
        {{username}}
      </div>
      <div class="nav-bottom-right" @click="handleBellSelect">
        <el-icon v-if="selectedItem === ''"><BellFilled /></el-icon>
        <el-icon v-else><Bell /></el-icon>
      </div>
    </div>
  </el-menu>
</template>

<script setup lang="ts">
  import { House, User, Message, Monitor, Avatar, Bell, BellFilled, ArrowDown, UserFilled } from '@element-plus/icons-vue'
  import { ElDropdown, ElDropdownItem, ElDropdownMenu, ElMessage, ElMessageBox } from 'element-plus'
  import { delToken, delUsername, getUsername } from '../../utils/auth'
  import router from '../../router'
  import { userStore } from '../../main'
  import { logOut } from '../../api/userApi'

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
        case 'overview':
          selectedItem.value = '1'
          break
        case 'conference':
          selectedItem.value = '2'
          break
        case 'paper':
          selectedItem.value = '3'
          break
        case 'profile':
          selectedItem.value = '4'
          break
        case 'notification':
          selectedItem.value = ''
          break
        case 'review':
          selectedItem.value = '6'
          break
        default:
          selectedItem.value = '1'
      }
    } else if (path === '/paper-detail') {
      selectedItem.value = router.currentRoute.value.query.level === 'person' ? '3' : '2'
    } else if (path === '/conference-detail') {
      selectedItem.value = '2'
    } else if (path === '/conference-paper') {
      selectedItem.value = '2'
    }
  })

  const emit = defineEmits(['UpdateActiveTab'])

  const handleBellSelect = () => {
    selectedItem.value = ''
    handleSelect(4)
  }

  const handleMenuSelect = (key: string, keyPath: string[]) => {
    selectedItem.value = key
    handleSelect(parseInt(key) - 1)
  }

  const handleSelect = (value: number) => {
    emit('UpdateActiveTab', value)
  }

  const handleCommand = (value: any) => {
    if (value === '') {
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
    } else {
      ElMessageBox.confirm('确认注销该用户?', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      })
        .then(() => {
          logOut().then((res: any) => {
            if (res.status === 200) {
              delToken()
              delUsername()
              userStore.setUsername('')
              ElMessage.success('注销成功')
              router.push({ path: '/login' })
            }
          })
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

  .nav-bottom-right {
    margin-right: 20px;
  }
</style>
