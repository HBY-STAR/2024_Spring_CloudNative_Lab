<template>
  <div v-if="props.status === 0">
  <el-form :model="form" label-width="120px" :rules="reviewRules" ref="reviewFormRef">
    <el-form-item label="评分" prop="reviewScore">
      <el-rate
          v-model="form.reviewScore"
          :texts="['reject', 'weak reject', 'weak accept', 'accept']"
          show-text
          :max=4
      />
    </el-form-item>
    <el-form-item label="Confidence" prop="reviewConfidence">
      <el-select v-model="form.reviewConfidence" class="m-2" placeholder="Select confidence">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="评语" prop="reviewComment">
      <el-input v-model="form.reviewComment" type="textarea" :rows="3" maxlength="800"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit" style="margin-left: 85%;">Review</el-button>
<!--      <el-button @click="handleClose">Cancel</el-button>-->
    </el-form-item>
  </el-form>
  </div>

  <div v-if="props.status !== 0">
    <el-form :model="form" label-width="120px" :rules="reviewRules">
      <el-form-item label="评分" prop="reviewScore">
        <el-rate
            v-model="score"
            :texts="['reject', 'weak reject', 'weak accept', 'accept']"
            show-text
            :max=4
            disabled
        />
      </el-form-item>
      <el-form-item label="Confidence" prop="reviewConfidence">
        <el-select v-model="props.confidence" class="m-2" placeholder="Select confidence" disabled>
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="评语" prop="reviewComment">
        <el-input v-model="props.comment" type="textarea" :rows="3" maxlength="800" disabled/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import {fistReview} from "../../../api/reviewApi";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {getScore, toScore} from "../../../utils/const";

const props = defineProps({
  status: Number,
  score: Number,
  confidence: Number,
  comment: String,
  seq: Number,
  contributionId: String,
})
const emit = defineEmits(["getClose"]);

const reviewRules = {
  reviewScore: [{ required: true, trigger: 'blur', message: '请评分' }],
  reviewConfidence: [{ required: true, trigger: 'blur', message: '请选择confidence' }],
  reviewComment: [{ required: true, trigger: 'blur', message: '请填写评语' }],
};

const options = [{
  label: 'very high',
  value: 2
},{
  label: 'high',
  value: 1
},{
  label: 'low',
  value: -1
},{
  label: 'very low',
  value: -2
}]

const state = reactive({
  form: {
    reviewScore: 0,
    reviewConfidence: null,
    reviewComment: '',
  },
  score: 0,
  confidence: null
})

const {form, score} = toRefs(state);
const reviewFormRef = ref()

const onSubmit = () =>{
  console.log(form.value)
  if (!reviewFormRef.value) return
  reviewFormRef.value.validate((valid: boolean, fields: any) => {
    if (valid) {
      fistReview({
        contributionId: props.contributionId,
        seq: props.seq,
        reviewScore: toScore(form.value.reviewScore),
        reviewConfidence: form.value.reviewConfidence,
        reviewComment: form.value.reviewComment,
      }).then((res: any) => {
        if(res.status == 200){
          ElMessage.success('提交评审结果成功')
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
  console.log(score.value)
  if(props.status !== 0){
    form.value.reviewScore = props.score;
    form.value.reviewConfidence = props.confidence;
    form.value.reviewComment = props.comment;
  }
})



</script>