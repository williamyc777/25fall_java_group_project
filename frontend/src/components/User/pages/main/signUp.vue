<script setup>
import  {reactive, ref} from "vue";
import axiosInstance from "@/utils/axios";
import {useRouter} from "vue-router";

const router = useRouter()
const registerForm = reactive({
  username: '',
  password: ''
})

function toLogin() {
  router.push({path: '/'})
}


const rules = reactive({
  username: [{
    required: true,
    message: 'please input username',
    trigger: 'blur'
  }],
  password: [{
    required: true,
    message: 'please input password',
    trigger: 'blur'
  }]
})

const registerFormInTemp = ref(null)

const register = () => {
  registerFormInTemp.value.validate((valid) => {
    if (valid) {
      axiosInstance.post('/signUp', {
            username: registerForm.username,
            password: registerForm.password
          },
          {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          }
      ).then((res) => {
        console.log(res)
        if (res.data.data) {
          console.log('registration success')
          alert('registration success')
          router.push({path: '/'})
        } else {
          console.log('registration failed')
          alert('registration failed, username already exist')
        }
      }).catch((err) => {
        console.log(err)
        alert('Username or password is incorrect')
      })
    }
  })
}

</script>

<template>
  <div class="logo">
    <a>
      <img src="https://www.sustech.edu.cn/static/images/sustech-logo-cn.png" alt="logo">
    </a>
  </div>
  <div class="video">
    <video class="videoStyle" id="Id" playsinline="true" autoplay="true" muted="false" loop="true">
      <source src="https://www.sustech.edu.cn/uploads/files/2024/01/25215239_50474.mp4" type="video/mp4">
    </video>
  </div>
  <div class="mainPanel">
    <h1 style="font-size: 30px; text-align: center">
      南科大活动中心
    </h1>
    <el-form :model="registerForm" :rules="rules" ref="registerFormInTemp">
      <el-form-item label="username" prop="username" style="margin-left: 100px; margin-top: 30px; margin-bottom: 30px">
        <el-input v-model="registerForm.username" style="max-width: 300px; height: 30px"></el-input>
      </el-form-item>
      <el-form-item label="password" prop="password" style="margin-left: 100px">
        <el-input v-model="registerForm.password" show-password style="max-width: 300px"></el-input>
      </el-form-item>
    </el-form>

    <div @click="toLogin">
      <p class="signUpText" style="margin-left: 110px; margin-top: 10px; color: #6bccff; cursor: pointer">
        Go back to login
      </p>
    </div>

    <button type="submit" class="submit" @click="register">
      Sign up
    </button>

  </div>

</template>

<style scoped>
.mainPanel {
  width: 600px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%);
  background: rgba(255,255,255,0.9);
  border-radius: 10px;
  padding: 10px 10px;
}

.video{
  width: 100%;
  max-width: unset;
  height: 100%;
  position: absolute;
  overflow: clip;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%);
  z-index: -2;
}

.logo a img{
height: 50%;

}

img, video {
border: none;
}

img {
vertical-align: middle;
overflow-clip-margin: content-box;
overflow: clip;
border:0;
height: 50%;
}

.submit {
  display: block;
  padding-top: 0.75rem;
  padding-bottom: 0.75rem;
  padding-left: 1.25rem;
  padding-right: 1.25rem;
  margin-top: 30px;
  margin-left: 120px;
  background-color: #4F46E5;
  color: #ffffff;
  font-size: 0.875rem;
  line-height: 1.25rem;
  font-weight: 500;
  width: 60%;
  border-radius: 0.5rem;
  text-transform: uppercase;
}

.signUpText {
  font-size: 0.875rem;
  line-height: 1.25rem;
  font-weight: 500;
  text-decoration: underline;
}

</style>
