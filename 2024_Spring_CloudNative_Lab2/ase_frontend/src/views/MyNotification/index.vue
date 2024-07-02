<template>
  <el-breadcrumb separator=">" class="notification-breadcrumb">
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item>我的邀请</el-breadcrumb-item>
  </el-breadcrumb>
  <el-divider style="margin-top: 10px" />
  <el-tabs v-model="activeTabName" type="card" class="tab-conference">
    <el-tab-pane label="我发送的" name="1"><Send></Send></el-tab-pane>
    <el-tab-pane label="我收到的" name="2"><Receive></Receive></el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
  import { House } from '@element-plus/icons-vue'
  import Send from './component/Send.vue'
  import Receive from './component/Receive.vue'

  const state = reactive({
    activeTabName: '1'
  })

  const { activeTabName } = toRefs(state)
</script>

<style scoped>
  .notification-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }
</style>
