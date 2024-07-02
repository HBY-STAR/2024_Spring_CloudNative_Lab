<template>
  <el-breadcrumb separator=">" class="paper-breadcrumb">
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/', query: { active: 'conference' } }">我的会议</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/conference-detail', query: { name: conferenceName } }">会议详情</el-breadcrumb-item>
    <el-breadcrumb-item>会议投稿</el-breadcrumb-item>
  </el-breadcrumb>
  <el-divider style="margin-top: 10px" />
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe>
    <el-table-column prop="title" label="论文标题" show-overflow-tooltip />
    <el-table-column prop="conferenceName" label="会议名称" show-overflow-tooltip />
    <el-table-column prop="time" label="投稿时间" show-overflow-tooltip />
    <el-table-column prop="status" label="审核状态" show-overflow-tooltip />
    <el-table-column label="操作" width="150">
      <template #default="scope">
        <el-button type="primary" size="small" color="#28b445" @click="goToDetail(scope.row.id)">详情</el-button>
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
  import { getConferencePapers } from '../../../api/paperApi'
  import { paperStatus } from '../../../utils/const'
  import { ElMessage } from 'element-plus'

  const router = useRouter()

  const state = reactive({
    conferenceName: '',
    tableData: [],
    current: 1
  })
  const { conferenceName, tableData, current } = toRefs(state)

  onMounted(() => {
    if (typeof router.currentRoute.value.query.name === 'string') {
      conferenceName.value = router.currentRoute.value.query.name || ''
    }

    fetchData()
  })

  const fetchData = () => {
    getConferencePapers(conferenceName.value).then((res: any) => {
      if (res.status === 200) {
        tableData.value = res.data.data
        tableData.value.map((v: any) => {
          v.status = paperStatus[v.status]
        })
      }
    })
  }

  const handleCurrentChange = (newPage: any) => {
    current.value = newPage
  }

  const goToDetail = (id: any) => {
    router.push({
      path: '/paper-detail',
      query: {
        id: id,
        level: 'conference',
        conferenceName: conferenceName.value
      }
    })
  }
</script>

<style scoped>
  .paper-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }
</style>
