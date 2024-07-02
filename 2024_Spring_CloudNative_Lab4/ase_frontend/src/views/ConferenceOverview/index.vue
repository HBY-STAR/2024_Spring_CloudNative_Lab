<template>
  <div style="display: flex; justify-content: space-between">
    <el-breadcrumb separator=">" class="overview-breadcrumb">
      <el-breadcrumb-item>
        <el-icon><House /></el-icon>
      </el-breadcrumb-item>
      <el-breadcrumb-item>会议概览</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <el-divider style="margin-top: 10px" />
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe>
    <el-table-column prop="conferenceName" label="会议名称" show-overflow-tooltip>
      <template #default="scope">
        <el-button link type="primary" @click="handleDetail(scope.row)">
          <span style="color: #61ac85">
            {{ scope.row.conferenceName }}
          </span>
        </el-button>
      </template>
    </el-table-column>
    <el-table-column prop="conferenceAbbr" label="会议简称" show-overflow-tooltip />
    <el-table-column label="举办时间" show-overflow-tooltip>
      <template #default="scope">
        <span>{{ scope.row.conferenceStartAt }}-{{ scope.row.conferenceEndAt }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="venue" label="举办地点" show-overflow-tooltip />
    <el-table-column prop="conferenceStatus" label="会议状态" show-overflow-tooltip />
    <el-table-column label="操作" width="150">
      <template #default="scope">
        <el-button
          v-if="scope.row.conferenceStatus === '投稿中'"
          type="primary"
          size="small"
          color="#28b445"
          @click="handleSubmission(scope.row.conferenceName)">
          投稿
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
  <PaperContribute :conference-name="selectedConference" v-if="formVisible"  @getClose="getClose"/>
  <el-dialog v-model="dialogVisible" title="会议详情" width="50%" style="top: -50px">
    <div class="detail-description">
      <el-descriptions :column="1" border size="large">
        <el-descriptions-item>
          <template #label>
            <div class="description-label">会议名称</div>
          </template>
          <div class="description-content">
            {{ detailData.conferenceName }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">会议简称</div>
          </template>
          <div class="description-content">
            {{ detailData.conferenceAbbr }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">举办时间</div>
          </template>
          <div class="description-content">
            {{ detailData.conferenceStartAt + '-' + detailData.conferenceEndAt }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">举办地点</div>
          </template>
          <div class="description-content">
            {{ detailData.venue }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">投稿截止时间</div>
          </template>
          <div class="description-content">
            {{ detailData.submissionDeadline }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">评审结果发布时间</div>
          </template>
          <div class="description-content">
            {{ detailData.reviewResultAnnounceAt }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">状态</div>
          </template>
          <div class="description-content">
            <el-tag>{{ detailData.conferenceStatus }}</el-tag>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" color="#28b445" @click="handleDetailClose">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
  import { House } from '@element-plus/icons-vue'
  import { getAllConferences } from '../../api/conferenceApi'
  import PaperContribute from "../MyPaper/component/PaperContribute.vue";

  const state = reactive({
    tableData: [],
    dialogVisible: false,
    formVisible: false,
    selectedConference: '',
    detailData: {
      conferenceName: '---',
      conferenceAbbr: '---',
      conferenceStartAt: '',
      conferenceEndAt: '',
      venue: '---',
      submissionDeadline: '---',
      reviewResultAnnounceAt: '---',
      conferenceStatus: '---'
    },
    current: 1
  })
  const { tableData, dialogVisible, detailData, current, selectedConference, formVisible } = toRefs(state)

  onMounted(() => {
   fetchConference()
   //  tableData.value = [{
   //    conferenceName: 'test',
   //    conferenceStatus: '投稿中'
   //  }]
  })

  const fetchConference = () => {
    getAllConferences().then((res: any) => {
      if (res.status === 200) {
        tableData.value = res.data.data
      }
    })
  }

  const handleCurrentChange = (newPage: any) => {
    current.value = newPage
  }

  const handleDetail = (rowData: any) => {
    detailData.value = rowData
    dialogVisible.value = true
  }

  const handleDetailClose = () => {
    detailData.value = {
      conferenceName: '---',
      conferenceAbbr: '---',
      conferenceStartAt: '',
      conferenceEndAt: '',
      venue: '---',
      submissionDeadline: '---',
      reviewResultAnnounceAt: '---',
      conferenceStatus: '---'
    }
    dialogVisible.value = false
  }

  const handleSubmission = (conferenceName: string) => {
    selectedConference.value = conferenceName;
    formVisible.value = true;

  }

  const getClose = (value: boolean) => {
    formVisible.value = value;
  }

</script>

<style scoped>
  .overview-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }

  .detail-item {
    margin-bottom: 10px;
    font-size: 17px;
  }

  .paper-input {
    width: 50%;
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
  .el-pager li.is-active {
    color: var(--color-theme) !important;
  }
  .el-pager li:hover {
    color: var(--color-theme) !important;
  }
  .el-descriptions__table.is-bordered .el-descriptions__cell {
    min-width: 120px;
    word-break: break-all;
    word-wrap: break-word;
  }
</style>
