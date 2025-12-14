<template>
  <div class="table-container">
    <div class="button-container">
      <button @click="showTable(1)" :class="{ active: activeTable === 1 }">切换活动表格</button>
      <button @click="showTable(2)" :class="{ active: activeTable === 2 }">切换帖子表格</button>
      <button @click="showTable(3)" :class="{ active: activeTable === 3 }">切换用户表格</button>
      <button @click="goToDoc">跳转到文档</button>
    </div>

    <div v-show="activeTable === 1">
      <el-table :data="events" style="width: 100%">
        <el-table-column prop="eventId" label="id" width="180"></el-table-column>
        <el-table-column prop="eventName" label="标题/活动" width="280"></el-table-column>
        <el-table-column prop="authorName" label="作者/用户" width="180"></el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button @click="deleteEvent(scope.row.eventId)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-show="activeTable === 2">
      <el-table :data="posts" style="width: 100%">
        <el-table-column prop="postID" label="id" width="180"></el-table-column>
        <el-table-column prop="postTitle" label="标题/帖子" width="380"></el-table-column>
        <el-table-column prop="username" label="作者/用户" width="180"></el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button @click="deletePost(scope.row.postID)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-show="activeTable === 3">
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="id" label="id" width="180"></el-table-column>
        <el-table-column prop="name" label="昵称" width="180"></el-table-column>
        <el-table-column prop="username" label="账号" width="180"></el-table-column>
        <el-table-column prop="password" label="密码" width="180"></el-table-column>
        <el-table-column prop="canCreate" label="能够创建" width="180"></el-table-column>
        <el-table-column prop="canEnroll" label="能够报名" width="180"></el-table-column>
        <el-table-column prop="canComment" label="能够评论" width="180"></el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button @click="showPermissionDialog(scope.row)" type="primary" size="small">设置权限</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="permissionDialogVisible" title="设置用户权限">
      <el-form :model="permissionForm">
        <el-form-item label="能够创建">
          <el-switch v-model="permissionForm.canCreate"></el-switch>
        </el-form-item>
        <el-form-item label="能够报名">
          <el-switch v-model="permissionForm.canEnroll"></el-switch>
        </el-form-item>
        <el-form-item label="能够评论">
          <el-switch v-model="permissionForm.canComment"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updatePermission">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import * as adminApi from '@/components/Admin/admin.js';
import { useRouter } from "vue-router";

const router = useRouter();
function goToDoc() {
  router.push('/docAdmin');
}

const activeTable = ref(1);
const eventIds = ref([]);
const events = ref([]);
const posts = ref([]);
const users = ref([]);

const permissionDialogVisible = ref(false);
const permissionForm = ref({
  userId: null,
  canCreate: false,
  canEnroll: false,
  canComment: false
});

function showPermissionDialog(user) {
  permissionForm.value = {
    userId: user.id,
    canCreate: user.canCreate,
    canEnroll: user.canEnroll,
    canComment: user.canComment
  };
  permissionDialogVisible.value = true;
}

async function updatePermission() {
  await adminApi.changePermission(permissionForm.value);
  permissionDialogVisible.value = false;
  await fetchData();
}


onMounted(() => {
  fetchData();
});

async function fetchData() {
  events.value = [];
  posts.value = [];
  eventIds.value = [];
  eventIds.value = await adminApi.getAllEvents();
  posts.value = await adminApi.getAllPosts();
  for (const id of eventIds.value) {
    events.value.push(await adminApi.getBriefEvent(id));
  }
  users.value = await adminApi.getAllUser();
}

async function deleteEvent(eventId) {
  await adminApi.deleteEvent(eventId);
  await fetchData();
}

async function deletePost(postId) {
  await adminApi.deletePost(postId);
  await fetchData();
  // console.log(postId);
}

function showTable(index) {
  activeTable.value = index;
}
</script>

<style scoped>
.table-container {
  position: relative;
}

.button-container {
  position: fixed;
  top: 20px;
  left: 20px;
}

.button-container button {
  margin-right: 10px;
}

.el-table {
  margin-top: 50px; /* 调整距离根据按钮高度 */
}

.active {
  font-weight: bold;
}
</style>
