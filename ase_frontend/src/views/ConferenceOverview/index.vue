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
  <el-dialog v-model="dialogFormVisible" title="论文投稿" width="50%">
    <el-form :model="form" :rules="paperRules" ref="paperFormRef">
      <el-form-item label="标题：" label-width="120px" prop="title">
        <el-input v-model="form.title" autocomplete="off" :maxlength="50" show-word-limit class="paper-input" />
      </el-form-item>
      <el-form-item label="会议名称：" label-width="120px" prop="conferenceName">
        <el-input v-model="form.conferenceName" disabled class="paper-input" />
      </el-form-item>
      <el-form-item label="作者：" label-width="120px" prop="realName">
        <el-input v-model="form.realName" autocomplete="off" class="paper-input" />
      </el-form-item>
      <el-form-item label="摘要：" label-width="120px" prop="abstractContent">
        <el-input v-model="form.abstractContent" autocomplete="off" type="textarea" :maxlength="800" show-word-limit />
      </el-form-item>
      <el-form-item label="文件：" label-width="120px" prop="essayId">
        <div style="width: 100%">
          <el-upload
            v-model="form.file"
            class="upload-paper"
            :action="fileUrl"
            method="post"
            :on-success="handleSuccess"
            :before-upload="beforeUpload"
            :on-remove="handleRemove"
            :limit="1">
            <el-button type="primary" color="#28b445">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">请上传pdf文件</div>
            </template>
          </el-upload>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" color="#28b445" @click="handleConfirm">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
  import { House } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  import { getAllConferences } from '../../api/conferenceApi'
  import { contributePaper } from '../../api/paperApi'
  import { getUsername } from '../../utils/auth'

  const paperFormRef = ref()

  function validateEssay(rule: any, value: any, callback: any) {
    if (value === '') {
      callback(new Error('文件不能为空，请确认文件是否上传完毕'))
    } else {
      callback()
    }
  }

  const state = reactive({
    tableData: [],
    fileUrl: import.meta.env.VITE_BASE_PATH + '/file/upload',
    dialogFormVisible: false,
    dialogVisible: false,
    form: {
      title: '',
      conferenceName: '',
      realName: '',
      abstractContent: '',
      essayId: '',
      file: []
    },
    paperRules: {
      title: [{ required: true, trigger: 'blur', message: '标题不能为空' }],
      conferenceName: [{ required: true, trigger: 'blur', message: '会议名不能为空' }],
      realName: [{ required: true, trigger: 'blur', message: '作者不能为空' }],
      abstractContent: [{ required: true, trigger: 'blur', message: '摘要不能为空' }],
      essayId: [{ required: true, trigger: 'blur', validator: validateEssay }]
    },
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
  const { tableData, fileUrl, dialogFormVisible, dialogVisible, form, paperRules, detailData, current } = toRefs(state)

  onMounted(() => {
    fetchConference()
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
    form.value.conferenceName = conferenceName
    dialogFormVisible.value = true
  }

  const handleCancel = () => {
    if (!paperFormRef.value) return
    paperFormRef.value.resetFields()
    dialogFormVisible.value = false
  }

  const handleConfirm = () => {
    if (!paperFormRef.value) return
    paperFormRef.value.validate((valid: boolean, fields: any) => {
      if (valid) {
        contributePaper({
          username: getUsername(),
          conferenceName: form.value.conferenceName,
          abstractContent: form.value.abstractContent,
          title: form.value.title,
          essayId: form.value.essayId,
          realName: form.value.realName
        }).then((res: any) => {
          if (res.status === 200) {
            ElMessage.success('论文投稿成功')
            handleCancel()
          }
        })
      } else {
        console.log('error submit!', fields)
      }
    })
  }

  const handleSuccess = (response: any, file: any, fileList: any) => {
    form.value.essayId = response.data
    form.value.file = fileList
  }

  const beforeUpload = (file: any) => {
    if(file.size >= 1048576 * 5){
      ElMessage.error('文件大小不得超过5MB')
      return false
    }

    if (file.type !== 'application/pdf') {
      ElMessage.error('只能上传 PDF 文件')
      return false
    }

    return true
  }

  const handleRemove = (file: any, fileList: any) => {
    form.value.file = fileList
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
