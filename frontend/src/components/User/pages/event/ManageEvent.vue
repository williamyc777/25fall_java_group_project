<script setup>
import { ref, reactive, onMounted } from 'vue'
import {useRouter} from "vue-router";
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import axiosInstance from "@/utils/axios";
import { formatTime } from "@/components/User/pages/message/utils";

const router = useRouter()

let eventList = ref([])


function createNewEvent() {
  router.push({path: '/event/create'})
}
function exportApplication(index) {
  // print the name
  console.log(eventList.value[index].eventId)
  axiosInstance.get('/event/getExcel', {
    responseType: 'blob',
    params: {
      id: eventList.value[index].eventId
    }
  }).then((res) => {
    console.log(res)
    const blob = new Blob([res.data], {type: 'application/vnd.ms-excel'})
    const downloadElement = document.createElement('a')
    const href = window.URL.createObjectURL(blob)
    downloadElement.href = href
    downloadElement.download = eventList.value[index].eventName + '报名表.xlsx'
    document.body.appendChild(downloadElement)
    downloadElement.click()
    document.body.removeChild(downloadElement)
    window.URL.revokeObjectURL(href)
  }).catch((err) => {
    console.log(err)
  })
}

onMounted(() => {
  axiosInstance.get('/event/hold').then((res) => {
    eventList.value = res.data.data
    for (let i = 0; i < eventList.value.length; i++) {
      if (eventList.value[i].enrollmentType === 'count') {
        eventList.value[i].type = '计数式'
      } else if (eventList.value[i].enrollmentType === 'seat') {
        eventList.value[i].type = '选座式'
      } else {
        eventList.value[i].type = '自定义报名'
      }
      eventList.value[i].applyStartTime = formatTime(eventList.value[i].applyStartTime).split(' ')[0]
      eventList.value[i].applyEndTime = formatTime(eventList.value[i].applyEndTime).split(' ')[0]
      eventList.value[i].startTime = formatTime(eventList.value[i].startTime).split(' ')[0]
      eventList.value[i].endTime = formatTime(eventList.value[i].endTime).split(' ')[0]

    }
  }).catch((err) => {
    console.log(err)
  })
})

// eventList.value = [
//   {
//     id: 1,
//     name: '活动1',
//     type: '计数式',
//     applyStartTime: '2024-04-01 00:00:00',
//     applyEndTime: '2024-04-10 00:00:00',
//     applyCnt: 100,
//     status: '报名中',
//   },
//   {
//     id: 2,
//     name: '活动2',
//     type: '选座式',
//     applyStartTime: '2024-04-01 00:00:00',
//     applyEndTime: '2024-04-10 00:00:00',
//     applyCnt: 100,
//     status: '未开始',
//   },
//   {
//     id: 3,
//     name: '活动3',
//     type: '自定义报名',
//     applyStartTime: '2024-04-01 00:00:00',
//     applyEndTime: '2024-04-10 00:00:00',
//     applyCnt: 100,
//     status: '已结束',
//   },
// ]

</script>

<template>
  <div>
    <HeaderForAll></HeaderForAll>
  </div>

  <div style="margin-left: 20px; margin-right: 20px; margin-top: 20px;"
  >
    <div style="margin-bottom: 30px; margin-top: 20px">
      <el-button @click="createNewEvent">创建新活动</el-button>
    </div>
    <div>
      <el-table :data="eventList" style="width: 100%">
        <el-table-column prop="eventId" label="活动编号"></el-table-column>
        <el-table-column prop="eventName" label="活动名称"></el-table-column>
        <el-table-column prop="type" label="活动类型"></el-table-column>
        <el-table-column prop="applyStartTime" label="报名开始时间"></el-table-column>
        <el-table-column prop="applyEndTime" label="报名结束时间"></el-table-column>
        <el-table-column prop="startTime" label="活动开始时间"></el-table-column>
        <el-table-column prop="endTime" label="活动结束时间"></el-table-column>
<!--        <el-table-column prop="applyCnt" label="报名人数"></el-table-column>-->
        <el-table-column label="操作">
          <template #default="row">
            <el-button @click="exportApplication(row.$index)">导出报名表</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

</template>

<style scoped>

</style>