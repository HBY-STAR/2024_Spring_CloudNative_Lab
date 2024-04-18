<template>
  <div class="register-background">
    <div class="register-box">
      <div class="register-back" @click="goToLogin()"><ArrowLeftBold /></div>
      <div class="register-title">注&nbsp;&nbsp;&nbsp;&nbsp;册</div>
      <div class="register-form">
        <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" label-position="top" style="display: flex">
          <div class="register-form-left">
            <el-form-item label="用户名" prop="username" class="register-form-item">
              <el-input v-model="registerForm.username" placeholder="请输入用户名" class="register-input"></el-input>
            </el-form-item>
            <el-form-item label="真实姓名" prop="realName" class="register-form-item">
              <el-input v-model="registerForm.realName" placeholder="请输入真实姓名" class="register-input"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password" class="register-form-item">
              <el-input v-model="registerForm.password" placeholder="请输入密码" type="password" show-password class="register-input"></el-input>
            </el-form-item>
          </div>
          <div class="register-form-right">
            <el-form-item label="电子邮箱" prop="email" class="register-form-item">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱" class="register-input"></el-input>
            </el-form-item>
            <el-form-item label="所属单位" prop="institutionName" class="register-form-item">
              <el-input v-model="registerForm.institutionName" placeholder="请输入所属单位" class="register-input"></el-input>
            </el-form-item>
            <el-form-item label="所在区域" prop="area" class="register-form-item">
              <el-input v-model="registerForm.area" placeholder="请输入所在区域" class="register-input"></el-input>
            </el-form-item>
          </div>
        </el-form>
      </div>
      <div style="margin-top: 20px; margin-bottom: 40px">
        <el-button type="primary" class="register-button" color="#28b445" @click="handleReset">重置</el-button>
        <el-button type="primary" class="register-button" color="#28b445" @click="handleRegister">注册</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ElMessage, FormInstance, FormRules } from 'element-plus'
  import { ArrowLeftBold } from '@element-plus/icons-vue'
  import { register } from '../../api/userApi'

  interface RegisterForm {
    username: string
    realName: string
    password: string
    email: string
    institutionName: string
    area: string
  }

  interface State {
    registerForm: RegisterForm
    registerRules: FormRules<RegisterForm>
  }

  const router = useRouter()

  const registerFormRef = ref<FormInstance>()

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
    } else if (value.includes(registerForm.value.username)) {
      callback(new Error('密码中不能包含账号'))
    } else {
      callback()
    }
  }

  // 邮箱校验
  function validateEmail(rule: any, value: any, callback: any) {
    // 1.使用雷·汤姆林森创立的标准E-mail格式，即用户标识符+ @ + 域名
    const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
    if (value === '') {
      callback(new Error('邮箱地址不能为空'))
    } else if (!emailPattern.test(value)) {
      callback(new Error('请输入有效的电子邮箱地址'))
    } else {
      callback()
    }
  }

  const state = reactive<State>({
    registerForm: {
      username: '',
      realName: '',
      password: '',
      email: '',
      institutionName: '',
      area: ''
    },
    registerRules: {
      username: [{ required: true, trigger: 'blur', validator: validateUserName }],
      realName: [{ required: true, trigger: 'blur', message: '真实姓名不能为空' }],
      password: [{ required: true, trigger: 'blur', validator: validatePassword }],
      email: [{ required: true, trigger: 'blur', validator: validateEmail }],
      institutionName: [{ required: true, trigger: 'blur', message: '所属单位不能为空' }],
      area: [{ required: true, trigger: 'blur', message: '所在区域不能为空' }]
    }
  })
  const { registerForm, registerRules } = toRefs(state)

  const handleReset = () => {
    if (!registerFormRef.value) return
    registerFormRef.value.resetFields()
  }

  const handleRegister = () => {
    if (!registerFormRef.value) return
    registerFormRef.value.validate((valid: boolean, fields: any) => {
      if (valid) {
        register(registerForm.value).then((res: any) => {
          if (res.status === 200) {
            ElMessage.success('注册成功！')
            router.push({ path: '/login' })
          } else if (res.status === 400) {
            ElMessage.error(res.data.message)
          } else {
            ElMessage.warning('注册失败！')
            console.log(res)
          }
        })
      } else {
        console.log('error submit!', fields)
      }
    })
  }

  const goToLogin = () => {
    router.push({ path: '/login' })
  }
</script>

<style>
  .register-background {
    width: 100%;
    height: 100%;
    margin: 0;
    background: url('../../assets/imgs/background.jpg') center no-repeat;
  }

  .register-box {
    position: absolute;
    width: 60%;
    height: 70%;
    margin-left: 20%;
    margin-top: 7%;
    border-radius: 15px;
    background-color: var(--color-background);
    padding: 20px;
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .register-back {
    width: 30px;
    position: absolute;
    margin-top: 10px;
    margin-left: 10%;
    cursor: pointer;
  }

  .register-title {
    font-size: 30px;
    margin: 0 auto;
    margin-bottom: 10px;
    font-family: inherit;
    font-weight: normal;
  }

  .register-form {
    width: 80%;
    margin-left: 10%;
    margin-top: 20px;
  }

  .register-form-left {
    font-family: 'KKSAKAI', sans-serif;
    width: 50%;
    text-align: center;
    font-size: 4rem;
  }

  .register-form-right {
    width: 50%;
    /* border-left: 1px solid var(--vt-c-divider-light-1); */
    text-align: center;
    padding-left: 5%;
  }

  .register-form-item {
    width: 90%;
    margin-bottom: 8% !important;
  }

  .register-input {
    height: 40px;
  }

  .register-button {
    width: 100px;
  }
</style>
