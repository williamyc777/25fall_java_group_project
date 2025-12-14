<script setup>
import {getCurrentInstance, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import axiosInstance from "@/utils/axios";
import VueNativeSock from "vue-native-websocket-vue3";
import store from "@/utils/store";
import app, {globalStore} from "@/main";

const router = useRouter()

const loginForm = reactive({
  username: '',
  password: ''
})

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

const loginFormInTemp = ref(null)


const instance = getCurrentInstance();

function initWebsocket() {
  // let baseUrl = 'ws://' + window.location.host.split(':')[0] + ':8082' + '/websocket/' + localStorage.getItem("token")
  let baseUrl = 'ws://' + '10.16.88.247' + ':8082' + '/websocket/' + localStorage.getItem("token")
  console.log(localStorage.getItem("token"))
  app.use(VueNativeSock,
      baseUrl, {
        headers: {
          Authorization: localStorage.getItem('token')
        },
        store: store,
        format: 'json',
        reconnection: true,
        reconnectionAttempts: 5,
        reconnectionDelay: 3000,
        connectManually: true,
      })
  // modify the websocket's authorization header


}

function checkLoginType() {
  if (localStorage.getItem('token') != null) {
    console.log('reinit')
    axiosInstance.get('/userInfo', {
      headers: {
        Authorization: localStorage.getItem('token')
      }
    }).then((res) => {
      console.log(res.data)
      if (res.data.data.userType === 'Admin') {
        router.push({path: '/admin'})
      } else if (res.data.data.userType === 'User') {
        initWebsocket()
        localStorage.setItem('userId', res.data.data.id)
        router.push({path: '/main'})
      }
    }).catch((err) => {
      console.log(err)
    })
  }
}

const login = () => {
  loginFormInTemp.value.validate((valid) => {
    if (valid) {
      axiosInstance.post('/login', {
            username: loginForm.username,
            password: loginForm.password
          },
          {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          }
      ).then((res) => {
        console.log(res)
        if (res.data.data && res.data.data.length > 0) { // 添加对res.data.data的检查
          console.log('login success')
          localStorage.setItem('token', res.data.data)
          localStorage.setItem('username', loginForm.username)
          checkLoginType()
          console.log(localStorage.getItem('token'))
        } else {
          console.log('login failed')
          alert('Username or password is incorrect')
        }
      }).catch((err) => {
        console.log(err)
        alert('Username or password is incorrect')
      })
    } else {

    }

  })
}

// TODO: add the sign up function
function toSignUp() {
  router.push({path: '/signup'})
}

// -------------------test button function-------------------
// add your test button function here

function toProfile() {
    router.push({path: '/profile', query: {userID: 1}})
}

function toProfileTest() {
    router.push({path: '/profileTest'})
}

function toMain() {
  router.push({path: '/main'})
}

function toStudent() {
  router.push({path: '/student'})
}

function toAdmin() {
  router.push({path: '/admin'})
}

function toSquare() {
    router.push({path: '/square', query: {eventID: 1}})
  // let url = router.resolve({path: '/square'}).href
  // window.open(url, '_blank')
}

function toPost() {
    // router.push({path: '/post'})
  let url = router.resolve({path: '/square/post', query: {id: 1}}).href
  window.open(url, '_blank')
}

function toEvent() {
  router.push({path: '/event', query: {id: 1}})
}

function toWriteEvent() {
  router.push({path: '/event/create'})
}

function toManageEvent() {
  router.push({path: '/event/manage'})
}

function toMessage() {
  router.push({path: '/message'})
}

function toEventCardTest() {
  router.push({path: '/event/cardTest'})
}

function toMyEvent() {
  router.push({path: '/event/my'})
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
    <el-form :model="loginForm" :rules="rules" ref="loginFormInTemp">
      <el-form-item label="username" prop="username" style="margin-left: 100px; margin-top: 30px; margin-bottom: 30px">
        <el-input v-model="loginForm.username" style="max-width: 300px; height: 30px"></el-input>
      </el-form-item>
      <el-form-item label="password" prop="password" style="margin-left: 100px">
        <el-input v-model="loginForm.password" show-password style="max-width: 300px"></el-input>
      </el-form-item>

    </el-form>

    <div @click="toSignUp">
      <p class="signUpText" style="margin-left: 110px; margin-top: 10px; color: #6bccff; cursor: pointer">
        I don't have an account...
      </p>
    </div>

    <button type="submit" class="submit" @click="login">
      Sign in
    </button>


    <!-- ------------- add your test button here --------------- -->
<!--    <button type="submit" @click="toMain">-->
<!--      toMain-->
<!--    </button>-->

<!--    <button type="submit" @click="toSquare">-->
<!--      toSquare-->
<!--    </button>-->

<!--    <button type="submit" @click="toPost">-->
<!--      toPost-->
<!--    </button>-->

<!--    <button type="submit" @click="toEvent">-->
<!--      toEvent-->
<!--    </button>-->

<!--      <button type="submit" @click="toProfile">-->
<!--          toProfile-->
<!--      </button>-->
<!--      <button type="submit" @click="toProfileTest">-->
<!--          toProfileTest-->
<!--      </button>-->
<!--    <button type="submit" @click="toWriteEvent">-->
<!--      toWriteEvent-->
<!--    </button>-->

<!--    <button type="submit" @click="toManageEvent">-->
<!--      toManageEvent-->
<!--    </button>-->

<!--    <button type="submit" @click="toMessage">-->
<!--      toMessage-->
<!--    </button>-->

<!--    <button type="submit" @click="toEventCardTest">-->
<!--      toCardTest-->
<!--    </button>-->

<!--    <button type="submit" @click="toMyEvent">-->
<!--      toMyEvent-->
<!--    </button>-->

    <!-- ------------- end of the test list --------------- -->
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
