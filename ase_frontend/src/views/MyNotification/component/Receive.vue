<template>
  <el-table :data="tableData.slice((current - 1) * 10, (current - 1) * 10 + 10)" stripe>
    <el-table-column prop="sender" label="发送者" show-overflow-tooltip />
    <el-table-column prop="conferenceName" label="会议名称" show-overflow-tooltip />
    <el-table-column prop="responsibleTopics" label="负责topic" show-overflow-tooltip />
    <el-table-column prop="inviteTime" label="发送时间" show-overflow-tooltip />
    <el-table-column prop="status" label="邀请状态" show-overflow-tooltip />
    <el-table-column label="操作" width="150">
      <template #default="scope">
        <el-button v-if="scope.row.status === '待确认'" type="primary" size="small" color="#28b445" @click="selectTopics(scope.row.notificationId, scope.row.allTopics)">
          同意
        </el-button>
        <el-button v-if="scope.row.status === '待确认'" type="danger" size="small" @click="handleAccept(scope.row.notificationId, false, [])">拒绝</el-button>
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

  <el-dialog title="请选择负责的topic" width="50%" style="top: -50px" v-model="open" @close="handleCancel">
    <el-checkbox-group v-model="topicsSelected">
      <el-checkbox v-for="topic in allTopics" :label="topic"/>
    </el-checkbox-group>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" color="#28b445" @click="handleConfirm">确定</el-button>
      </span>
    </template>
  </el-dialog>

</template>

<script setup lang="ts">
  import { reactive } from 'vue'
  import { handleReceive, handleInvitation } from '../../../api/notificationApi'
  import { getUsername } from '../../../utils/auth'
  import { ElMessage } from 'element-plus'
  import { invitationStatus } from '../../../utils/const'

  const state = reactive({
    tableData: [],
    current: 1,
    allTopics: [],
    open: false,
    id: '',
    topicsSelected: [],
  })
  const { tableData, current, open, allTopics, id, topicsSelected } = toRefs(state)

  onMounted(() => {
    fetchData()
  })

  const fetchData = () => {
    // tableData.value = [{
    //   allTopics: ["a", "b", "c"],
    //   status: "待确认"
    // }]
    handleReceive(getUsername()).then((res: any) => {
      if (res.status === 200 && res.data.success) {
        tableData.value = res.data.data
        tableData.value.map((v: any) => {
          v.status = invitationStatus[v.status]
          //展示所有负责的topic
          v.responsibleTopics = v.responsibleTopics.join('  ');
          console.log(v.status, v.allTopics)
          return v
        })
      }
    })
  }

  const handleCurrentChange = (newPage: any) => {
    current.value = newPage
  }

  const handleAccept = (notificationId: any, accept: boolean, topics: any[]) => {
      handleInvitation({
        username: getUsername(),
        notificationId: notificationId,
        responsibleTopics: topics,
        accept: accept
      }).then((res: any) => {
        if (res.status === 200 && res.data.success) {
          ElMessage.success(res.data.message)
          fetchData()
        }
      })
  }

  const selectTopics = (notificationId: string, topics: any) => {
    open.value = true;
    console.log(topics)
    console.log(open.value)
    allTopics.value = topics;
    id.value = notificationId;
  }

  const handleCancel = () => {
    open.value = false;
    allTopics.value = [];
    id.value = '';
    topicsSelected.value = [];
  }

  const handleConfirm = () => {
    handleAccept(id.value, true, topicsSelected.value);
  }


</script>
