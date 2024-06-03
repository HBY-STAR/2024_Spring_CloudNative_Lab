<template>
  <el-button type="primary" color="#28b445" @click="visible = true" style="margin-right: 20px">开启审稿</el-button>
  <el-dialog v-model="visible" title="开启审稿" width="50%" style="top: -30px" @close="handleCancel">
    <el-form :model="form" :rules="rules" ref="paperFormRef" style="width: 80%; margin-left: 10%">
      <el-form-item label="稿件分配策略：" label-width="150px" prop="strategy">
        <el-select v-model="form.strategy" class="m-2" placeholder="请选择稿件分配策略">
          <el-option
              v-for="item in options"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
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
import { startReview } from '../../../api/reviewApi'
const paperFormRef = ref()

const props = defineProps({
  conferenceName: String
})

const options = [{
  label: '基于topic相关度',
  value: 1
},
  {
    label: '基于审稿平均负担',
    value: 2
  }]

const state = reactive({
  form: {
    strategy: 0,
  },
  rules: {
    strategy: [{ required: true, trigger: 'blur', message: '请选择稿件分配策略' }],
  },
  visible: false
})
const { form, rules, visible} = toRefs(state)

const handleCancel = () => {
  visible.value = false
  if (!paperFormRef.value) return
  paperFormRef.value.resetFields()
}

const handleConfirm = () => {
  if (!paperFormRef.value) return
  paperFormRef.value.validate((valid: boolean, fields: any) => {
    if (valid) {
      startReview({
        conferenceName: props.conferenceName,
        reviewStrategy: form.value.strategy,
      }).then((res: any) => {
        if (res.status === 200) {
          ElMessage.success('开启审稿成功')
          handleCancel()
        }
      })
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>
