<script setup>
import {onBeforeRouteLeave, useRoute, useRouter} from "vue-router";
import Avatar from "@/components/Modules/avatar/Avatar.vue";
import {getCurrentInstance, onMounted, onUpdated, onBeforeUnmount, ref} from "vue";
import axiosInstance from "@/utils/axios";
import {formatTime, nowDate} from "@/components/User/pages/message/utils";


const router = useRouter()
const route = useRoute()

const chatData = ref([])
const chatTexts = ref([])
const noChat = ref(true)
const userId = ref(null)

const inputText = ref('')

function toChatter(newId) {
  userId.value = newId
  updateChatTexts()
}


function sendChat() {
  if (inputText.value === '') {
    return
  }

  let temp = new FormData()
  temp.append('userId', userId.value)
  temp.append('content', inputText.value)
  temp.append('time', nowDate(new Date()))

  axiosInstance.post('/message/sendChat', temp).then(() => {
    inputText.value = ''
    console.log('sent to backend')
    updateChatTexts()
  }).catch(error => {
    console.error(error);
  });
}

function updateChatTexts() {
  for (let i = 0; i < chatData.value.length; i++) {
    if (chatData.value[i].userId === Number(userId.value)) {
      chatData.value[i].hasUnread = false
      readChat()
      break
    }
  }
  axiosInstance.get('/message/chatText', {
    params: {
      userId: Number(userId.value)
    }
  }).then(response => {
    let tempList = response.data.data
    chatTexts.value = []
    for (let i = 0; i < tempList.length; i++) {
      chatTexts.value.push({
        id: tempList[i].id,
        fromSelf: (tempList[i].from === Number(localStorage.getItem('userId'))),
        time: formatTime(tempList[i].time),
        content: tempList[i].content,
      })
    }
  }).catch(error => {
    console.error(error);
  });
}

function readChat() {
  let temp = new FormData()
  temp.append('messageType', 'chat')
  temp.append('userId', userId.value)
  axiosInstance.post('/message/read', temp).then(() => {
    // console.log('read chats from ' + userId.value.toString())
  }).catch(error => {
    console.error(error);
  });
}

function scrollToBottom() {
  let chatBox = document.getElementsByClassName('chat_box')[0]
  chatBox.scrollTop = chatBox.scrollHeight
}

onUpdated(() => {
  scrollToBottom()
})

let timer = null
const polling = () => {
  timer = setInterval(() => {
    updateChatTexts()
  }, 1500)
}

onMounted(() => {
  axiosInstance.get('/message/chat').then(response => {
    let temp = response.data.data;
    for (let i = 0; i < temp.length; i++) {
      chatData.value.push({
        userId: JSON.parse(temp[i]).userId,
        userName: JSON.parse(temp[i]).userName,
        hasUnread: JSON.parse(temp[i]).hasUnread,
      })
    }
    // alert(chatData.value[1].userId)
    // alert(chatData.value[1].userName)
    // alert(chatData.value[1].hasUnread)

    if (typeof (route.query.userId) !== 'undefined') {

      userId.value = Number(route.query.userId)
      console.log(userId.value)
      noChat.value = false

      let userName = null
      for (let i = 0; i < chatData.value.length; i++) {
        if (chatData.value[i].userId === Number(userId.value)) {
          userName = chatData.value[i].userName
          let user = chatData.value.splice(i, 1)[0]
          chatData.value.unshift(user)
          break
        }
      }
      if (userName === null) {
        axiosInstance.get('profile/info/get', {
          params: {
            userID: Number(userId.value)
          }
        }).then(response => {
          userName = response.data.data.name
          chatData.value.unshift({
            userId: Number(userId.value),
            userName: userName,
            hasUnread: false
          })
        }).catch(error => {
              console.error(error);
            });
      }

    } else if (chatData.value.length > 0) {
      userId.value = chatData.value[0].userId
      noChat.value = false
    } else {
      noChat.value = true
    }

    if (noChat.value === false) {
      updateChatTexts()
      // connectWs();
    }
    // handle the polling
    polling()


  }).catch(error => {
    console.error(error);
  });

})


const ws = getCurrentInstance()

function connectWs() {
  ws.proxy.$connect()
  console.log('WS connected')
  ws.proxy.$socket.onmessage = function (event) {
    console.log('WS received message: ' + event.data)
    let ws_id = JSON.parse(event.data).userId
    let ws_name = JSON.parse(event.data).userName

    if (userId.value === ws_id) {
      updateChatTexts()
    }
    else {
      let flag = false
      for (let i = 0; i < chatData.value.length; i++) {
        if (chatData.value[i].userId === ws_id) {
          chatData.value[i].hasUnread = true
          flag = true
          break
        }
      }
      if (flag === false) {
        chatData.value.unshift({
          userId: ws_id,
          userName: ws_name,
          hasUnread: true
        })
      }
    }
  }
}

onBeforeUnmount(() => {
  // try {
  //   ws.proxy.$disconnect()
  // } catch (error) {
  //   console.error(error)
  // }
  clearInterval(timer)
})


// test data
// noChat.value = false
// chatData.value = [
//   {
//     userId: 1,
//     userName: 'Alice'
//   },
// {
//     userId: 2,
//     userName: 'Bob'
//   },
//   {
//     userId: 3,
//     userName: 'Charlie'
//   },
//   {
//     userId: 4,
//     userName: 'David'
//   },
//   {
//     userId: 5,
//     userName: 'Eve'
//   }
// ]
//
// chatTexts.value = [
//   {
//     id: 1,
//     fromSelf: true,
//     time: '2021-10-01 12:00:00',
//     content: 'Hello, Alice!'
//   },
//   {
//     id: 2,
//     fromSelf: false,
//     time: '2021-10-01 12:01:00',
//     content: 'Hi, Bob!'
//   },
//   {
//     id: 3,
//     fromSelf: true,
//     time: '2021-10-01 12:02:00',
//     content: 'How are you?'
//   },
//   {
//     id: 4,
//     fromSelf: false,
//     time: '2021-10-01 12:03:00',
//     content: 'I am fine, thank you.'
//   },
//   {
//     id: 5,
//     fromSelf: true,
//     time: '2021-10-01 12:04:00',
//     content: 'Good to hear that.'
//   },
//   {
//     id: 6,
//     fromSelf: false,
//     time: '2021-10-01 12:05:00',
//     content: 'Goodbye.'
//   }
// ]

</script>

<template>
  <div v-if="noChat" style="align-content: center; text-align: center; color: lightpink">
    <p>Trying communications is a good start!</p>
  </div>

  <div v-else>
    <div class="wrap">

      <div class="chatter_list">
        <div v-for="items in chatData" :key="items.userId" style="width: 100%">
          <div @click="toChatter(items.userId)" class="chatter"
               :style="items.userId === userId ? 'background-color: #ffd2d9' : ''">
            <div style="margin-left: 10px; display: flex; flex-direction: row; align-items: center">
              <avatar :user-id="items.userId.toString()" :need-levi="false"/>
              <p style="font-size: 15px; margin-left: 10px;">{{ items.userName }}</p>
              <p :style="items.hasUnread ? 'display: block':'display: none'">&nbsp;</p>
              <i :class="items.hasUnread ? 'dot_show':'dot_unshow'"></i>
            </div>
          </div>
<!--          <i :class="items.hasUnread ? 'dot_show':'dot_unshow'"></i>-->
        </div>
      </div>

      <div class="chat_window">
        <div class="chat_box" ref="chats">
          <div v-for="items in chatTexts" :key="items.id">
            <div v-if="items.fromSelf">
              <div class="self_wrap">
                <div class="time_wrap">
                  <p class="time">{{ items.time }}&nbsp;</p>
                </div>

                <div class="self_text">
                  <p class="self_letters">{{ items.content }}</p>
                </div>
              </div>
            </div>

            <div v-else>
              <div class="other_wrap">
                <div class="other_text">
                  <p class="other_letters">{{ items.content }}</p>
                </div>

                <div class="time_wrap">
                  <p class="time">&nbsp;{{ items.time }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="input_wrap">
          <input v-model="inputText" placeholder="Type your message..." @keyup.enter="sendChat" class="input_box"/>
          <div @click="sendChat" class="button_box">Send</div>
        </div>
      </div>

    </div>
  </div>

</template>

<style scoped>

.wrap {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;

}

.chatter_list {
  width: 18%;
  height: 450px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  border-radius: 10px;
  background-color: #ffffff;
  overflow-y: scroll;
}

.chatter {
  display: flex;
  flex-direction: row;
  align-items: center;
  cursor: pointer;
  border: #c7c7c7 1px solid;
}

.dot_show {
  background-color: red;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  display: block;
}

.dot_unshow {
  background-color: #FFFFFF00;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  display: none;
}

.chat_window {
  width: 80%;
  display: flex;
  flex-direction: column;
}

.chat_box {
  width: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  background-color: #ffffff;
  overflow-y: scroll;
  height: 75vh;
}

.input_wrap {
  margin: 20px 0 10px;
  display: flex;
  justify-content: stretch;
}

.input_box {
  width: 100%;
  height: 40px;
  border-radius: 10px;
  border: #c7c7c7 1px solid;
  padding: 10px;
  box-sizing: border-box;
}

.input_box:focus {
  outline: lightpink auto 1px;
}

.button_box {
  width: 100px;
  height: 40px;
  margin-left: 20px;
  border-radius: 10px;
  background-color: lightpink;
  border: 1px solid #c7c7c7;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  color: #ffffff;
}

.button_box:hover {
  background-color: #f6aaaa;
}

.button_box:active {
  transform: scale(0.95);
}

.self_wrap {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  margin: 10px;
  align-items: flex-end;
}

.self_text {
  max-width: 70%;
  display: flex;
  background-color: lightpink;
  border-radius: 15px 0 15px 15px;
}

.other_wrap {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  margin: 10px;
  align-items: flex-end;
}

.other_text {
  max-width: 70%;
  display: flex;
  background-color: #ffffff;
  border-radius: 0 15px 15px 15px;
  border: 1px solid #c7c7c7;
}

.self_letters {
  margin: 10px;
  font-size: 15px;
  text-align: left;
  color: #ffffff;
}

.other_letters {
  margin: 10px;
  font-size: 15px;
  text-align: left;
}

.time_wrap {
  display: flex;
  flex-direction: column;
  margin: 10px;
  align-items: flex-end;
  line-height: 0;
}

.time {
  font-size: 10px;
}

</style>