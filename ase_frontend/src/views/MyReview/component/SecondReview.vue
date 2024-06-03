<template>
  <div v-if="score === 0">
  <el-form :model="form" label-width="120px" :rules="reviewRules" ref="reviewFormRef">
    <el-form-item label="rebuttal">
      <el-input v-model="props.rebuttal" type="textarea" :rows="4" disabled/>
    </el-form-item>
    <el-form-item label="评分" prop="reviewScore">
      <el-rate
          v-model="form.reviewScore"
          :texts="['reject', 'weak reject', 'weak accept', 'accept']"
          show-text
          :max=4
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit" style="margin-left: 85%;">Review</el-button>
<!--      <el-button @click="handleClose">Cancel</el-button>-->
    </el-form-item>
  </el-form>
  </div>
  <div v-if="score !== 0">
    <el-form :model="form" label-width="120px" :rules="reviewRules">
      <el-form-item label="rebuttal">
        <el-input v-model="props.rebuttal" type="textarea" :rows="4" disabled/>
      </el-form-item>
      <el-form-item label="评分" prop="reviewScore">
        <el-rate
            v-model="score"
            :texts="['reject', 'weak reject', 'weak accept', 'accept']"
            show-text
            :max=4
            disabled
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import {secondReview} from "../../../api/reviewApi";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {getScore, toScore} from "../../../utils/const";

const props = defineProps({
  status: Number,
  score: Number,
  rebuttal: String,
  seq: Number,
  contributionId: String,
})
const emit = defineEmits(["getClose"]);

const reviewRules = {
  reviewScore: [{ required: true, trigger: 'blur', message: '请评分' }],
};

const state = reactive({
  form: {
    reviewScore: 0,
  },
  score: 0
})

const {form, score } = toRefs(state);
const reviewFormRef = ref()

const onSubmit = () =>{
  if (!reviewFormRef.value) return
  reviewFormRef.value.validate((valid: boolean, fields: any) => {
    if (valid) {
      secondReview({
        contributionId: props.contributionId,
        seq: props.seq,
        rebuttalScore: toScore(form.value.reviewScore),
      }).then((res: any) => {
        if(res.status == 200){
          ElMessage.success('提交复审结果成功')
          handleClose();
        }
      })
    }
  })
}

const handleClose = () => {
  if (!reviewFormRef.value) return
  reviewFormRef.value.resetFields()
  emit("getClose", false)
}

onMounted(() => {
  score.value = getScore(props.score)
 if(props.status === 100){
   form.value.reviewScore = props.score;
 }
})


</script>