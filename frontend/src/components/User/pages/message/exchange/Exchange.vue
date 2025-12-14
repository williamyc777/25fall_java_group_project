<script setup>
import axiosInstance from "@/utils/axios";
import {reactive, ref, onMounted, getCurrentInstance} from "vue";
import Avatar from "@/components/Modules/avatar/Avatar.vue";
import {formatTime} from "@/components/User/pages/message/utils";


const exchangeData = ref([])

exchangeData.value = [
  {
    id: 1,
    time: '2023-07-01 19:45:00',
    userId: 123456,
    userName: '王煜然',
    groupId: 1,
    groupName: 'Group A',
    roomId: 1003,
    roomName: '湖畔三栋3楼326',
    text: '你们好，我们想和你们换下房间，我们的房间是湖畔三栋3楼326，我们宿舍的人经常要去荔园那边上课，所以想换到近一点的地方，方便的话请私聊联系我们，谢谢。',
  },
  {
    id: 2,
    time: '2023-06-01 19:45:00',
    userId: 456789,
    userName: 'Shinomiya',
    groupId: 2,
    groupName: 'Group B',
    roomId: 1002,
    roomName: '湖畔三栋3楼325',
    text: 'Prepared by experienced English teachers, the texts, articles and conversations are brief and appropriate to your level of proficiency. Take the multiple-choice quiz following each text, and you\'ll get the results immediately. You will feel both challenged and accomplished! You can even download (as PDF) and print the texts and exercises. It\'s enjoyable, fun and free. Good luck!',
  },
  {
    id: 3,
    time: '2023-05-01 19:45:00',
    userId: 789123,
    userName: '区煜林',
    groupId: 3,
    groupName: 'Group C',
    roomId: 1001,
    roomName: '湖畔三栋3楼337',
    text: '我们的宿舍位于湖畔三楼最靠外，此处位置得天独厚，每天都有好看的蝶蛾类生物来做客，现在点击下方按钮，你就能不费吹灰之力地收获这份大自然的友谊！'
  }
]


function accept(id) {
  let temp = new FormData()
  temp.append('message_id', id)
  axiosInstance.post('/message/exchange/accept', temp).then(response => {
    let temp = response.data
    if (Number(temp.code) === 0) {
      if (Number(temp.data.errorCode) === 0) {
        removeExchange(id)
        alert('You have accepted the exchange.')
      }
      else {
        alert(temp.data.message)
      }

    } else {
      alert("Accept failed!")
    }

  }).catch(() => {
    console.error('error')
  })
}

function reject(id) {
  let temp = new FormData()
  temp.append('message_id', id)
  axiosInstance.post('/message/exchange/reject', temp).then(response => {
    let temp = response.data
    if (Number(temp.code) === 0) {
      if (Number(temp.data.errorCode) === 0) {
        removeExchange(id)
        alert('You have rejected the exchange.')
      }
      else {
        alert(temp.data.message)
      }

    } else {
      alert("Reject failed!")
    }

  }).catch(() => {
    console.error('error')
  })
}

function removeExchange(id) {
  for (let i = 0; i < exchangeData.value.length; i++) {
    if (exchangeData.value[i].id === id) {
      exchangeData.value.splice(i, 1)
      break
    }
  }
}

onMounted(() => {
  axiosInstance.get('/message/exchange')
      .then(response => {
        let tempList = response.data.data
        exchangeData.value = []
        for (let i = 0; i < tempList.length; i++) {
          exchangeData.value.push({
            id: tempList[i].id,
            time: formatTime(tempList[i].time),
            userId: tempList[i].from.id,
            userName: tempList[i].from.name,
            roomId: JSON.parse(tempList[i].content).roomId,
            roomName: JSON.parse(tempList[i].content).roomName,
            applyContent: JSON.parse(tempList[i].content).applyContent,
          })
        }
      })
      .catch(error => {
        console.error(error);
      });
})

</script>

<template>
  <div>
      <div v-for="item in exchangeData" :key="item.id">
        <div class="entity">
          <div class="header">

            <div style="display: flex; flex-direction: row; align-items: end">
              <p class="title1">{{item.groupName}}</p>
              <p class="title2">asked to exchange&nbsp;</p>
              <p class="room">{{item.roomName}}</p>
            </div>

            <div class="person">
              <avatar :user-id="item.userId"/>
              <p style="font-size: 15px; font-weight: bold; margin-left: 10px;">{{item.userName}}</p>
            </div>

            <p class="extend_time">{{item.time}}</p>

          </div>

          <div class="content">
            <p class="body">{{item.text}}</p>
          </div>

          <div class="buttons">
            <el-button style="background-color: #ffb2b2;" @click="accept(item.id)">
              <p style="color: white">Accept</p>
            </el-button>
            <el-button style="background-color: #ffffff; border: 1px solid black" @click="reject(item.id)">
              <p>Reject</p>
            </el-button>
          </div>

        </div>
      </div>
  </div>

</template>

<style scoped>
.entity {
  width: 100%;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  margin-bottom: 10px;
}

.header {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
}

.title1 {
  font-size: 30px;
  font-weight: bold;
  margin-left: 10px;
  margin-bottom: 0;
  margin-top: 10px;
}

.title2 {
  font-size: 20px;
  font-weight: bold;
  margin-left: 10px;
  color: lightpink;
  margin-bottom: 0;
}

.room {
  font-style: italic;
  font-size: 20px;
  font-weight: bold;
  color: darkgray;
  cursor: default;
  margin-bottom: 0;
}

.person {
  margin: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.extend_time {
  font-size: 10px;
  margin-left: 10px;
  margin-top: 0;
  margin-bottom: 0;
}

.content {
  width: 100%;
  display: flex;
  flex-direction: column;
  line-height: 2;
  justify-content: flex-start;
  align-items: flex-start;
}

.body {
  margin-left: 20px;
  margin-right: 20px;
  text-align: left;
}

.buttons {
  display: flex;
  flex-direction: row;
  width: 10%;
  justify-content: space-between;
  margin-left: 10px;
  margin-bottom: 10px;
  height: 40px;
}

</style>