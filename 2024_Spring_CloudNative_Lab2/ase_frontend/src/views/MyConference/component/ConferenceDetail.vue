<template>
  <!-- 查看会议详细信息、开启投稿（chair）、根据姓名查询用户&邀请PCMember（chair）、发送的所有邀请（chair）、查看该会议的所有投稿（chair\PCMember） -->
  <el-breadcrumb separator=">" class="conference-breadcrumb">
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item :to="{ path: '/', query: { active: 'conference' } }">我的会议</el-breadcrumb-item>
    <el-breadcrumb-item>会议详情</el-breadcrumb-item>
  </el-breadcrumb>
  <el-divider style="margin-top: 10px" />
  <div class="operation">
    <div style="font-weight: bold; font-size: 20px">当前您的身份是{{ role }}</div>
    <!-- chair/PCMember/Author/Tourist -->
    <div>
      <SubmissionStart :conferenceName="conferenceName" v-if="role.includes('CHAIR')"></SubmissionStart>
      <PCMemberInvite :conferenceName="conferenceName" v-if="role.includes('CHAIR')"></PCMemberInvite>
      <el-button type="primary" color="#28b445" @click="goToPaperDetail" v-if="role.includes('CHAIR') || role.includes('PC_MEMBER') || role.includes('AUTHOR')">
        查看投稿
      </el-button>
    </div>
  </div>
  <div class="description">
    <el-descriptions :column="1" border size="large">
      <el-descriptions-item>
        <template #label>
          <div class="description-label">会议名称</div>
        </template>
        <div class="description-content">
          {{ conferenceDetail.conferenceName }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">会议简称</div>
        </template>
        <div class="description-content">
          {{ conferenceDetail.conferenceAbbr }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">举办时间</div>
        </template>
        <div class="description-content">
          {{ conferenceDetail.conferenceStartAt + '-' + conferenceDetail.conferenceEndAt }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">举办地点</div>
        </template>
        <div class="description-content">
          {{ conferenceDetail.venue }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">投稿截止时间</div>
        </template>
        <div class="description-content">
          {{ conferenceDetail.submissionDeadline ?? '---' }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">评审结果发布时间</div>
        </template>
        <div class="description-content">
          {{ conferenceDetail.reviewResultAnnounceAt ?? '---' }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="description-label">状态</div>
        </template>
        <div class="description-content">
          <el-tag>{{ conferenceDetail.conferenceStatus }}</el-tag>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script setup lang="ts">
  import { House } from '@element-plus/icons-vue'
  import { onMounted, reactive, toRefs } from 'vue'
  import SubmissionStart from '@/views/MyConference/component/SubmissionStart.vue'
  import PCMemberInvite from './PCMemberInvite.vue'
  import { useRouter } from 'vue-router'
  import { handleConferenceRole } from '../../../api/userApi'
  import { ElMessage } from 'element-plus'
  import { getConferenceDetail, getMyConferenceRole } from '../../../api/conferenceApi'
  import { events } from '../../../bus'

  const router = useRouter()

  const state = reactive({
    conferenceName: '',
    role: '',
    conferenceDetail: {
      conferenceName: '---',
      conferenceAbbr: '---',
      conferenceStartAt: '---',
      conferenceEndAt: '---',
      venue: '---',
      submissionDeadline: '---',
      reviewResultAnnounceAt: '---',
      conferenceStatus: '---'
    }
  })
  const { conferenceName, conferenceDetail, role } = toRefs(state)

  onMounted(() => {
    if (typeof router.currentRoute.value.query.name === 'string') {
      conferenceName.value = router.currentRoute.value.query.name || ''
    }

    getRole()
    if (role.value === '') role.value = 'tourist'

    getDetail()

    events.on('startSubmission', () => {
      getDetail()
    })
  })

  onBeforeUnmount(() => {
    events.off('startSubmission')
  })

  const getRole = () => {
    getMyConferenceRole(conferenceName.value).then((res: any) => {
      if (res.status === 200) {
        role.value = (res.data.data ?? []).join(',')
      }
    })
  }

  const getDetail = () => {
    getConferenceDetail(conferenceName.value).then((res: any) => {
      if (res.status === 200) {
        conferenceDetail.value = res.data.data
      }
    })
  }

  const goToPaperDetail = () => {
    router.push({ path: '/conference-paper', query: { name: conferenceName.value } })
  }
</script>
<style scoped>
  .conference-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }

  .operation {
    display: flex;
    justify-content: space-between;
    padding-left: 15%;
    padding-right: 15%;
    margin-bottom: 40px;
    margin-top: 30px;
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
    min-width: 200px;
    text-align: center;
  }
</style>
<style>
  .el-descriptions__table.is-bordered .el-descriptions__cell {
    min-width: 120px;
    word-break: break-all;
    word-wrap: break-word;
  }
</style>
