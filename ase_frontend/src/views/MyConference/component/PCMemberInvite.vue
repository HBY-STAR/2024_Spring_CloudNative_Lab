<template>
  <el-button type="primary" color="#28b445" @click="visible = true" style="margin-right: 20px">邀请审稿人</el-button>
  <el-dialog v-model="visible" title="邀请" width="50%" style="top: -30px" @close="handleCancel">
    <el-tabs v-model="activeTabName" tab-position="left" class="tab-invitation">
      <el-tab-pane label="邀请用户" name="1"><UserInvite :conferenceName="props.conferenceName" :topics="props.topics"/></el-tab-pane>
      <el-tab-pane label="已发送" name="2"><HaveInvited :conferenceName="props.conferenceName" /></el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup lang="ts">
  import UserInvite from '@/views/MyConference/component/UserInvite.vue'
  import HaveInvited from '@/views/MyConference/component/HaveInvited.vue'

  const props = defineProps({
    conferenceName: String,
    topics: []
  })

  const state = reactive({
    visible: false,
    activeTabName: '1'
  })
  const { visible, activeTabName } = toRefs(state)

  const handleCancel = () => {
    visible.value = false
  }

  const handleConfirm = () => {}
</script>

<style>
  .el-tabs__item.is-active {
    color: var(--color-theme) !important;
  }

  .el-tabs__item:hover {
    color: var(--color-theme) !important;
  }

  .el-tabs__header {
    border-bottom: 0px !important;
  }

  .el-tabs__active-bar {
    background-color: var(--color-theme) !important;
  }
</style>
