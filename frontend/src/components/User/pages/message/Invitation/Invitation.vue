<script setup>
import { formatTime } from "@/components/User/pages/message/utils";
import axiosInstance from "@/utils/axios";
import {reactive, ref, onMounted, getCurrentInstance} from "vue";
import Avatar from "@/components/old/Student/Avatar.vue";


const invAndAppData = ref([])


function accept(id) {
  let temp = new FormData()
  temp.append('message_id', Number(id))
  axiosInstance.post('/message/invitation/accept', temp).then(response => {
    let temp = response.data
    if (Number(temp.code) === 0) {
      if (Number(temp.data.errorCode) === 0) {
        removeInvi(id)
        alert('You have accepted the invitation.')
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
  temp.append('message_id', Number(id))
  axiosInstance.post('/message/invitation/reject', temp).then(response => {
    let temp = response.data
    if (Number(temp.code) === 0) {
      if (Number(temp.data.errorCode) === 0) {
        removeInvi(id)
        alert('You have rejected the invitation.')
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

function removeInvi(id) {
  for (let i = 0; i < invAndAppData.value.length; i++) {
    if (invAndAppData.value[i].id === id) {
      invAndAppData.value.splice(i, 1)
      break
    }
  }
}

onMounted(() => {
  axiosInstance.get('/message/invAndApp')
      .then(response => {
        let tempList = response.data.data
        invAndAppData.value = []
        for (let i = 0; i < tempList.length; i++) {
          invAndAppData.value.push({
            id: tempList[i].id,
            inviterId: tempList[i].from.id.toString(),
            inviterName: tempList[i].from.name,
            groupId: JSON.parse(tempList[i].content).groupId,
            groupName: JSON.parse(tempList[i].content).groupName,
            text: JSON.parse(tempList[i].content).text,
            time: formatTime(tempList[i].time),
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
      <div v-for="item in invAndAppData" :key="item.id">
        <div class="entity">
          <div class="header">

            <div style="display: flex; flex-direction: row; align-items: end">
              <p class="title1">{{item.groupName}}</p>
              <p class="title2">invite you</p>
            </div>

            <div class="person">
              <avatar :user-id="item.inviterId"/>
              <p style="font-size: 15px; font-weight: bold; margin-left: 10px;">{{item.inviterName}}</p>
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
  margin-top: 10px;
}

.person {
  margin: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.title1 {
  font-size: 30px;
  font-weight: bold;
  margin-left: 10px;
  margin-top: 0;
  margin-bottom: 0;
}

.title2 {
  font-size: 20px;
  font-weight: bold;
  margin-left: 10px;
  color: lightpink;
  margin-top: 0;
  margin-bottom: 0;
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
  height: 40px;
}

</style>