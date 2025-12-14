<template>
  <div class="container">
    <!-- Header -->
    <HeaderForAll>
    </HeaderForAll>
    <el-row>
      <div class="container">
        <h1>校园活动平台</h1>
        <p>欢迎来到校园活动平台，发现最新最热门的校园活动！</p>
        <el-row gutter="10" align="middle">
          <el-col :span="12">
            <el-input
                v-model="searchInput"
                placeholder="搜索你感兴趣的活动或帖子！"
                clearable
                @clear="clearSearch"
            />
          </el-col>
          <el-col :span="6">
            <el-button @click="searchEvent" type="primary" block>搜索活动</el-button>
          </el-col>
          <el-col :span="6">
            <el-button @click="searchPost" type="primary" block>搜索帖子</el-button>
          </el-col>
        </el-row>
      </div>
    </el-row>
    <!-- Search Results -->
    <div v-if="postIds.length > 0">
      <div v-for="item in postIds" :key="item.id">
        <div class="event-card-wrapper">
          <post-card :post-i-d="item" />
        </div>
      </div>
    </div>
    <div v-else>
      <p>没有找到相关内容</p>
    </div>

  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import * as searchApi from '@/components/User/pages/main/searchApi.js';
import { useRoute } from "vue-router";
import { useRouter } from "vue-router";
import postCard from '@/components/User/pages/post/components/postsSquare/postCard.vue';

const route = useRoute();
const router = useRouter();

const content = ref(' ');
const postIds = ref([]);
const searchInput = ref('');

onMounted(async () => {
  content.value = route.query.content;
  if (typeof route.query.content === 'undefined') {
    content.value = '';
  }
  postIds.value = await searchApi.searchPost(content.value);
});

const clearSearch = () => {
  console.log('Search input cleared!');
  searchInput.value = '';
};

const searchPost = () => {
  if (searchInput.value.trim() !== '') {
    let url = router.resolve({ path: '/post/search', query: { content: searchInput.value } }).href;
    window.open(url, '_blank');
  } else {
    alert('请输入搜索内容');
  }
};

const searchEvent = () => {
  if (searchInput.value.trim() !== '') {
    let url = router.resolve({ path: '/event/search', query: { content: searchInput.value } }).href;
    window.open(url, '_blank');
  } else {
    alert('请输入搜索内容');
  }
};

</script>

<style scoped>
/* Styles for Header, Search Bar, and Information Cards */
/* Customize according to your design */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-bar {
  margin-top: 20px;
}
</style>
