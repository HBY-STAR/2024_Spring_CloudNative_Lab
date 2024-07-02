<template>
  <el-breadcrumb separator=">" class="paper-breadcrumb" v-if="paperLevel === 'person' || paperLevel === ''">
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/', query: { active: 'paper' } }">我的投稿</el-breadcrumb-item>
    <el-breadcrumb-item>投稿详情</el-breadcrumb-item>
  </el-breadcrumb>
  <el-breadcrumb separator=">" class="paper-breadcrumb" v-else>
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/', query: { active: 'conference' } }">我的会议</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/conference-detail', query: { name: conferenceName } }">会议详情</el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/conference-paper', query: { name: conferenceName } }">会议投稿</el-breadcrumb-item>
    <el-breadcrumb-item>投稿详情</el-breadcrumb-item>
  </el-breadcrumb>
  <el-divider style="margin-top: 10px" />
  <div class="description">
    <el-descriptions :title="paperDetail.title" :column="1" border size="large">
      <el-descriptions-item>
        <template #label>
          <div class="description-label">作者</div>
        </template>
        <div class="description-content">
          {{ paperDetail.realName }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">会议名称</div>
        </template>
        <div class="description-content">
          {{ paperDetail.conferenceName }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">摘要</div>
        </template>
        <div class="description-content">
          {{ paperDetail.abstractContent }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">文件</div>
        </template>
        <div class="description-content">
          <el-button type="primary" color="#28b445" @click="PreviewEssay(paperDetail.essayId)">查看文件</el-button>
          <el-button type="primary" color="#28b445" @click="DownloadEssay(paperDetail.essayId)">下载文件</el-button>
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">投稿时间</div>
        </template>
        <div class="description-content">
          {{ paperDetail.contributeTime }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">状态</div>
        </template>
        <div class="description-content">
          <el-tag>{{ paperDetail.status }}</el-tag>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script setup lang="ts">
  import { House } from '@element-plus/icons-vue'
  import { onMounted, reactive, toRefs } from 'vue'
  import { useRouter } from 'vue-router'
  import { getPaperDetail } from '../../../api/paperApi'
  import { paperStatus } from '../../../utils/const'

  const router = useRouter()

  const state = reactive({
    paperId: '',
    paperLevel: '',
    conferenceName: '',
    paperDetail: {
      id: -1,
      conferenceName: '---',
      author: '---',
      realName: '---',
      title: '---',
      abstractContent: '---',
      contributeTime: '---',
      essayId: '---',
      status: '---'
    }
  })
  const { paperId, paperLevel, conferenceName, paperDetail } = toRefs(state)

  onMounted(() => {
    if (typeof router.currentRoute.value.query.id === 'string') {
      paperId.value = router.currentRoute.value.query.id || ''
    }

    if (typeof router.currentRoute.value.query.level === 'string') {
      paperLevel.value = router.currentRoute.value.query.level || ''
    }

    if (router.currentRoute.value.query.conferenceName && typeof router.currentRoute.value.query.conferenceName === 'string') {
      conferenceName.value = router.currentRoute.value.query.conferenceName || ''
    }

    fetchData()
  })

  const fetchData = () => {
    getPaperDetail(paperId.value).then((res: any) => {
      if (res.status === 200) {
        paperDetail.value = res.data.data
        paperDetail.value.status = paperStatus[paperDetail.value.status]
      }
    })
  }

  const PreviewEssay = (essayId: any) => {
    window.open(import.meta.env.VITE_BASE_PATH + `/file/preview/${essayId}`)
  }

  const DownloadEssay = (essayId: any) => {
    window.open(import.meta.env.VITE_BASE_PATH + `/file/download/${essayId}`)
  }
</script>

<style scoped>
  .paper-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }

  .description {
    padding-left: 15%;
    padding-right: 15%;
    max-height: 550px;
    overflow-y: auto;
  }

  .description-label {
    text-align: center;
    min-height: 40px;
    min-width: 120px;
    font-weight: 700;
    line-height: 40px;
  }

  .description-content {
    min-height: 40px;
    line-height: 40px;
  }
</style>
<style>
  .el-descriptions__table.is-bordered .el-descriptions__cell {
    /* padding: 30px 11px; */
    min-width: 120px;
    /* max-width: 80%; */
    word-break: break-all;
    word-wrap: break-word;
  }
</style>
