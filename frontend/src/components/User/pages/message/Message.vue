<script setup>
import {useRoute, useRouter} from "vue-router";
import {reactive, ref, onMounted, getCurrentInstance} from "vue";
import axiosInstance from "@/utils/axios";
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import Chat from "@/components/User/pages/message/chat/Chat.vue";
import CommentEntity from "@/components/User/pages/message/CommentEntity.vue";
import { formatTime } from "@/components/User/pages/message/utils";


const router = useRouter()
const route = useRoute()


const mt = ref(route.query.messageType)

const messageSelectList = reactive({
  // noticeSelect: (typeof (route.query.messageType) === 'undefined' ? true : route.query.messageType === 'notices'),
  commentSelect: (typeof (route.query.messageType) === 'undefined' ? true : route.query.messageType === 'comments'),
  chatSelect: (route.query.messageType === 'chats')
})

const hasUnread = ref({
  notice: false,
  invAndApp: false,
  exchange: false,
  comment: false,
  chat: false
})

function press_button(messageType) {
  router.push({path: '/message', query: {messageType: messageType}})

  // report('messageSelectList')
}

const instance = getCurrentInstance();

// const noticeData = ref([])
const commentData = ref([])


onMounted(() => {
  // get all the unread stages
  axiosInstance.get('/message/hasUnread')
      .then(response => {
        // hasUnread.value.notice = response.data.data.notice
        // hasUnread.value.invAndApp = response.data.data.invAndApp
        // hasUnread.value.exchange = response.data.data.exchange
        // hasUnread.value.comment = response.data.data.comment
        // hasUnread.value.chat = response.data.data.chat
        let temp = JSON.parse(response.data.data)
        hasUnread.value.comment = temp.comment
        hasUnread.value.chat = temp.chat
      })
      .catch(error => {
        console.error(error);
      });

  // update the stages immediately
  let temp = new FormData()
  if (messageSelectList.commentSelect) {
    hasUnread.value.comment = false
    temp.append('messageType', 'comment')
    axiosInstance.post('/message/read', temp).then(() => {
    }).catch(() => {
      console.log('Comments read failed')
    })
  } else if (messageSelectList.chatSelect) {
    hasUnread.value.chat = false
    temp.append('messageType', 'chat')
    axiosInstance.post('/message/read', temp).then(() => {
    }).catch(() => {
      console.log('Chats read failed')
    })
  } else {
    hasUnread.value.comment = false
    temp.append('messageType', 'comment')
    axiosInstance.post('/message/read', temp).then(() => {
    }).catch(() => {
      console.log('Comments read failed')
    })
  }

  // get the corresponding data
  if (messageSelectList.commentSelect) {
    axiosInstance.get('/message/comment')
        .then(response => {
          // commentData.value = response.data.data;
          let tempList = response.data.data
          commentData.value = []
          for (let i = 0; i < tempList.length; i++) {
            let exData = JSON.parse(tempList[i].content)
            commentData.value.push({
              id: tempList[i].id,
              time: formatTime(tempList[i].time).split('.')[0],
              commenterId: tempList[i].from,
              commenterName: tempList[i].fromName,
              // TODO: communicate with the backend to get the ids
              eventId: exData.eventId === undefined ? -1 : exData.eventId,
              postId: exData.postId === undefined ? -1 : exData.postId,
              commentContent: exData.commentContent,
              oriCommentContent: exData.oriCommentContent
            })
          }
          console.log(commentData.value)
        })
        .catch(error => {
          console.error(error);
        });
  } else if (messageSelectList.chatSelect) {

  } else {
    axiosInstance.get('/message/comment')
        .then(response => {
          // commentData.value = response.data.data;
          let tempList = response.data.data
          commentData.value = []
          for (let i = 0; i < tempList.length; i++) {
            let exData = JSON.parse(tempList[i].content)
            commentData.value.push({
              id: tempList[i].id,
              time: formatTime(tempList[i].time).split('.')[0],
              commenterId: tempList[i].from,
              commenterName: tempList[i].fromName,
              eventId: exData.eventId === undefined ? -1 : exData.eventId,
              postId: exData.postId === undefined ? -1 : exData.postId,
              commentContent: exData.commentContent,
              oriCommentContent: exData.oriCommentContent
            })
          }
        })
        .catch(error => {
          console.error(error);
        });
  }
//
//
//   // test data
//
//   // commentData.value = [
//   //   {
//   //     id: 1,
//   //     time: '2023-07-01 19:45:00',
//   //     commenterId: 123456,
//   //     commenterName: '王煜然',
//   //     roomId: 3,
//   //     roomName: '湖畔三栋3楼326',
//   //     commentContent: '？哥们你真的要挨个房间问一遍吗',
//   //     oriCommentContent: '想找一起玩原神的舍友。',
//   //   },
//   //   {
//   //     id: 2,
//   //     time: '2023-06-01 19:45:00',
//   //     commenterId: 456789,
//   //     commenterName: 'Haoson',
//   //     roomId: 2,
//   //     roomName: '湖畔三栋3楼325',
//   //     commentContent: '我也想找一起玩原神的舍友。',
//   //     oriCommentContent: '我想找一起玩原神的舍友。',
//   //   },
//   //   {
//   //     id: 3,
//   //     time: '2023-05-01 19:45:00',
//   //     commenterId: 789123,
//   //     commenterName: 'Shinomiya',
//   //     roomId: 1,
//   //     roomName: '湖畔三栋3楼324',
//   //     commentContent: '你说的对，但是《原神》是由米哈游自主研发的一款全新开放世界冒险游戏。游戏发生在一个被称作「提瓦特」的幻想世界，在这里，被神选中的人将被授予「神之眼」，导引元素之力。你将扮演一位名为「旅行者」的神秘角色，在自由的旅行中邂逅性格各异、能力独特的同伴们，和他们一起击败强敌，找回失散的亲人——同时，逐步发掘「原神」的真相。',
//   //     oriCommentContent: '大家好啊，我想找一起玩原神的舍友。',
//   //   },
//   // ]
//
//
//
})

</script>

<template>
  <header-for-all/>
  <div class="wrap">
    <div class="button_list">
      <div :class="messageSelectList.commentSelect?'pressed_button':'unpressed_button'"
              @click="press_button('comments')">
        <div class="dot_wrap">
          <p>comments</p>
          <p :style="hasUnread.comment ? 'display: block':'display: none'">&nbsp;</p>
          <i :class="hasUnread.comment ? 'dot_show':'dot_unshow'"></i>
        </div>
      </div>
      <div :class="messageSelectList.chatSelect?'pressed_button':'unpressed_button'"
           @click="press_button('chats')"
           style="border-bottom-left-radius: 10px; border-bottom-right-radius: 10px">
        <div class="dot_wrap">
          <p>chats</p>
          <p :style="hasUnread.chat ? 'display: block':'display: none'">&nbsp;</p>
          <i :class="hasUnread.chat ? 'dot_show':'dot_unshow'"></i>
        </div>
      </div>
    </div>

    <div class="main_list" :style="messageSelectList.chatSelect ? 'overflow-y: hidden' : 'overflow-y: scroll'">

<!--      <div v-if="messageSelectList.commentSelect">-->
<!--        <div v-for="item in commentData" :key="item.id">-->
<!--          <CommentEntity :id="item.id.toString()" :time="item.time" :commenterId="item.commenterId.toString()"-->
<!--                         :commenterName="item.commenterName" :roomId="item.roomId" :roomName="item.roomName"-->
<!--                         :commentContent="item.commentContent" :oriCommentContent="item.oriCommentContent"/>-->
<!--        </div>-->
<!--      </div>-->

      <div v-if="messageSelectList.chatSelect">
        <chat/>
      </div>

      <div v-else>
        <div v-for="item in commentData" :key="item.id">
          <CommentEntity :id="item.id.toString()" :time="item.time" :commenterId="item.commenterId.toString()"
                         :commenterName="item.commenterName" :event-id="item.eventId" :post-id="item.postId"
                         :commentContent="item.commentContent" :oriCommentContent="item.oriCommentContent"/>
        </div>
      </div>

    </div>

  </div>

</template>

<style scoped>

.wrap {
  display: flex;
  flex-direction: row;
  height: 90vh;
}

.button_list {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  background: #ffffed;
  height: 105px;
  width: 15%;
  margin: 10px 10px;
  border-radius: 10px;
  border: 1px solid #ababab;
}



.unpressed_button {
  background-color: #ffffff;
  width: 100%;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  text-align: center;
  border-bottom: 1px solid #ababab;
}

.pressed_button {
  background-color: #ffd2d9;
  width: 100%;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-bottom: 1px solid #ababab;
}

.button_form {
  text-align: center;
}

.dot_wrap {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 100%;
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

.main_list {
  display: flex;
  flex-direction: column;
  background: #ffefef;
  flex: 4;
  padding: 20px;
  box-sizing: border-box;
  margin: 10px 10px;
  border-radius: 10px;
  overflow-y: hidden;
}

</style>


