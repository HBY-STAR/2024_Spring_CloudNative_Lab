<template>
  <el-button type="primary" color="#28b445" @click="visible = true" style="margin-right: 20px">开启投稿</el-button>
  <el-dialog v-model="visible" title="开启投稿" width="50%" style="top: -30px" @close="handleCancel">
    <el-form :model="form" :rules="rules" ref="paperFormRef" style="width: 80%; margin-left: 10%">
      <el-form-item label="会议名称：" label-width="150px" prop="name">
        <span>{{ props.conferenceName }}</span>
      </el-form-item>
      <el-form-item label="投稿截止时间：" label-width="150px" prop="submissionDeadline">
        <el-date-picker v-model="form.submissionDeadline" placeholder="请选择投稿截止时间" type="datetime" format="YYYY-MM-DD HH:mm:ss" />
      </el-form-item>
      <el-form-item label="评审结果发布时间：" label-width="150px" prop="reviewResultAnnounceAt">
        <el-date-picker v-model="form.reviewResultAnnounceAt" placeholder="请选择评审结果发布时间" type="datetime" format="YYYY-MM-DD HH:mm:ss" />
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
  import { ElMessage, dayjs } from 'element-plus'
  import { startSubmiission } from '../../../api/conferenceApi'
  import { events } from '../../../bus'
  const paperFormRef = ref()

  const props = defineProps({
    conferenceName: String
  })

  const state = reactive({
    form: {
      submissionDeadline: '',
      reviewResultAnnounceAt: ''
    },
    rules: {
      submissionDeadline: [{ required: true, trigger: 'blur', message: '投稿截止时间不能为空' }],
      reviewResultAnnounceAt: [{ required: true, trigger: 'blur', message: '评审结果发布时间不能为空' }]
    },
    visible: false
  })
  const { form, rules, visible } = toRefs(state)

  const handleCancel = () => {
    visible.value = false
    if (!paperFormRef.value) return
    paperFormRef.value.resetFields()
  }

  const handleConfirm = () => {
    if (!paperFormRef.value) return
    paperFormRef.value.validate((valid: boolean, fields: any) => {
      if (valid) {
        startSubmiission({
          conferenceName: props.conferenceName,
          submissionDeadline: dayjs(form.value.submissionDeadline).format('YYYY-MM-DD' + 'T' + 'HH:mm:ss'),
          reviewResultAnnounceAt: dayjs(form.value.reviewResultAnnounceAt).format('YYYY-MM-DD' + 'T' + 'HH:mm:ss')
        }).then((res: any) => {
          if (res.status === 200) {
            ElMessage.success('开启投稿成功')
            events.emit('startSubmission')
            handleCancel()
          }
        })
      } else {
        console.log('error submit!', fields)
      }
    })
  }
</script>
