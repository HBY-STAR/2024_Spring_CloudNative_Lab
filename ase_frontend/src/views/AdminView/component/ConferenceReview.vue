<template>
  <el-breadcrumb separator=">" class="conference-breadcrumb">
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item>审核会议</el-breadcrumb-item>
  </el-breadcrumb>
  <el-divider style="margin-top: 10px" />
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe>
    <el-table-column prop="applicantUsername" label="申请人" show-overflow-tooltip />
    <el-table-column prop="conferenceName" label="会议名称" show-overflow-tooltip />
    <el-table-column prop="conferenceAbbr" label="会议简称" show-overflow-tooltip />
    <el-table-column label="举办时间" show-overflow-tooltip>
      <template #default="scope">
        <span>{{ scope.row.conferenceStartAt }}-{{ scope.row.conferenceEndAt }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="venue" label="举办地点" show-overflow-tooltip />
    <el-table-column prop="applicationStatus" label="状态" show-overflow-tooltip />
    <el-table-column label="操作" width="150">
      <template #default="scope">
        <el-button
          v-if="scope.row.applicationStatus === '审核中'"
          type="primary"
          size="small"
          color="#28b445"
          @click="handleAccept(scope.row.applicationID, true)">
          同意
        </el-button>
        <el-button v-if="scope.row.applicationStatus === '审核中'" type="danger" size="small" @click="handleAccept(scope.row.applicationID, false)">
          拒绝
        </el-button>
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
  import { House } from '@element-plus/icons-vue'
  import { getAllAdminApplications, reviewConference } from '../../../api/conferenceApi'
  import { ElMessage } from 'element-plus'

  const state = reactive({
    tableData: [],
    current: 1
  })

  const { tableData, current } = toRefs(state)

  onMounted(() => {
    fetchData()
  })

  const fetchData = () => {
    getAllAdminApplications().then((res: any) => {
      if (res.status === 200) {
        tableData.value = res.data.data
      }
    })
  }

  const handleCurrentChange = (newPage: any) => {
    current.value = newPage
  }

  const handleAccept = (applicationId: any, accept: boolean) => {
    reviewConference({
      applicationId: applicationId,
      applicationPass: accept
    }).then((res: any) => {
      if (res.status === 200) {
        ElMessage.success('审核成功')
        fetchData()
      } else if (res.status === 500) {
        ElMessage.error(res.data.message)
      }
    })
  }
</script>

<style scoped>
  .conference-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }
</style>
