<template>
  <el-breadcrumb separator=">" class="conference-breadcrumb">
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item>我的会议</el-breadcrumb-item>
  </el-breadcrumb>
  <el-divider style="margin-top: 10px" />
  <el-button type="primary" color="#28b445" style="float: right; position: relative; z-index: 999" @click="dialogFormVisible = true">申请会议</el-button>
  <el-tabs v-model="activeTabName" type="card" class="tab-conference">
    <el-tab-pane label="我的申请" name="1"><Application></Application></el-tab-pane>
    <el-tab-pane label="我的会议" name="2"><Conference /></el-tab-pane>
  </el-tabs>
  <el-dialog v-model="dialogFormVisible" title="会议申请" width="50%" style="top: -30px">
    <el-form :model="form" :rules="rules" ref="conferenceFormRef" style="width: 80%; margin-left: 10%">
      <el-form-item label="会议全名：" label-width="120px" prop="conferenceName">
        <el-input v-model="form.conferenceName" autocomplete="off" class="conference-input" />
      </el-form-item>
      <el-form-item label="会议简称：" label-width="120px" prop="conferenceAbbr">
        <el-input v-model="form.conferenceAbbr" autocomplete="off" class="conference-input" />
      </el-form-item>
      <el-form-item label="会议topic: " label-width="120px" prop="topics">
        <el-tag
            v-for="(topic, index) in form.topics" class="mx-1" closable :disable-transitions="false"
            @close="handleClose(topic)" style="margin-right: 5px">
          <el-form-item :prop="'topics[' + index + ']'" >{{ topic }}</el-form-item>
        </el-tag>
        <el-input v-if="inputVisible" ref="InputRef" v-model="inputValue" class="ml-1 w-20" size="small"
            @keyup.enter="handleInputConfirm" @blur="handleInputConfirm" style="width: 20%"/>
        <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
          + New Tag
        </el-button>
      </el-form-item>
      <el-form-item label="举办时间：" label-width="120px" prop="date">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          format="YYYY-MM-DD HH:mm:ss"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          @change="handleDatePick" />
      </el-form-item>
      <el-form-item label="举办地点：" label-width="120px" prop="venue">
        <el-input v-model="form.venue" autocomplete="off" class="conference-input" />
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
  import Conference from './component/Conference.vue'
  import Application from './component/Application.vue'
  import {ElMessage, dayjs, ElInput} from 'element-plus'
  import { applyConference } from '../../api/conferenceApi'
  import { events } from '../../bus'

  const conferenceFormRef = ref()

  function validateDate(rule: any, value: any, callback: any) {
    if (form.value.conferenceStartAt === '' || form.value.conferenceStartAt === null) {
      callback(new Error('举办时间不能为空'))
    } else {
      callback()
    }
  }
  const state = reactive({
    activeTabName: '1',
    dialogFormVisible: false,
    form: {
      conferenceName: '',
      conferenceAbbr: '',
      topics: [],
      conferenceStartAt: '',
      conferenceEndAt: '',
      venue: ''
    },
    rules: {
      conferenceName: [{ required: true, trigger: 'blur', message: '标题不能为空' }],
      conferenceAbbr: [{ required: true, trigger: 'blur', message: '会议名不能为空' }],
      topics: [{required: true, trigger: 'blur', message: '请至少添加一个topic'}],
      date: [{ required: true, trigger: 'blur', validator: validateDate }],
      venue: [{ required: true, trigger: 'blur', message: '摘要不能为空' }]
    },
    dateRange: '',
    inputValue: '',
    inputVisible: false
  })
  const { activeTabName, dialogFormVisible, form, rules, dateRange, inputVisible, inputValue } = toRefs(state)

  const handleDatePick = (value: any) => {
    if (value !== null) {
      form.value.conferenceStartAt = dayjs(value[0]).format('YYYY-MM-DD' + 'T' + 'HH:mm:ss')
      form.value.conferenceEndAt = dayjs(value[1]).format('YYYY-MM-DD' + 'T' + 'HH:mm:ss')
    } else {
      form.value.conferenceStartAt = ''
      form.value.conferenceEndAt = ''
    }
  }

  const handleCancel = () => {
    if (!conferenceFormRef.value) return
    conferenceFormRef.value.resetFields()
    dialogFormVisible.value = false
  }

  const handleConfirm = () => {
    if (!conferenceFormRef.value) return
    conferenceFormRef.value.validate((valid: boolean, fields: any) => {
      if (valid) {
        applyConference(form.value).then((res: any) => {
          if (res.status === 200) {
            ElMessage.success('申请会议成功')
            events.emit('applyConference')
            handleCancel()
          }
        })
      } else {
        console.log('error submit!', fields)
      }
    })
  }

  const InputRef = ref<InstanceType<typeof ElInput>>()

  const handleClose = (tag: string) => {
    form.value.topics.splice(form.value.topics.indexOf(tag), 1)
  }

  const showInput = () => {
    inputVisible.value = true
    nextTick(() => {
      InputRef.value!.input!.focus()
    })
  }

  const handleInputConfirm = () => {
    if (inputValue.value) {
      form.value.topics.push(inputValue.value)
    }
    inputVisible.value = false
    inputValue.value = ''
  }
</script>

<style scoped>
  .conference-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }
</style>

<style>
  .el-tabs__item.is-active {
    color: var(--color-theme) !important;
  }

  .el-tabs__item:hover {
    color: var(--color-theme) !important;
  }

  .el-tabs__header {
    border-bottom: 0px !important;
  }
</style>
