<script setup>
import {ref, toRefs, computed, onMounted} from "vue";
import {useRouter} from "vue-router";
import axiosInstance from "@/utils/axios";

const router = useRouter()

const props = defineProps({
  userId: {
    type: String,
    required: true
  },
  needLevi: {
    type: Boolean,
    default: true
  },
  needSmall: {
    type: Boolean,
    default: false
  },
  sizeSmall: {
    type: String,
    default: '25px'
  },
  sizeMini: {
    type: String,
    default: '38px'
  },
  sizeWrapper: {
    type: String,
    default: '45px'
  }
})

const smallStyle = computed(() => {
  return "width: " + props.sizeSmall + "; height: " + props.sizeSmall + ";"
})
const miniStyle = computed(() => {
  return "width: " + props.sizeMini + "; height: " + props.sizeMini + ";"
})
const wrapperStyle = computed(() => {
  return "width: " + props.sizeWrapper + "; height: " + props.sizeWrapper + ";"
})


const toshow = ref(false)

const isSelf = computed(() => {
  return localStorage.getItem('userId') === props.userId
})

function toProfile() {
  router.push({path: '/profile', query: {userID: props.userId}})
}

function mouseEnter() {
  toshow.value = true
}

function mouseOut() {
  toshow.value = false
}

function toSelf() {
  router.push({path: '/self'})
}

function logout() {
  router.push({path: '/'})
  localStorage.setItem('token', '')
  localStorage.setItem('userId', '')
}

function toChat() {
  router.push({
    path: '/message', query: {
      messageType: 'chats',
      userId: props.userId
    }
  })
}

function invite() {
  router.push({
    path: '/invite', query: {
      userId: props.userId
    }
  })
}

let avatar_url = ref('')

onMounted(() => {
  axiosInstance.get('profile/info/get', {
    params: {
      userID: Number(props.userId)
    }
  }).then(response => {
    let temp = response.data.data
    avatar_url.value = temp.avatar
  }).catch(error => {
    console.error(error);
  });
})

// let avatar_url = computed(() => {
//   axiosInstance.get('profile/info/get', {
//     params: {
//       userID: Number(props.userId)
//     }
//   }).then(response => {
//     let temp = response.data.data
//     return temp.avatar
//   }).catch(error => {
//     console.error(error);
//   });
// })

</script>


<template>
  <div>
    <div @mouseover="mouseEnter" @mouseleave="mouseOut" class="wrapper" :style="wrapperStyle">
      <img v-if="needSmall" @click="toProfile" :style="smallStyle"
           class="header-entry-small" alt="avatar" id="avatar1" :src="avatar_url">
      <img v-else @click="toProfile" :style="miniStyle"
           class="header-entry-mini" alt="avatar" id="avatar2" :src="avatar_url">
      <div v-if="props.needLevi">
        <div class="levitate" v-show="toshow" @mouseover="mouseEnter" @mouseout="mouseOut">

          <div v-if="isSelf" style="width: 100%">
            <div>
              <div @click="logout">
                <div class="levi_butt">Log out</div>
              </div>
            </div>
          </div>

          <div v-else style="width: 100%">
            <div>
              <div @click="toChat">
                <div class="levi_butt">Chat</div>
              </div>
            </div>
          </div>

        </div>
      </div>

    </div>

  </div>
</template>

<style scoped>
.header-entry-mini {
  z-index: 2;
  display: block;
  border-radius: 50%;
  cursor: pointer;
}

.header-entry-small {
  z-index: 2;
  display: block;
  border-radius: 50%;
  cursor: pointer;
}

.wrapper {
  position: relative;
}

.levitate {
  left: -20px;
  height: 40px;
  width: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: absolute;
  align-content: center;
  align-items: center;
  border: 1px solid #cccccc;
  border-radius: 10px;
  background-color: #ffffff;
  z-index: 9999;
}

.levi_butt {
  cursor: pointer;
  align-items: center;
  align-content: center;
  text-align: center;
}

.el-divider-modified1 {
  margin: 15px 0;
  width: 100%;
}

</style>
