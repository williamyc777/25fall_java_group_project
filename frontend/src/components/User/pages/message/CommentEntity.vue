<script setup>
import Avatar from "@/components/Modules/avatar/Avatar.vue";
import router from "@/utils/router";

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  time: {
    type: String,
    required: true
  },
  commenterId: {
    type: String,
    required: true
  },
  commenterName: {
    type: String,
    required: true
  },
  eventId: {
    type: Number,
    required: false,
    default: -1
  },
  postId: {
    type: Number,
    required: false,
    default: -1
  },
  commentContent: {
    type: String,
    required: true
  },
  oriCommentContent: {
    type: String,
    required: true
  },
})

// function toRoom() {
//   router.push({path: '/detailInfo', query: {roomId: props.roomId}})
// }

// TODO: figure out how to pass post
function toOrigin() {
  if (props.eventId !== -1) {
    router.push({path: '/event', query: {id: props.eventId}})
  } else if (props.postId !== -1) {
    router.push({path: '/square/post', query: {id: props.postId}})
  }
}

</script>

<template>
  <div class="entity">
    <div class="header">
      <div class="person">
        <avatar :user-id="commenterId"/>
        <p style="font-size: 15px; font-weight: bold; margin-left: 10px;">{{commenterName}}</p>
      </div>
      <p style="cursor: default; color: #ffb2b2">&nbsp; replied you &nbsp;</p>
    </div>

    <div style="display: flex; align-items: flex-start; margin-top: 5px">
      <p style="font-size: 10px; margin-left: 10px;">{{time}}</p>
    </div>

    <div class="content">
      <p class="comment_content">{{commentContent}}</p>
    </div>

    <div class="ori_comment">
      <p class="ori_comment_content">Original comment: {{oriCommentContent}}</p>
    </div>

    <div class="button-wrap">
      <el-button style="background-color: #ffb2b2;" @click="toOrigin">
        <p style="color: white">See Details</p>
      </el-button>
    </div>
  </div>
</template>

<style scoped>

.entity {
  width: 100%;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
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

.room {
  font-style: italic;
  cursor: default;
}

.content {
  margin: 0 20px 0 20px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
}

.comment_content {
  text-align: left;
  margin-top: 5px;
  margin-bottom: 5px;
}

.ori_comment {
  margin: 0 20px 0 20px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
}

.ori_comment_content {
  text-align: left;
  font-style: italic;
  color: #c7c7c7;
  font-size: 14px;
  margin-top: 5px;
  margin-bottom: 5px;
}

.button-wrap {
  margin: 10px;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
}

.el-button {
  background-color: #ff93c9;
  border: none;
}

</style>