<script setup>
import { ref, reactive, onMounted } from 'vue';
import EventCardBig from "@/components/Modules/event/EventCardBig.vue";
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import { useRouter } from "vue-router";
import PostCard from "@/components/User/pages/post/components/postsSquare/postCard.vue";
import * as API from "@/components/User/pages/main/mainApi";

const router = useRouter();

const input = ref('');
const eventIds = ref([]);
const postIds = ref([]);


const loadEventItems = async () => {
  const ids = [1, 2,3,4,5];
  for (const id of ids) {
    // const res = await API.getBriefEvent(id);
    // events.value.push(res);
    eventIds.value.push(id);
  }
};

const loadPostItems = async () => {
  const ids = [1, 2, 5, 7];
  for (const id of ids) {
    // const res = await API.getPost(id);
    // postItems.value.push(res.data);
    postIds.value.push(id);
  }
};

const onClear = () => {
  input.value = '';
};

const searchPost = () => {
  if (input.value.trim() !== '') {
    let url = router.resolve({ path: '/post/search', query: { content: input.value } }).href;
    window.open(url, '_blank');
  } else {
    alert('请输入搜索内容');
  }
};

const searchEvent = () => {
  if (input.value.trim() !== '') {
    let url = router.resolve({ path: '/event/search', query: { content: input.value } }).href;
    window.open(url, '_blank');
  } else {
    alert('请输入搜索内容');
  }
};

onMounted(async () => {
  await loadEventItems();
  await loadPostItems();
});
</script>

<template>
    <div class="common-layout-all">
      <HeaderForAll>
      </HeaderForAll>
      <el-row>
        <div class="container">
          <h1>校园活动平台</h1>
          <p>欢迎来到校园活动平台，发现最新最热门的校园活动！</p>
          <el-row gutter="10" align="middle">
            <el-col :span="12">
              <el-input
                  v-model="input"
                  placeholder="搜索你感兴趣的活动或帖子！"
                  clearable
                  @clear="onClear"
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
      <h1>热门活动</h1>
        <el-row :class="main-main" gutter="10">
            <el-col :span="24">
                    <el-row>
                        <el-col>
                            <el-card style="border-radius: 0.5vw">
                                <el-carousel height="40vh" motion-blur interval="6000">
<!--                                    <el-carousel-item v-for="item in events" :key="item.id">-->
<!--                                      <div class="event-card-wrapper">-->
<!--                                        <event-card-big :id="item.eventId" />-->
<!--                                      </div>-->
<!--                                    </el-carousel-item>-->
                                  <el-carousel-item v-for="item in eventIds" :key="item.id">
                                    <div class="event-card-wrapper">
                                      <event-card-big :id="item" />
                                    </div>
                                  </el-carousel-item>
                                </el-carousel>
                            </el-card>
                        </el-col>
                    </el-row>
            </el-col>
        </el-row>
      <h1>热门帖子</h1>
      <el-row>
        <el-col :span="24">
          <div>
            <post-card v-for="item in postIds" :key="item" :post-i-d="item" />
          </div>
        </el-col>
      </el-row>
    </div>
</template>

<style scoped>
/* scoped */
.common-layout-all {
  height: 100vh;
}

.common-layout {
  height: 80vh;
}

.el-row {
  border-radius: 0.5vw;
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 0.5vw;
}

.el-carousel__item {
  display: flex;
  justify-content: center;
  align-items: center;
}

.event-card-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.infinite-list {
  height: 300px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.infinite-list .infinite-list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  background: var(--el-color-primary-light-9);
  margin: 10px;
  color: var(--el-color-primary);
}

.infinite-list .infinite-list-item + .list-item {
  margin-top: 10px;
}


</style>
