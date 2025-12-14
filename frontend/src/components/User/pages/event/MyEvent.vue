<script setup>
import { ref, reactive, onMounted } from 'vue'
import {useRouter} from "vue-router";
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import EventCardBig from "@/components/Modules/event/EventCardBig.vue";
import axiosInstance from "@/utils/axios";

let favorList = ref([])
let applyList = ref([])

onMounted(() => {
  // TODO: test the following code
  axiosInstance.get('/event/favored').then((res) => {
    favorList.value = res.data.data
  }).catch((err) => {
    console.log(err)
  })
  axiosInstance.get('/event/applied').then((res) => {
    applyList.value = res.data.data
  }).catch((err) => {
    console.log(err)
  })
  // for (let i = 0; i < 10; i++) {
  //   favorList.value.push({
  //     id: '123'
  //   })
  // }
  // for (let i = 0; i < 10; i++) {
  //   applyList.value.push({
  //     id: '123'
  //   })
  // }
})

</script>

<template>
  <div>
    <HeaderForAll></HeaderForAll>
  </div>

  <div class="whole-warp">
    <div style="margin-right: 60px; overflow-y: auto; height: 87vh;"
    >
      <p style="font-size: 20px; font-weight: bold; margin-top: 20px; margin-bottom: 20px;"
      >我收藏的活动</p>

      <div v-for="item in favorList" :key="item.id" style="margin-top: 20px"
      >
        <EventCardBig :id="item.toString()"></EventCardBig>
      </div>
    </div>

    <div style="margin-right: 60px; overflow-y: auto; height: 87vh;"
    >
      <p style="font-size: 20px; font-weight: bold; margin-top: 20px; margin-bottom: 20px;"
      >我报名的活动</p>

      <div v-for="item in applyList" :key="item.id" style="margin-top: 20px"
      >
        <EventCardBig :id="item.toString()"></EventCardBig>
      </div>
    </div>

  </div>

</template>

<style scoped>
.whole-warp {
  margin-left: 20px;
  margin-right: 20px;
  margin-top: 20px;
  display: flex;
  flex-direction: row;
}
</style>