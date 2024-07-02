<template>
  <div style="display: flex; margin-left: 20px">
    <el-input v-model="input" placeholder="请输入想要查询的用户名或真实姓名" style="width: 80%" :prefix-icon="Search" />
    <el-button type="primary" color="#28b445" style="margin-left: 20px" @click="searchUser">查询</el-button>
  </div>
  <div>
    <el-empty description="暂无数据" v-if="userList.length === 0" />
    <div v-else style="margin-top: 30px">
      <div style="margin-left: 20px">
        <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
        <el-checkbox-group v-model="checkUsers" @change="handleCheckedChange">
          <div v-for="user in userList" style="width: 100%; display: flex; margin-top: 15px; line-height: 40px; margin-left: 10px; align-items: center">
            <el-checkbox :key="user.username" :label="user.username">{{ '' }}</el-checkbox>
            <el-avatar shape="circle" size="default" :icon="UserFilled" style="font-size: 24px; line-height: 40px" />
            <div style="margin-left: 10px">
              <div style="font-size: 16px; line-height: 20px">{{ user.username }}</div>
              <div style="font-size: 12px; line-height: 15px; margin-top: 5px">{{ user.realname }}</div>
            </div>
          </div>
        </el-checkbox-group>
      </div>
      <div style="display: flex; justify-content: center; margin-top: 20px">
        <el-button type="primary" color="#28b445" @click="invite">邀请</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { Search, UserFilled } from '@element-plus/icons-vue'
  import { invitePCMember } from '../../../api/notificationApi'
  import { getAllTopics } from "../../../api/conferenceApi";
  import { ElMessage } from 'element-plus'
  import { getUsername } from '../../../utils/auth'
  import { searchByRealName, searchByUsername } from '../../../api/userApi'
  import { events } from '../../../bus'
  const props = defineProps({
    conferenceName: String,
    topics: [],
  })

  const state = reactive({
    input: '',
    userList: [],
    checkUsers: [],
    checkAll: false,
    isIndeterminate: false,
  })
  const { input, userList, checkUsers, checkAll, isIndeterminate } = toRefs(state)

  const searchUser = () => {
    userList.value = []
    checkUsers.value = []
    checkAll.value = false
    isIndeterminate.value = false
    // 根据用户名查询
    searchByUsername(input.value).then((res: any) => {
      if (res.status === 200) {
        userList.value.push(res.data.data)
      }
    })

    // 根据真实姓名查询
    searchByRealName(input.value).then((res: any) => {
      if (res.status === 200) {
        res.data.data.forEach((e: any) => {
          const flag = userList.value.find((v: any) => {
            if (v.username === e.username) {
              return true
            }
          })
          if (flag === undefined) {
            userList.value.push(e)
          }
        })
      }
    })
  }

  const handleCheckAllChange = (val: boolean) => {
    checkUsers.value = val ? userList.value.map((v) => v.username) : []
    isIndeterminate.value = false
  }

  const handleCheckedChange = (value: string[]) => {
    const checkedCount = value.length
    checkAll.value = checkedCount === userList.value.length
    isIndeterminate.value = checkedCount > 0 && checkedCount < userList.value.length
  }

  const invite = () => {
    invitePCMember({
      sender: getUsername(),
      conferenceName: props.conferenceName,
      allTopics: props.topics,
      receiverList: checkUsers.value,
    }).then((res: any) => {
      if (res.status === 200 && res.data.success) {
        ElMessage.success('邀请成功')
        events.emit('inviteUser')
      }
    })
  }
</script>

<style>
  .el-input__wrapper.is-focus {
    box-shadow: 0 0 0 1px var(--color-theme) inset !important;
  }

  .el-checkbox__input.is-checked .el-checkbox__inner {
    background-color: var(--color-theme) !important;
    border-color: var(--color-theme) !important;
  }

  .el-checkbox__input :hover {
    border-color: var(--color-theme) !important;
  }

  .el-checkbox__input.is-indeterminate .el-checkbox__inner {
    background-color: var(--color-theme) !important;
    border-color: var(--color-theme) !important;
  }

  .el-checkbox__input.is-checked + .el-checkbox__label {
    color: var(--color-theme) !important;
  }
</style>
