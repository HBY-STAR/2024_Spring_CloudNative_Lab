<template>
  <div>
    <el-descriptions :column="1" border size="large">
      <el-descriptions-item>
        <template #label>
          <div class="description-label">评分</div>
        </template>
        <div class="description-content">
<!--          {{ getScoreString(props.score) }}-->
          <el-rate
              v-model="score"
              :texts="['reject', 'weak reject', 'weak accept', 'accept']"
              show-text
              :max=4
              disabled
          />
        </div>
      </el-descriptions-item>

      <el-descriptions-item v-if="props.finalScore && props.finalScore !== 0">
        <template #label>
          <div class="description-label">复审评分</div>
        </template>
        <div class="description-content">
          <!--          {{ getScoreString(props.score) }}-->
          <el-rate
              v-model="finalScore"
              :texts="['reject', 'weak reject', 'weak accept', 'accept']"
              show-text
              :max=4
              disabled
          />
        </div>
      </el-descriptions-item>

      <el-descriptions-item>
        <template #label>
          <div class="description-label">confidence</div>
        </template>
        <div class="description-content">
          {{ confidenceToString[props.confidence] }}
        </div>
      </el-descriptions-item>

      <el-descriptions-item>
        <template #label>
          <div class="description-label">评语</div>
        </template>
        <div class="description-content">
          {{ props.comment }}
        </div>
      </el-descriptions-item>

      <el-descriptions-item v-if="props.message && props.message?.length > 0">
        <template #label>
          <div class="description-label">rebuttal</div>
        </template>
        <div class="description-content">
          {{ props.message }}
        </div>
      </el-descriptions-item>
    </el-descriptions>

  </div>
<el-button type="success" @click="handleClick" style="margin-left: 85%; margin-top: 10px"
           v-if="props.status === 1">rebuttal</el-button>

  <el-dialog v-model="dialogVisible" title="提交rebuttal">
    <el-input v-model="message" autocomplete="off" type="textarea" :rows="4"/>
    <el-button type="success" @click="submit" style="margin-left: 90%; margin-top: 10px">提交</el-button>
  </el-dialog>
</template>

<script setup lang="ts">
import {rebuttal} from "../../../api/reviewApi";
import {ElMessage} from "element-plus";
import {getScore} from "../../../utils/const";


const props = defineProps({
  seq: Number,
  score: Number,
  confidence: Number,
  comment: String,
  contributionId: Number,
  message: String,
  finalScore: Number,
  status: Number
})

const state = reactive({
  dialogVisible: false,
  score: 0,
  finalScore: 0,
  message: '',
})

const {dialogVisible, message, score, finalScore} = toRefs(state);

const confidenceToString = ['very low', 'low', 'high', 'very high']
onMounted(() => {
  console.log(props.finalScore)
  score.value = getScore(props.score)
  finalScore.value = getScore(props.finalScore)
})


const handleClick = () => {
  dialogVisible.value = true;
}

const submit = () => {
 if(message.value.length < 1){
   ElMessage.error('请填写rebuttal!');
 }
 else{
   rebuttal({
     seq: props.seq,
     contributionId: props.contributionId,
     rebuttalMessage: message.value
   }).then((res: any) => {
     if(res.status === 200){
       ElMessage.success('提交rebuttal成功!')
       dialogVisible.value = false;

     }
   })
 }
}

</script>

<style scoped>
.description-label {
  text-align: center;
  min-width: 120px;
  min-height: 25px;
  font-weight: 700;
  line-height: 25px;
}

.description-content {
  min-height: 25px;
  line-height: 25px;
}
</style>