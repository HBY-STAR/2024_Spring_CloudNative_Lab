<template>
  <div style="display: flex; justify-content: space-between">
    <el-breadcrumb separator=">" class="overview-breadcrumb">
      <el-breadcrumb-item>
        <el-icon><House /></el-icon>
      </el-breadcrumb-item>
      <el-breadcrumb-item>我的审稿</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <el-divider style="margin-top: 10px" />
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe>
    <el-table-column prop="title" label="论文标题" show-overflow-tooltip/>
    <el-table-column prop="conferenceName" label="会议名称" show-overflow-tooltip/>
    <el-table-column label="审稿状态" show-overflow-tooltip >
      <template #default="scope">
        {{reviewStatus(scope.row.reviewRebuttal.status)}}
      </template>
    </el-table-column>
    <el-table-column label="操作" width="150">
      <template #default="scope">
        <el-button
            v-if="scope.row.reviewRebuttal.status === 0"
            type="primary"
            size="small"
            color="#28b445"
            @click="handleReview(scope.row)">
          审稿
        </el-button>
        <el-button
            v-else-if="scope.row.reviewRebuttal.status === 2"
            type="primary"
            size="small"
            color="#28b445"
            @click="handleReview(scope.row)">
          复审
        </el-button>
        <el-button
          v-else
          type="primary"
          size="small"
          color="#28b445"
          @click="handleReview(scope.row)">
        查看详情
      </el-button>
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

  <el-dialog v-if="dialogVisible" v-model="dialogVisible" title="审稿" width="50%" style="top: -50px">
    <div class="detail-description">
      <el-descriptions :column="1" border size="large">
        <el-descriptions-item>
          <template #label>
            <div class="description-label">论文标题</div>
          </template>
          <div class="description-content">
            {{ title }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
        <template #label>
          <div class="description-label">会议名称</div>
        </template>
        <div class="description-content">
          {{ conferenceName }}
        </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">摘要</div>
          </template>
          <div class="description-content">
            {{ abstractContent }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">相关topic</div>
          </template>
          <div class="description-content">
            {{ topics }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="description-label">文件</div>
          </template>
          <div class="description-content">
            <el-button type="primary" color="#28b445" @click="PreviewEssay()">查看文件</el-button>
            <el-button type="primary" color="#28b445" @click="DownloadEssay()">下载文件</el-button>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <el-tabs v-model="activeTabName" type="card" class="tab-review">
      <el-tab-pane label="一审" name="1"><FirstReview :status="activeReview.status" :score="activeReview.reviewScore"
      :comment="activeReview.reviewComment" :confidence="activeReview.reviewConfidence"
      :seq="activeReview.seq" :contributionId="activeReview.contributionId" @getClose="getClose"></FirstReview></el-tab-pane>
      <el-tab-pane label="复审" name="2" :disabled="activeReview.status !== 2 && activeReview.status !== 100">
        <SecondReview :status="activeReview.status" :score="activeReview.rebuttalScore" :rebuttal="activeReview.rebuttalMessage"
        :seq="activeReview.seq" :contributionId="activeReview.contributionId" @getClose="getClose"/></el-tab-pane>
    </el-tabs>

  </el-dialog>
</template>

<script setup lang="ts">
import { House } from '@element-plus/icons-vue'
import { getAllReviews } from '../../api/reviewApi'
import FirstReview from './component/FirstReview.vue'
import SecondReview from './component/SecondReview.vue'
import {reviewStatus} from "../../utils/const";

const state = reactive({
  tableData: [],
  activeTabName: '1',
  dialogVisible: false,
  activeReview: {
    contributionId: '',
    essayId: '',
    seq: 0,
    reviewerUsername: '',
    status: -1,
    reviewScore: 0,
    reviewConfidence: -1,
    reviewComment: '',
    rebuttalMessage: '',
    rebuttalScore: 0,
  },
  title:'',
  abstractContent: '',
  essayId: '',
  conferenceName: '',
  topics: '',
  current: 1
})
const { tableData, current, activeReview, activeTabName, dialogVisible, conferenceName,essayId, abstractContent, title, topics } = toRefs(state)

onMounted(() => {
  fetchReviews();
  //  tableData.value = [{
  //    contributionId: "111",
  //    essayId: "string",
  //    username: "string",
  //    realName: "string",
  //    conferenceName: "会议名称1",
  //    title: "论文标题1",
  //    abstractContent: "摘要1",
  //    topics: [
  //      'string', 'number'
  //    ],
  //    reviewRebuttal: {
  //      contributionId: 0,
  //      seq: 1,
  //      essayId: "string",
  //      reviewerUsername: "string",
  //      status: 0,
  //      reviewScore: null,
  //      reviewComment: "",
  //      rebuttalMessage: "",
  //      rebuttalScore: 0,
  //      reviewConfidence: null
  //    }
  //  },
  //    {
  //      contributionId: "222",
  //      essayId: "string",
  //      username: "string",
  //      realName: "string",
  //      conferenceName: "会议名称2",
  //      title: "论文标题2",
  //      abstractContent: "摘要2",
  //      topics: [
  //        "string"
  //      ],
  //      reviewRebuttal: {
  //        contributionId: 0,
  //        seq: 1,
  //        essayId: "string",
  //        reviewerUsername: "string",
  //        status: 100,
  //        reviewScore: -1,
  //        reviewComment: "string",
  //        rebuttalMessage: "rebuttal",
  //        rebuttalScore: 2,
  //        reviewConfidence: 0
  //      }
  //    }]
})

const fetchReviews = () => {
  getAllReviews().then((res: any) => {//改接口
    if (res.status === 200) {
      tableData.value = res.data.data
    }
  })
}

const handleCurrentChange = (newPage: any) => {
  current.value = newPage
}

const handleReview = (selected: any) => {
  const activeIndex = tableData.value.indexOf(selected);
  dialogVisible.value= true;
  activeReview.value = tableData.value[activeIndex].reviewRebuttal!;

  conferenceName.value = tableData.value[activeIndex].conferenceName;
  title.value = tableData.value[activeIndex].title;
  abstractContent.value = tableData.value[activeIndex].abstractContent;
  essayId.value = tableData.value[activeIndex].essayId;
  topics.value = tableData.value[activeIndex].topics.join(', ')

  if(activeReview.value.status === 2){
    activeTabName.value = '2';
  }
  else{
    activeTabName.value = '1';
  }
}

const PreviewEssay = () => {
  window.open(import.meta.env.VITE_BASE_PATH + `/file/preview/${essayId.value}`)
}

const DownloadEssay = () => {
  window.open(import.meta.env.VITE_BASE_PATH + `/file/download/${essayId.value}`)
}

const getClose = (value: boolean) => {
  console.log(value);
  dialogVisible.value = false;
}

</script>

<style scoped>
.overview-breadcrumb {
  font-size: 16px;
  line-height: 32px;
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
.tab-review{
  margin-top: 20px;
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
