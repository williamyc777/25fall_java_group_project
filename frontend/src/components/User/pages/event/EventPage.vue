<script setup>
import { ref, onMounted } from 'vue'
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import Comment from '@/components/Modules/comment/Comment.vue'
import Avatar from '@/components/Modules/avatar/Avatar.vue'
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import SimplePost from "@/components/Modules/SimplePost.vue";
import { formatTime } from "@/components/User/pages/message/utils";

import {useRoute, useRouter} from "vue-router";
import AvatarWithName from "@/components/Modules/avatar/AvatarWithName.vue";
import axiosInstance from "@/utils/axios";
const router = useRouter()
const route = useRoute()

const eventId = route.query.id
console.log(eventId)

let title = ref('')
let eventName = ref('')
let authorId = ref(0)
let authorName = ref('')
let applyStartTime = ref('')
let applyEndTime = ref('')
let startTime = ref('')
let endTime = ref('')
let grade = ref(0)
let posterUrl = ref('')
let text = ref('')

let postList = ref([])

let liked = ref(false)
let stars = ref('')

let eventType = ''
let countVisible = ref(false)
let selectVisible = ref(false)
let formVisible = ref(false)

// count attributes
let currentCount = ref(0)
let limitCount = ref('')

// select attributes
//TODO: select attributes
let selectConstraint = ref('')

// form attributes
let definedForm = ref([])
const appliedForm = ref([])


function clickLike() {
  liked.value = !liked.value
  console.log(liked.value)
  let temp = new FormData()
  temp.append('id', eventId)
  if (liked.value) {
    axiosInstance.post('/event/favor', temp).then(response => {
      console.log(response)
    }).catch(error => {
      console.error(error)
    })
  } else {
    axiosInstance.post('/event/unfavor', temp).then(response => {
      console.log(response)
    }).catch(error => {
      console.error(error)
    })
  }
}

function clickWrite() {
  console.log('write')
  router.push({path: '/square', query: {eventID: eventId}})
}

function clickApply() {
  if (eventType === 'count') {
    console.log('count')
    countVisible.value = true
  } else if (eventType === 'select') {
    selectVisible.value = true
  } else if (eventType === 'form') {
    formVisible.value = true
  }
}

function clickCancel() {
  countVisible.value = false
  selectVisible.value = false
  formVisible.value = false
}

function countApply() {
  // currentCount.value += 1
  // alert('报名成功！')
  // countVisible.value = false
  if (currentCount.value >= limitCount.value) {
    alert('报名人数已满！')
    return
  }
  let temp = new FormData()
  temp.append('id', eventId)
  axiosInstance.post('/event/apply', temp).then(response => {
    console.log(response)
    if (response.data.code === 0) {
      alert('报名成功！')
      currentCount.value += 1
    } else {
      alert('报名失败！')
    }
  }).catch(error => {
    console.error(error)
  })
  countVisible.value = false
}

function formApply() {
  console.log(appliedForm.value)
  for (let i = 0; i < appliedForm.value.length; i++) {
    console.log(appliedForm.value[i].name + ': ' + appliedForm.value[i].value.toString())
  }

  for (let i = 0; i < appliedForm.value.length; i++) {
    if (appliedForm.value[i].value === '' && definedForm.value[i].required) {
      alert('请填写：' + appliedForm.value[i].name)
      return
    }
  }

  let temp = new FormData()
  temp.append('id', eventId)

  // TODO: check the correctness
  for (let i = 0; i < appliedForm.value.length; i++) {
    temp.append('formValues[]', appliedForm.value[i].value)
  }

  axiosInstance.post('/event/apply', temp).then(response => {
    console.log(response)
    if (response.data.code === 0) {
      alert('报名成功！')
    } else {
      alert('报名失败！')
    }
  }).catch(error => {
    console.error(error)
  })

  for (let i = 0; i < appliedForm.value.length; i++) {
    appliedForm.value[i].value = ''
  }
  formVisible.value = false
}

let drawAuthor = ref(false)
onMounted(() => {
  axiosInstance.get('/event/detail', {
    params: {
      id: eventId
    }
  }).then(response => {
    let temp = response.data.data
    title.value = temp.title
    eventName.value = temp.eventName
    authorId.value = temp.authorId
    authorName.value = temp.authorName
    applyStartTime.value = formatTime(temp.applyStartTime)
    applyEndTime.value = formatTime(temp.applyEndTime)
    startTime.value = formatTime(temp.startTime)
    endTime.value = formatTime(temp.endTime)
    grade.value = temp.score
    posterUrl.value = temp.postUrl
    liked.value = temp.liked


    stars.value = '⭐'
    for (let i = 1; i < grade.value; i++) {
      stars.value += '⭐'
    }

    text.value = temp.text

    eventType = temp.enrollmentType
    if (eventType === 'count') {
      currentCount.value = temp.currentCount
      let limit = temp.limit
      if (limit === -1) {
        limitCount.value = '无限制'
      } else {
        limitCount.value = limit.toString()
      }
    } else if (eventType === 'select') {
      selectConstraint.value = temp.selectConstraint
    } else if (eventType === 'form') {
      // TODO: need to be checked the correctness
      definedForm.value = temp.definedForm
      for (let i = 0; i < definedForm.value.length; i++) {
        appliedForm.value.push({
          id: definedForm.value[i].id,
          name: definedForm.value[i].name,
          value: ''
        })
      }
    }

    // TODO: need to be checked the correctness
    postList.value = temp.postList

    drawAuthor.value = true

  }).catch(error => {
    console.error(error);
  });

})


function showGrade(newGrade) {
  console.log(newGrade)

  let temp = new FormData()
  temp.append('id', eventId)
  temp.append('grade', newGrade)
  axiosInstance.post('/event/grade', temp).then(response => {
    console.log(response)
  }).catch(error => {
    console.error(error)
  })
}

</script>

<template>
  <div>
    <header-for-all/>
  </div>
  <div class="main">
    <div class="left-body">
      <div>
        <h1 class="event-title">{{ title }}</h1>
      </div>

      <div class="name-time-wrap">
        <p style="margin-top: 5px; margin-bottom: 5px; color: #3abbff;">{{ eventName }}</p>
      </div>

      <div style="margin-top: 10px; margin-left: 5px; font-size: 12px">
        报名时间: {{applyStartTime}} - {{applyEndTime}}
      </div>

      <div style="margin-top: 10px; margin-left: 5px; font-size: 12px">
        活动时间: {{applyStartTime}} - {{applyEndTime}}
      </div>

      <div>
        <p style="margin-top: 10px"
        >{{ stars }}</p>
      </div>

      <div>
        <img :src="posterUrl" style="width: 400px"/>
      </div>

      <div>
        <v-md-preview :text="text"></v-md-preview>
      </div>

      <comment :event-id="Number(eventId)"></comment>


    </div>

    <div class="right-panel">
      <div v-if="drawAuthor"
          class="author-wrap">
        <avatar-with-name
            :user-id="authorId.toString()"
            :need-small="true"
            size-small="60px"
            :name="authorName"
        ></avatar-with-name>
      </div>
      <div>
        <p class="event-title">Related Posts</p>
      </div>

      <div v-for="post in postList">
        <simple-post :postID="post"></simple-post>
      </div>
    </div>

  </div>

  <div class="bottom-button">
    <input type="checkbox"
           :checked="liked"
           id="favorite"
           name="favorite-checkbox"
           value="favorite-button"
           class="liked-input"
           @click="clickLike"
    >
    <label for="favorite" class="liked-label">
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-heart"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path></svg>
      <div class="action">
        <span class="option-1">收藏</span>
        <span class="option-2">已收藏</span>
      </div>
    </label>
    <el-button type="primary"
               @click="clickApply"
                style="margin-left: 20px;"
    >我要参加</el-button>
    <el-button type="primary"
               @click="clickWrite"
               style="margin-left: 20px;"
    >我想发帖</el-button>

    <div class="rating" style="margin-left: 20px; margin-right: 20px">
      <input value="5" name="rate" id="star5" type="radio" v-model="grade" @click="showGrade(5)"/>
      <label title="text" for="star5"></label>
      <input value="4" name="rate" id="star4" type="radio" v-model="grade" @click="showGrade(4)"/>
      <label title="text" for="star4"></label>
      <input value="3" name="rate" id="star3" type="radio" v-model="grade" @click="showGrade(3)"/>
      <label title="text" for="star3"></label>
      <input value="2" name="rate" id="star2" type="radio" v-model="grade" @click="showGrade(2)"/>
      <label title="text" for="star2"></label>
      <input value="1" name="rate" id="star1" type="radio" v-model="grade" @click="showGrade(1)"/>
      <label title="text" for="star1"></label>
    </div>
  </div>

  <el-dialog v-model="countVisible" title="活动报名">
    <div>
      <p
      >当前报名人数: {{ currentCount }}/{{ limitCount }}</p>
    </div>

    <div style="display: flex; flex-direction: row; justify-content: flex-end">
      <div style="margin-right: 20px">
        <el-button type="primary" @click="countApply">报名</el-button>
      </div>
      <div>
        <el-button type="primary" @click="clickCancel">取消</el-button>
      </div>
    </div>

  </el-dialog>

  <el-dialog v-model="selectVisible" title="选座式活动">
    <span>选座式活动</span>
  </el-dialog>

  <el-dialog v-model="formVisible" title="自定义报名活动">
    <div>
      <el-form>
        <el-form-item v-for="item in definedForm" :key="item.name" :label="item.name">
          <el-input v-if="item.type === 'input'" v-model="appliedForm[item.id].value"/>
          <el-select v-else-if="item.type === 'select'" v-model="appliedForm[item.id].value">
            <el-option
                v-for="option in item.options"
                :key="option"
                :label="option"
                :value="option"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>

    <div style="display: flex; flex-direction: row; justify-content: flex-end">
      <div style="margin-right: 20px">
        <el-button type="primary" @click="formApply">报名</el-button>
      </div>
      <div>
        <el-button type="primary" @click="clickCancel">取消</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>

.main {
  width: 99vw;
  display: flex;
  flex-direction: row;
  overflow-y: scroll;
  height: 82vh;
}

.event-title {
  font-size: 20px;
}

.name-time-wrap {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  margin-top: 5px;
  margin-bottom: 5px;
  margin-left: 5px;
}

.left-body {
  width: 70%;
  margin-left: 20px;
}

.right-panel {
  margin-left: 20px;
  width: 25%;
}

.author-wrap {
  display: flex;
  flex-direction: row;
  align-items: center;
  height: auto;
  margin-bottom: 60px;
  margin-top: 20px;
}

.bottom-button {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  height: 7vh;
  margin-right: 100px;
  margin-left: 50px;
}




.rating:not(:checked) > input {
  position: absolute;
  appearance: none;
}

.rating:not(:checked) > label {
  float: right;
  cursor: pointer;
  font-size: 30px;
  color: #666;
}

.rating:not(:checked) > label:before {
  content: '★';
}

.rating > input:checked + label:hover,
.rating > input:checked + label:hover ~ label,
.rating > input:checked ~ label:hover,
.rating > input:checked ~ label:hover ~ label,
.rating > label:hover ~ input:checked ~ label {
  color: #e58e09;
}

.rating:not(:checked) > label:hover,
.rating:not(:checked) > label:hover ~ label {
  color: #ff9e0b;
}

.rating > input:checked ~ label {
  color: #ffa723;
}




.liked-label {
  background-color: white;
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 10px 15px 10px 10px;
  cursor: pointer;
  user-select: none;
  border-radius: 10px;
  box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
  color: black;
}

.liked-input {
  display: none;
}

.liked-input:checked + .liked-label svg {
  fill: hsl(0deg 100% 50%);
  stroke: hsl(0deg 100% 50%);
  animation: heartButton 1s;
}

@keyframes heartButton {
  0% {
    transform: scale(1);
  }

  25% {
    transform: scale(1.3);
  }

  50% {
    transform: scale(1);
  }

  75% {
    transform: scale(1.3);
  }

  100% {
    transform: scale(1);
  }
}

.liked-input + .liked-label .action {
  position: relative;
  overflow: hidden;
  display: grid;
}

.liked-input + .liked-label .action span {
  grid-column-start: 1;
  grid-column-end: 1;
  grid-row-start: 1;
  grid-row-end: 1;
  transition: all .5s;
}

.liked-input + .liked-label .action span.option-1 {
  transform: translate(0px,0%);
  opacity: 1;
}

.liked-input:checked + .liked-label .action span.option-1 {
  transform: translate(0px,-100%);
  opacity: 0;
}

.liked-input + .liked-label .action span.option-2 {
  transform: translate(0px,100%);
  opacity: 0;
}

.liked-input:checked + .liked-label .action span.option-2 {
  transform: translate(0px,0%);
  opacity: 1;
}
</style>