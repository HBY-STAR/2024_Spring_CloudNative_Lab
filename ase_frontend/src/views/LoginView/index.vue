<template>
  <div class="login-background">
    <div class="login-box">
      <div class="login-box-left">
        <div>RevuSage</div>
        <div style="width: 100%; overflow: hidden">
          <el-carousel height="260px" indicator-position="outside">
            <el-carousel-item key="1">
              <div class="login-carousel-item">
                <img :src="NewsIcon" alt="News Icon" width="230" />
                <div>会议投稿</div>
              </div>
            </el-carousel-item>
            <el-carousel-item key="2">
              <div class="login-carousel-item">
                <img :src="FileSearchIcon" alt="File Search Icon" width="230" />
                <div>论文审稿</div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <div class="login-box-right">
        <div class="login-title">登&nbsp;&nbsp;&nbsp;&nbsp;录</div>
        <div style="width: 70%; margin-left: 15%">
          <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" label-width="80px" label-position="top">
            <el-form-item label="用户名" prop="username" class="login-form-item">
              <el-input v-model="loginForm.username" placeholder="请输入用户名" :prefix-icon="User" class="login-input"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password" class="login-form-item">
              <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" :prefix-icon="Lock" show-password class="login-input"></el-input>
            </el-form-item>
          </el-form>
          <el-button type="primary" @click="handleLogin()" :loading="loading" class="login-button" color="#28b445">登录</el-button>
          <div style="font-size: 14px; color: grey">
            <span>
              还没有账号？前去
              <el-button link style="padding: 0; color: var(--color-theme)" @click="goToRegister()">注册</el-button>
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import FileSearchIcon from '@/assets/icons/FileSearchIcon.svg'
  import NewsIcon from '@/assets/icons/NewsIcon.svg'
  import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
  import { User, Lock } from '@element-plus/icons-vue'
  import { getLogin } from '../../api/userApi'
  import { setToken, setUsername } from '../../utils/auth'
  import { userStore } from '../../main'

  interface LoginForm {
    username: string
    password: string
  }

  interface State {
    loginForm: LoginForm
    loginRules: FormRules<LoginForm>
    loading: boolean
  }

  const router = useRouter()

  const loginFormRef = ref<FormInstance>()

  // 用户名校验
  function validateUserName(rule: any, value: any, callback: any) {
    // 1.5-32个字符
    // 2.只能包含字母，数字或两种特殊字符(-_)且只能以字母或-开头
    const usernamePattern = /^[a-zA-Z-][a-zA-Z0-9_-]{4,32}$/
    if (value.length < 5 || value.length > 32) {
      callback(new Error('用户名长度为5-32个字符'))
    } else if (!usernamePattern.test(value)) {
      callback(new Error('只能包含字母，数字或两种特殊字符(-_)且只能以字母或-开头'))
    } else {
      callback()
    }
  }

  // 密码校验
  function validatePassword(rule: any, value: any, callback: any) {
    // 1.6-32个字符
    // 2.字母数字或特殊字符（-_）至少包含两种
    // 3.不能包含账号
    const passwordPattern = /^(?=(.*\d.*[A-Za-z]|.*[A-Za-z].*\d|.*[A-Za-z].*[-_]|.*[-_].*[A-Za-z]|.*[-_].*\d|.*\d.*[-_]))[\w-_]{5,32}$/
    if (value.length < 6 || value.length > 32) {
      callback(new Error('密码长度为6-32个字符'))
    } else if (!passwordPattern.test(value)) {
      callback(new Error('字母数字或特殊字符（-_）至少包含两种'))
    } else if (value.includes(loginForm.value.username)) {
      callback(new Error('密码中不能包含账号'))
    } else {
      callback()
    }
  }

  const state = reactive<State>({
    loginForm: {
      username: '',
      password: ''
    },
    loginRules: {
      username: [{ required: true, trigger: 'blur', validator: validateUserName }],
      password: [{ required: true, trigger: 'blur', validator: validatePassword }]
    },
    loading: false
  })
  const { loginForm, loginRules, loading } = toRefs(state)

  const handleLogin = () => {
    if (!loginFormRef.value) return
    loginFormRef.value.validate((valid: boolean, fields: any) => {
      if (valid) {
        getLogin(loginForm.value).then((res: any) => {
          if (res.status === 200) {
            if (res.config.headers.satoken) {
              setToken(res.config.headers.satoken)
            }
            userStore.setUsername(loginForm.value.username)
            setUsername(loginForm.value.username)
            ElMessage.success('登陆成功')
            if (loginForm.value.username === 'admin') {
              router.push({ path: '/admin' })
            } else {
              router.push({ path: '/' })
            }
          }
        })
      } else {
        console.log('error submit!', fields)
      }
    })
  }

  const goToRegister = () => {
    router.push({ path: '/register' })
  }
</script>

<style scoped>
  .login-background {
    width: 100%;
    height: 100%;
    margin: 0;
    background: url('../../assets/imgs/background.jpg') center no-repeat;
  }

  .login-box {
    position: absolute;
    display: flex;
    width: 60%;
    height: 70%;
    margin-left: 20%;
    margin-top: 7%;
    border-radius: 15px;
    background-color: var(--color-background);
    padding: 15px;
  }

  .login-box-left {
    font-family: 'KKSAKAI', sans-serif;
    width: 50%;
    text-align: center;
    font-size: 4rem;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    padding-bottom: 20px;
  }

  .login-box-right {
    width: 50%;
    border-left: 1px solid var(--vt-c-divider-light-1);
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .login-carousel-item {
    font-size: 16px;
  }

  .login-title {
    font-size: 30px;
    margin-bottom: 30px;
    font-family: inherit;
    font-weight: normal;
  }

  .login-form-item {
    margin-bottom: 30px;
  }

  .login-input {
    width: 100%;
    height: 40px;
  }

  .login-button {
    width: 70%;
    height: 40px;
    border-radius: 10px;
    margin-bottom: 10px;
  }
</style>
../../api/userApi
