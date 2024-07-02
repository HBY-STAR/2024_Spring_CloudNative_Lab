<template>
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe>
    <el-table-column prop="conferenceName" label="会议名称" show-overflow-tooltip />
    <el-table-column prop="conferenceAbbr" label="会议简称" show-overflow-tooltip />
    <el-table-column label="举办时间" show-overflow-tooltip>
      <template #default="scope">
        <span>{{ scope.row.conferenceStartAt }}-{{ scope.row.conferenceEndAt }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="venue" label="举办地点" show-overflow-tooltip />
    <el-table-column prop="applicationStatus" label="审核状态" show-overflow-tooltip />
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
  import { getAllApplications } from '../../../api/conferenceApi'
  import { ElMessage } from 'element-plus'
  import { events } from '../../../bus'
  const state = reactive({
    tableData: [],
    current: 1
  })
  const { tableData, current } = toRefs(state)

  onMounted(() => {
    fetchData()

    events.on('applyConference', () => {
      fetchData()
    })
  })

  onBeforeUnmount(() => {
    events.off('applyConference')
  })

  const fetchData = () => {
    getAllApplications().then((res: any) => {
      if (res.status === 200) {
        tableData.value = res.data.data
      }
    })
  }

  const handleCurrentChange = (newPage: any) => {
    current.value = newPage
  }
</script>
