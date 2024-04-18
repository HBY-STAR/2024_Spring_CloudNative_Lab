<template>
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe border>
    <el-table-column prop="receiver" label="用户名" show-overflow-tooltip />
    <el-table-column prop="sender" label="邀请人" show-overflow-tooltip />
    <el-table-column prop="inviteTime" label="邀请时间" show-overflow-tooltip />
    <el-table-column prop="status" label="状态" show-overflow-tooltip />
  </el-table>
  <el-pagination
    v-if="tableData.length > 10"
    layout="prev, pager, next"
    :total="tableData.length"
    :current-page="current"
    @current-change="handleCurrentChange"
    style="justify-content: center" />
</template>

<script setup lang="ts">
  import { ElMessage } from 'element-plus'
  import { handleConferenceInvitation } from '../../../api/notificationApi'
  import { invitationStatus } from '../../../utils/const'
  import { events } from '../../../bus'

  const props = defineProps({
    conferenceName: String
  })

  const state = reactive({
    tableData: [],
    current: 1
  })

  const { tableData, current } = toRefs(state)

  onMounted(() => {
    fetchData()

    events.on('inviteUser', () => {
      fetchData()
    })
  })

  onBeforeUnmount(() => {
    events.off('inviteUser')
  })

  const handleCurrentChange = (newPage: any) => {
    current.value = newPage
  }

  const fetchData = () => {
    handleConferenceInvitation(props.conferenceName).then((res: any) => {
      if (res.status === 200 && res.data.success) {
        tableData.value = res.data.data
        tableData.value.map((v: any) => {
          v.status = invitationStatus[v.status]
          return v
        })
      }
    })
  }
</script>
