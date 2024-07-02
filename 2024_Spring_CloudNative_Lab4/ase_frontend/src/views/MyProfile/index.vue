<template>
  <el-breadcrumb separator=">" class="profile-breadcrumb">
    <el-breadcrumb-item>
      <el-icon><House /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item>个人中心</el-breadcrumb-item>
  </el-breadcrumb>
  <el-divider style="margin-top: 10px" />
  <div class="profile-container">
    <div style="width: 50%">
      <div class="avater-container">
        <el-avatar shape="square" size="large" :icon="UserFilled" style="font-size: 24px" />
        <div style="margin-left: 20px">
          <div style="font-size: 24px; font-weight: bold; line-height: 56px">{{ profile.username }}</div>
        </div>
      </div>
      <el-form :model="profile" ref="profileRef" label-position="top" style="margin-top: 50px">
        <el-form-item label="真实姓名：" label-width="120px" prop="realname">
          <el-input v-model="profile.realName" disabled class="profile-input" />
        </el-form-item>
        <el-form-item label="邮箱：" label-width="120px" prop="realname">
          <el-input v-model="profile.email" disabled class="profile-input" />
        </el-form-item>
        <el-form-item label="所在单位：" label-width="120px" prop="realname">
          <el-input v-model="profile.institutionName" disabled class="profile-input" />
        </el-form-item>
        <el-form-item label="所在地区：" label-width="120px" prop="realname">
          <el-input v-model="profile.area" disabled class="profile-input" />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { House, UserFilled } from '@element-plus/icons-vue'
  import { getMyProfile } from '../../api/userApi'
  import { ElMessage } from 'element-plus'
  import { getUsername } from '../../utils/auth'

  const state = reactive({
    profile: {
      username: getUsername(),
      realName: '---',
      email: '---',
      institutionName: '---',
      area: '---'
    }
  })

  const { profile } = toRefs(state)

  onMounted(() => {
    getProfile()
  })

  const getProfile = () => {
    getMyProfile().then((res: any) => {
      if (res.status === 200) {
        profile.value = res.data.data
      }
    })
  }
</script>

<style scoped>
  .profile-breadcrumb {
    font-size: 16px;
    line-height: 32px;
  }

  .profile-container {
    /* width: 80%;
    margin-left: 10%; */
    display: flex;
    justify-content: center;
  }

  .avater-container {
    display: flex;
    justify-content: center;
  }

  .profile-input {
    height: 40px;
    /* width: 50%; */
  }
</style>
