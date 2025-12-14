<script setup>

import {ref} from "vue";
import AvatarWithName from "@/components/Modules/avatar/AvatarWithName.vue";

// defineProps({
//   id: {
//     type: String,
//     required: true
//   },
// })

let title = ref('')
let eventName = ref('')
let introduction = ref('')
let author = ref('')
let authorId = ref('')

let applyStartTime = ref('')
let applyEndTime = ref('')
let startTime = ref('')
let endTime = ref('')

let score = ref(0)
let posterUrl = ref('')

// just for test
score = '4'
let stars = ref("")
stars = '⭐'
for (let i = 1; i < score; i++) {
  stars = stars + '⭐'
}

title = '某某活动马上就要开始了！'
eventName = '活动某某'
author = 'Lamptales'
authorId = '123456'

applyStartTime = '2024-4-4 00:00:00'
applyEndTime = '2024-4-14 00:00:00'
startTime = '2024-4-16 00:00:00'
endTime = '2024-4-26 00:00:00'

introduction = '加入绝地潜兵的行列吧！😆 ' +
    '成为维和部队的精英！🤠 ' +
    '见识奇异的生命体👽 ' +
    '让管理式民主惠及整个星系🤟 ' +
    '成为英雄，' +
    '成为传奇😃🤲😄 ' +
    '成为绝地潜兵！😃'
posterUrl = 'https://static.fotor.com.cn/assets/projects/pages/c3000361e65b4048ab8dd18e8c076c0e/fotor-86b1e566f1d74bf1870ac2c2a624390f.jpg'


</script>

<template>
  <el-card style="border-radius: 0.5vw" shadow="hover">
    <div class="warp">
      <div class="poster-warp">
        <img :src="posterUrl" alt="poster" style="width: 100%; height: auto;">
      </div>
      <div class="content-warp">

        <div style="display: flex; flex-direction: row; justify-content: space-between; align-items: center;">
          <p style="font-size: 20px; font-weight: bold"
          >{{title}}</p>
          <avatar-with-name :user-id="authorId" :name="author" margin-left="5px"/>
        </div>

        <div style="display: flex; flex-direction: row; align-items: center;">
          <p style="margin-top: 0; color: #3abbff"
          >{{eventName}}</p>
          <p style="margin-top: 0; margin-left: 20px"
          >{{stars}}</p>
        </div>

        <div style="margin-top: 0; margin-left: 5px; font-size: 12px">
          报名时间: {{applyStartTime}} - {{applyEndTime}}
        </div>

        <div style="margin-top: 10px; margin-left: 5px; font-size: 12px">
          活动时间: {{applyStartTime}} - {{applyEndTime}}
        </div>

        <div>
          <p style="margin-right: 30px"
          >{{introduction}}</p>
        </div>
      </div>
    </div>
  </el-card>

</template>

<style scoped>
  .warp {
    display: flex;
    flex-direction: row;
  }

  .poster-warp {
    margin-left: 5px;
    margin-right: 15px;
  }


</style>