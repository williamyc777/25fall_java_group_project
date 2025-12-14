<script setup>
import {ref} from "vue";
import Avatar from "@/components/Modules/avatar/Avatar.vue";
import subCommentEntity from "@/components/Modules/comment/SubCommentEntity.vue";
import axiosInstance from "@/utils/axios";
import {ChatDotSquare} from "@element-plus/icons-vue";


const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  commenterId: {
    type: Number,
    required: true
  },
  commenterName: {
    type: String,
    required: true
  },
  // TODO: remove roomId
  // roomId: {
  //   type: Number,
  //   required: true
  // },
  // commentId: {
  //   type: Number,
  //   required: true
  // },
  postId: {
    type: Number,
    required: true
  },
  commentContent: {
    type: String,
    required: true
  },
  subComments: {
    type: Array,
    required: true
  }
})

let isReplying = ref(false)
let replyContent = ref('')

const user = ref(localStorage.getItem('userId'))

function changeReplyingState() {
  console.log('isReplying:', isReplying.value)
  isReplying.value = !isReplying.value
}

function Reply(commentId) {
  let temp = new FormData()
  temp.append('commentId', commentId)
  temp.append('comment', replyContent.value)
  axiosInstance.post('/user/reply', temp).then((res) => {
    console.log(res.data)
    isReplying.value = false
    replyContent.value = ''
  }).catch((err) => {
    console.log(err)
  })
  changeReplyingState()
}

</script>

<template>
  <div class="entity">
    <div class="header">
      <div class="person">
        <avatar :user-id="commenterId"/>
        <p style="font-size: 15px; font-weight: bold; margin-left: 10px; margin-top: 5px">{{commenterName}}</p>
      </div>
    </div>

    <div class="content">
      <p class="comment_content">{{commentContent}}</p>
    </div>

    <div style="display: flex; align-items: flex-start">
      <el-icon @click="changeReplyingState" style="margin-left: 20px; margin-top: 5px; margin-bottom: 8px;" size="20"><ChatDotSquare /></el-icon>
    </div>

    <div v-if="isReplying" class="reply-input">
      <el-row>
        <el-col :span="2">
          <Avatar :user-id="user"/>
        </el-col>
        <el-col :span="19">
          <input type="text" placeholder="reply here" name="text" class="input" v-model="replyContent">
        </el-col>
        <el-col :span="2">
          <button @click="Reply(id)">
            <div class="svg-wrapper-1">
              <div class="svg-wrapper">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="24"
                    height="24"
                >
                  <path fill="none" d="M0 0h24v24H0z"></path>
                  <path
                      fill="currentColor"
                      d="M1.946 9.315c-.522-.174-.527-.455.01-.634l19.087-6.362c.529-.176.832.12.684.638l-5.454 19.086c-.15.529-.455.547-.679.045L12 14l6-8-8 6-8.054-2.685z"
                  ></path>
                </svg>
              </div>
            </div>
            <span>Reply</span>
          </button>
        </el-col>
      </el-row>
    </div>

    <div class="subComment">
      <subCommentEntity
          v-for="subComment in subComments"
          :key="subComment.id"
          :id="subComment.id"
          :student-id="subComment.userId"
          :student-name="subComment.userName"
          :underCommentID="subComment.underCommentId"
          :to-comment-id="subComment.toCommentId"
          :to-student-name="subComment.toUserName"
          :commentContent="subComment.comment"
      />
    </div>
  </div>

</template>

<style scoped>

.entity {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  border-radius: 10px;
  margin-bottom: 10px;

}

.header {
  margin-top: 20px;
  display: flex;
  flex-direction: row;
  align-items: center;
  height: 30px;
}

.person {
  margin: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.content {
  margin: 0 20px 0 20px;
  padding-left: 20px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
}

.comment_content {
  text-align: left;
  margin-top: 5px;
  margin-bottom: 5px;
}

.subComment{
  border-radius: 10px;
  margin-left: 30px;
  margin-right: 30px;
  width: 95%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.reply-input {
  margin: 10px;
  width: 100%;
}

.input {
  border-radius: 10px;
  outline: 2px solid #3e55e1;
  border: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  background-color: #e2e2e2;
  outline-offset: 3px;
  padding: 10px 1rem;
  transition: 0.25s;
  width: 90%;
}

.input:focus {
  outline-offset: 5px;
  background-color: #fff
}

button {
  transform: scale(0.9);
  font-family: inherit;
  font-size: 18px;
  background: royalblue;
  color: white;
  padding: 0.7em 1em;
  margin-top: -5px;
  margin-left: -20px;
  display: flex;
  align-items: center;
  border: none;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.2s;
  cursor: pointer;
}

button span {
  display: block;
  margin-left: 0.3em;
  transition: all 0.3s ease-in-out;
}

button svg {
  display: block;
  transform-origin: center center;
  transition: transform 0.3s ease-in-out;
}

button:hover .svg-wrapper {
  animation: fly-1 0.6s ease-in-out infinite alternate;
}

button:hover svg {
  transform: translateX(1.2em) rotate(45deg) scale(1.1);
}

button:hover span {
  transform: translateX(5em);
}

button:active {
  transform: scale(0.95);
}

@keyframes fly-1 {
  from {
    transform: translateY(0.1em);
  }

  to {
    transform: translateY(-0.1em);
  }
}



</style>