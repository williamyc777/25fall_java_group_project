<script setup>
import { ref, reactive, onMounted } from 'vue'
import VMdEditor from '@kangc/v-md-editor'
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";

import axiosInstance from "@/utils/axios";
import router from "@/utils/router";


let imageUrl = ref('')

let mdText = ref('')

const form = reactive({
  title: '',
  name: '',
  type: '',
  applyStartTime: '',
  applyEndTime: '',
  startTime: '',
  endTime: '',
  introduction: '',

  limitCount: '',
  seatSet: '',
})

const rules = reactive({
  title: [
    { required: true, message: '请输入活动标题', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入活动名', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择活动类型', trigger: 'change' }
  ],
  applyStartTime: [
    { required: true, message: '请选择报名开始时间', trigger: 'change' }
  ],
  applyEndTime: [
    { required: true, message: '请选择报名结束时间', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择活动开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择活动结束时间', trigger: 'change' }
  ],
  introduction: [
    { required: true, message: '请输入活动简介', trigger: 'blur' }
  ]
})

function beforeUpload(file) {
  console.log(file.name)
  let temp = new FormData()
  temp.append('file', file.raw)
  axiosInstance.post('/image/upload', temp).then((res) => {
    console.log(res.data)
    imageUrl.value = res.data.data
    // cat the host and the path
    // imageUrl.value = axiosInstance.defaults.url + imageUrl.value
  }).catch((err) => {
    console.log(err)
  })
  // imageUrl.value = URL.createObjectURL(file.raw)
  console.log(imageUrl.value)
  return false
}

function handleSuccess(response, file, fileList) {
  console.log(response)
  imageUrl.value = URL.createObjectURL(file.raw)
}




let eventTypes = [
  { value: '1', label: '计数式' },
  { value: '3', label: '自定义报名' },
]

let pickerOptions = {
  shortcuts: [
    {
      text: '今天',
      onClick(picker) {
        picker.$emit('pick', new Date())
      }
    },
    {
      text: '昨天',
      onClick(picker) {
        const date = new Date()
        date.setTime(date.getTime() - 3600 * 1000 * 24)
        picker.$emit('pick', date)
      }
    },
    {
      text: '一周前',
      onClick(picker) {
        const date = new Date()
        date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
        picker.$emit('pick', date)
      }
    }
  ]
}

let definedForm = ref([])
let formVisible = ref(false)

function deleteForm(id) {
  let found = false
  for (let i = 0; i < definedForm.value.length; i++) {
    if (found) {
      definedForm.value[i].id -= 1
      continue
    }
    if (definedForm.value[i].id === id) {
      definedForm.value.splice(i, 1)
      found = true
      if (definedForm.value.length !== 0 && id < definedForm.value.length) {
        definedForm.value[i].id -= 1
      }
    }
  }
}

function formClick() {
  formVisible.value = true
}

function formApply() {
  formVisible.value = false
}

function formCancel() {
  formVisible.value = false
}


let newFormEntryVisible = ref(false)
let newFormEntryName = ref('')
let newFormEntryType = ref('')
let newFormEntryOptions = ref('')
let newFormEntryRequired = ref(false)

function addNewFormEntryClick() {
  newFormEntryVisible.value = true
}

function addNewFormEntryApply() {
  if (newFormEntryType.value === 'input') {
    definedForm.value.push({
      id: definedForm.value.length,
      name: newFormEntryName.value,
      type: 'input',
      required: newFormEntryRequired.value
    })
  } else {
    definedForm.value.push({
      id: definedForm.value.length,
      name: newFormEntryName.value,
      type: 'select',
      options: newFormEntryOptions.value.split('\n'),
      required: newFormEntryRequired.value
    })
  }
  newFormEntryName.value = ''
  newFormEntryType.value = ''
  newFormEntryOptions.value = ''
  newFormEntryRequired.value = false

  newFormEntryVisible.value = false
}


let editSelectVisible = ref(false)
let currentEditSelectId = ref(0)
let editSelectTable = ref([])
function reTableEditSelect() {
  editSelectTable.value = []
  for (let i = 0; i < definedForm.value[currentEditSelectId.value].options.length; i++) {
    editSelectTable.value.push({
      id: i,
      name: definedForm.value[currentEditSelectId.value].options[i]
    })
  }
}
function editSelectClick(id) {
  currentEditSelectId.value = id
  reTableEditSelect()
  editSelectVisible.value = true

}

function deleteOptionForEditSelect(index) {
  definedForm.value[currentEditSelectId.value].options.splice(index, 1)
  reTableEditSelect()
}

let newOptionForEditSelect = ref('')
function addOptionForEditSelect() {
  definedForm.value[currentEditSelectId.value].options.push(newOptionForEditSelect.value)
  newOptionForEditSelect.value = ''
  reTableEditSelect()
}

function editSelectApply() {
  editSelectVisible.value = false
}

function editSelectCancel() {
  editSelectVisible.value = false
}

// turn Sat Jul 06 2024 00:00:00 GMT+0800 (中国标准时间) to 2024-07-06 00:00:00
function formatTime(str) {
  let date = str.toString().split(' ')
  let month = ''
  switch (date[1]) {
    case 'Jan':
      month = '01'
      break
    case 'Feb':
      month = '02'
      break
    case 'Mar':
      month = '03'
      break
    case 'Apr':
      month = '04'
      break
    case 'May':
      month = '05'
      break
    case 'Jun':
      month = '06'
      break
    case 'Jul':
      month = '07'
      break
    case 'Aug':
      month = '08'
      break
    case 'Sep':
      month = '09'
      break
    case 'Oct':
      month = '10'
      break
    case 'Nov':
      month = '11'
      break
    case 'Dec':
      month = '12'
      break
  }
  let day = date[2]
  let year = date[3]
  let time = date[4]
  return year + '-' + month + '-' + day + ' ' + time
}

function createEventClick() {
  let temp = {
    title: form.title,
    name: form.name,
    applyStartTime: formatTime(form.applyStartTime),
    applyEndTime: formatTime(form.applyEndTime),
    startTime: formatTime(form.startTime),
    endTime: formatTime(form.endTime),
    introduction: form.introduction,
    imageUrl: imageUrl.value,
    mdText: mdText.value,
    enrollmentType: '',
  }
  if (form.type === '1') {
    temp.enrollmentType = 'count'
    if (form.limitCount === '') {
      temp.limitCount = 0
    } else {
      temp.limitCount = Number(form.limitCount)
    }
  } else if (form.type === '2') {
    temp.enrollmentType = 'select'
  } else {
    temp.enrollmentType = 'form'
    temp.limitCount = 0
    temp.definedForm = definedForm.value
  }
  let jsonContent = JSON.stringify(temp)
  // jsonContent = "{\"title\": \"asdf\"}"
  axiosInstance.post('/event/create', jsonContent).then((res) => {
    console.log(res)
    if (res.data.code === 0) {
      alert('创建成功')
      router.push({path: '/event/manage'})
    } else {
      alert('创建失败')
    }
  }).catch((err) => {
    console.log(err)
  })
}

function mdUploadImage(event, insertImage, files) {
  let temp = new FormData()
  temp.append('file', files[0])
  axiosInstance.post('/image/upload', temp).then((res) => {
    insertImage({
      url: res.data.data,
      desc: 'Description'
    })
  }).catch((err) => {
    console.log(err)
  })
}


onMounted(() => {
  // definedForm.value = [
  //   {
  //     id: 0,
  //     name: '姓名',
  //     type: 'input',
  //     required: true,
  //   },
  //   {
  //     id: 1,
  //     name: '学号',
  //     type: 'input',
  //     required: false,
  //   },
  //   {
  //     id: 2,
  //     name: '年级',
  //     type: 'select',
  //     options: ['大一', '大二', '大三', '大四'],
  //     required: true,
  //   }
  // ]




  // mdText.value = 'sdf\n' +
  //     '### Title\n' +
  //     '\n' +
  //     '![Description](https://github.com/LampTales/YuxiaLin/raw/main/pics/lin.jpg){{{width="200" height="auto"}}}'

})

</script>

<template>
  <div>
    <header-for-all/>
  </div>
  <div style="overflow-y: scroll; height: 87vh; margin-top: 20px"
  >
    <div>
      <el-form :model="form" :rules="rules" label-width="120px">
        <el-form-item label="活动标题" prop="title" style="width: 600px">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="活动名" prop="name" style="width: 600px">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="活动类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择活动类型">
            <el-option v-for="item in eventTypes" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>

        <div v-if="form.type === '1'">
          <el-form-item label="活动限制人数" prop="limitCount" style="width: 250px">
            <el-input v-model="form.limitCount"></el-input>
          </el-form-item>
        </div>

        <div v-if="form.type === '2'">
          <el-form-item label="座位" prop="seatCount" style="width: 400px">
            <el-input v-model="form.seatSet"></el-input>
          </el-form-item>
        </div>

        <div v-if="form.type === '3'" style="margin-left: 120px; margin-bottom: 20px">
          <el-button type="primary" @click="formClick">自定义表单</el-button>
        </div>

        <el-form-item label="报名开始时间" prop="applyStartTime">
          <el-date-picker
            v-model="form.applyStartTime"
            type="datetime"
            placeholder="选择日期和时间"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报名结束时间" prop="applyEndTime">
          <el-date-picker
            v-model="form.applyEndTime"
            type="datetime"
            placeholder="选择日期和时间"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择日期和时间"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择日期和时间"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="活动封面" style="width: 600px">
          <el-upload
            class="avatar-uploader"
            action=""
            :auto-upload="false"
            :show-file-list="false"
            :on-success="handleSuccess"
            :on-change="beforeUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar"/>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>

        <el-form-item label="活动简介" prop="introduction" style="width: 600px">
          <el-input type="textarea" v-model="form.introduction"></el-input>
        </el-form-item>

      </el-form>
    </div>


    <div>
      <p style="margin-left: 25px; font-size: 14px; color: #606266"
      >活动正文</p>
    </div>
    <div style="width: 90%; display: flex; flex-direction: row; justify-content: center; margin-left: 50px">
      <v-md-editor
          v-model="mdText"
          @upload-image="mdUploadImage"
          :disabled-menus="[]"
      ></v-md-editor>
    </div>

    <div style="margin-top: 30px; display: flex; flex-direction: row; justify-content: center;">
      <el-button type="primary" @click="createEventClick"
      >发起活动</el-button>
    </div>
  </div>

  <el-dialog v-model="formVisible" title="自定义报名活动">
    <div>
      <el-form>
        <el-form-item
            v-for="item in definedForm"
            :key="item.name"
            :label="item.name"
        >
          <el-col span="12">
            <el-input v-if="item.type === 'input'" />
            <el-select v-else-if="item.type === 'select'" >
              <el-option
                  v-for="option in item.options"
                  :key="option"
                  :label="option"
                  :value="option"
              ></el-option>
            </el-select>
          </el-col>

          <el-col span="12" v-if="item.type === 'select'">
            <el-button type="primary"
                       @click="editSelectClick(item.id)"
                       style="margin-left: 30px"
            >编辑选项</el-button>
          </el-col>

          <el-col span="12">
            <el-button type="primary" @click="deleteForm(item.id)" style="margin-left: 30px">删除表项</el-button>
          </el-col>

        </el-form-item>
      </el-form>
    </div>

    <div style="margin-top: 20px; display: flex; flex-direction: row; justify-content: center">
      <el-button type="primary" style="margin-right: 20px" @click="addNewFormEntryClick">添加表项</el-button>
    </div>

    <div style="display: flex; flex-direction: row; justify-content: flex-end">
      <div style="margin-right: 20px">
        <el-button type="primary" @click="formApply">完成</el-button>
      </div>
    </div>
  </el-dialog>

  <el-dialog v-model="editSelectVisible" title="编辑选项">
    <div>
      <el-table :data="editSelectTable" style="width: 100%">
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column prop="name" label="选项内容"></el-table-column>
        <el-table-column label="操作">
          <template #default="row">
            <el-button @click="deleteOptionForEditSelect(row.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div style="margin-top: 20px; display: flex; flex-direction: row; justify-content: center">
      <el-input v-model="newOptionForEditSelect" style="width: 200px"></el-input>
      <el-button type="primary" @click="addOptionForEditSelect" style="margin-left: 30px"
      >添加选项</el-button>
    </div>

    <div style="display: flex; flex-direction: row; justify-content: flex-end; margin-top: 30px">
      <div style="margin-right: 20px">
        <el-button type="primary" @click="editSelectApply">确定</el-button>
      </div>
    </div>
  </el-dialog>

  <el-dialog v-model="newFormEntryVisible" title="添加表项">
    <div>
      <el-form>
        <el-form-item label="表项名称">
          <el-input v-model="newFormEntryName"></el-input>
        </el-form-item>
        <el-form-item label="表项类型">
          <el-select v-model="newFormEntryType">
            <el-option label="输入框" value="input"></el-option>
            <el-option label="下拉框" value="select"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选项" v-if="newFormEntryType === 'select'">
          <el-input v-model="newFormEntryOptions" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="是否必填">
          <el-switch v-model="newFormEntryRequired"></el-switch>
        </el-form-item>
      </el-form>
    </div>

    <div style="display: flex; flex-direction: row; justify-content: flex-end; margin-top: 30px">
      <div style="margin-right: 20px">
        <el-button type="primary" @click="addNewFormEntryApply">确定</el-button>
      </div>
    </div>
  </el-dialog>

</template>

<style scoped>
/deep/ .avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 254px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 254px;
  display: block;
}

</style>