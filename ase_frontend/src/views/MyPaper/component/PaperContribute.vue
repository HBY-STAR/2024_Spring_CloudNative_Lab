<template>
  <el-dialog title="论文投稿" width="50%" v-model="open" @close="handleCancel">
    <el-form :model="form" :rules="paperRules" ref="paperFormRef">
      <el-form-item label="标题：" label-width="120px" prop="title">
        <el-input v-model="form.title" autocomplete="off" :maxlength="50" show-word-limit class="paper-input" />
      </el-form-item>
      <el-form-item label="会议名称：" label-width="120px" prop="conferenceName">
        <el-input v-model="form.conferenceName" disabled class="paper-input" />
      </el-form-item>
      <el-form-item label="会议topic" label-width="120px" prop="topics">
        <el-checkbox-group v-model="form.topics">
            <el-checkbox  v-for="topic in topics" :label="topic" />
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="投稿人：" label-width="120px" prop="realName">
        <el-input v-model="form.realName" autocomplete="off" class="paper-input" />
      </el-form-item>
<hr style="margin-bottom: 8px; border-top: #807f7f">

      <el-row v-for="(writer, index) in form.writers" :key="index" style="margin-left: 35px">
        <el-form-item label="作者姓名：" :prop="'writers[' + index + '].name'" :rules="nameRule">
          <el-input v-model="writer.name" autocomplete="off" class="paper-input" />
        </el-form-item>
        <el-form-item label="所属单位：" :prop="'writers[' + index + '].institutionName' " :rules="instRule">
          <el-input v-model="writer.institutionName" autocomplete="off" class="paper-input" />
        </el-form-item>
        <el-form-item label="所属地区：" :prop="'writers[' + index + '].area' " :rules="areaRule">
          <el-input v-model="writer.area" autocomplete="off" class="paper-input" />
        </el-form-item>
        <el-form-item label="电子邮箱：" :prop="'writers[' + index + '].email' " :rules="emailRule">
          <el-input v-model="writer.email" autocomplete="off" class="paper-input" />
        </el-form-item>
        <el-button type="danger" size="small" plain @click="removeRow(index)" class="form-button">
          删除</el-button>
      </el-row>
      <el-button type="primary" size="small" plain @click="addRow()" style="margin-left: 80%; margin-bottom: 10px">
        新增作者</el-button>
      <hr style="margin-bottom: 8px; border-top: #807f7f">
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
  import { ElMessage } from 'element-plus'
  import { contributePaper, reContribute, getPaperDetail } from '../../../api/paperApi'
  import { getUsername } from '../../../utils/auth';
  import {reactive, ref, toRefs} from "vue";
  import {getAllTopics} from "../../../api/conferenceApi";
  const props = defineProps({
    conferenceName: String,
    id: String
  })
  const emit = defineEmits(["getClose"]);
  const paperFormRef = ref()

  function validateEssay(rule: any, value: any, callback: any) {
    if (value === '') {
      callback(new Error('文件不能为空，请确认文件是否上传完毕'))
    } else {
      callback()
    }
  }
  const validateEmail = (rule: any, value: any, callback: any) => {
    // 1.使用雷·汤姆林森创立的标准E-mail格式，即用户标识符+ @ + 域名
    const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
    if (value === '') {
      callback(new Error('作者邮箱不能为空'))
    } else if (!emailPattern.test(value)) {
      callback(new Error('请填写合法的邮箱'))
    } else {
      callback()
    }
  }
  const emailRule = [{required: true, trigger: 'blur', validator: validateEmail}]
  const nameRule = [{ required: true, trigger: 'blur', message: '作者姓名不能为空' }]
  const instRule = [{ required: true, trigger: 'blur', message: '作者所属单位不能为空' }]
  const areaRule = [{ required: true, trigger: 'blur', message: '作者所属地区不能为空' }]

  const state = reactive({
    fileUrl: import.meta.env.VITE_BASE_PATH + '/file/upload',
    form: {
      id: '',
      title: '',
      conferenceName: '',
      realName: '',
      topics: [],
      writers: [{
        name: '',
        institutionName: '',
        area: '',
        email: ''
      }],
      abstractContent: '',
      essayId: '',
      file: []
    },
    paperRules: {
      title: [{ required: true, trigger: 'blur', message: '标题不能为空' }],
      conferenceName: [{ required: true, trigger: 'blur', message: '会议名不能为空' }],
      realName: [{ required: true, trigger: 'blur', message: '投稿者真实姓名不能为空' }],
      topics: [{required: true, trigger: 'blur', message: '请至少添加一个topic'}],
      abstractContent: [{ required: true, trigger: 'blur', message: '摘要不能为空' }],
      essayId: [{ required: true, trigger: 'blur', validator: validateEssay }]
    },
    writerVisible: true,
    topics: [],
    isFirst: true,
    open: true
  })
  const { fileUrl, form, paperRules, writerVisible, topics, isFirst, open } = toRefs(state)

  onMounted(() => {
    fetchTopics(props.conferenceName);
    if(props.id){
      console.log(props.id)
      isFirst.value = false;
      fetchData(props.id || '');
    }
  })

  const fetchData = (contributionId: string) => {
    getPaperDetail(contributionId).then((res: any) => {
      form.value.writers = res.data.data.writers
      form.value.id = res.data.data.id
      form.value.conferenceName = res.data.data.conferenceName
      form.value.abstractContent = res.data.data.abstractContent
      form.value.realName = res.data.data.author
      form.value.title = res.data.data.title
      form.value.topics = res.data.data.topics
    })
  }

  const fetchTopics = (name: string) => {
    // topics.value = ['随便吧', '毁灭吧', '爱谁谁', '随便吧', '毁灭吧', '爱谁谁']
    form.value.conferenceName = name;
    getAllTopics(name).then((res: any) => {
      topics.value = res.data.data
    })
  }

  const addRow = () => {
    form.value.writers.push({
      name: '',
      institutionName: '',
      area: '',
      email: '',
    });
    writerVisible.value = false;
  }

  const removeRow = (index: number) => {
    if(form.value.writers.length === 1){
      ElMessage.error('请至少添加一位作者')
      return
    }
    form.value.writers.splice(index, 1);
  }

  const handleCancel = () => {
    if (!paperFormRef.value) return
    paperFormRef.value.resetFields()
    open.value = false
    emit("getClose", open)
  }

  const handleConfirm = () => {
    //console.log(form.value.topics)
    if (!paperFormRef.value) return
    paperFormRef.value.validate((valid: boolean, fields: any) => {
      if (valid) {
        if(isFirst.value){
          contributePaper({
            username: getUsername(),
            conferenceName: form.value.conferenceName,
            abstractContent: form.value.abstractContent,
            title: form.value.title,
            essayId: form.value.essayId,
            realName: form.value.realName,
            writers: form.value.writers,
            topics: form.value.topics,
          }).then((res: any) => {
            if (res.status === 200) {
              ElMessage.success('论文投稿成功')
              handleCancel()
            }
          })
        }else{
          reContribute({
            username: getUsername(),
            id: form.value.id,
            conferenceName: form.value.conferenceName,
            abstractContent: form.value.abstractContent,
            title: form.value.title,
            essayId: form.value.essayId,
            realName: form.value.realName,
            writers: form.value.writers,
            topics: form.value.topics,
          }).then((res: any) => {
            if (res.status === 200) {
              ElMessage.success('论文重投成功')
              handleCancel()
            }
          })
        }

      } else {
        console.log(form.value.writers)
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
.paper-input {
  width: 50%;
}
.form-button{
  margin-bottom: 10px;
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