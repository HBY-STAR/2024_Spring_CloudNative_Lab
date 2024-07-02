<template>
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe>
    <el-table-column prop="sender" label="发送者" show-overflow-tooltip />
    <el-table-column prop="conferenceName" label="会议名称" show-overflow-tooltip />
    <el-table-column prop="inviteTime" label="发送时间" show-overflow-tooltip />
    <el-table-column prop="status" label="邀请状态" show-overflow-tooltip />
    <el-table-column label="操作" width="150">
      <template #default="scope">
        <el-button v-if="scope.row.status === '待确认'" type="primary" size="small" color="#28b445" @click="handleAccept(scope.row.notificationId, true)">
          同意
        </el-button>
        <el-button v-if="scope.row.status === '待确认'" type="danger" size="small" @click="handleAccept(scope.row.notificationId, false)">拒绝</el-button>
        <span v-else>--</span>
      </template>
    </el-table-column>
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
  import { reactive } from 'vue'
  import { userStore } from '../../../main'
  import { handleReceive, handleInvitation } from '../../../api/notificationApi'
  import { getUsername } from '../../../utils/auth'
  import { ElMessage } from 'element-plus'
  import { invitationStatus } from '../../../utils/const'

  const state = reactive({
    tableData: [],
    current: 1
  })
  const { tableData, current } = toRefs(state)

  onMounted(() => {
    fetchData()
  })

  const fetchData = () => {
    handleReceive(getUsername()).then((res: any) => {
      if (res.status === 200 && res.data.success) {
        tableData.value = res.data.data
        tableData.value.map((v: any) => {
          v.status = invitationStatus[v.status]
          return v
        })
      }
    })
  }

  const handleCurrentChange = (newPage: any) => {
    current.value = newPage
  }

  const handleAccept = (notificationId: any, accept: boolean) => {
    handleInvitation({
      username: getUsername(),
      notificationId: notificationId,
      accept: accept
    }).then((res: any) => {
      if (res.status === 200 && res.data.success) {
        ElMessage.success(res.data.message)
        fetchData()
      }
    })
  }
</script>
