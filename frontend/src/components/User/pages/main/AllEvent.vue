<script>
import {ref} from 'vue'
import postCardForM from "@/components/User/pages/main/components/postCardForM.vue";
import EventCard from "@/components/User/pages/main/components/EventCard.vue";
import * as API from "@/components/User/pages/main/mainApi"
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import EventCardBig from "@/components/Modules/event/EventCardBig.vue";

import {
    ArrowLeft,
    Delete,
    Edit, Search,
    Share,
} from "@element-plus/icons";

export default {
    computed: {
        Search() {
            return Search
        },
        Delete() {
            return Delete
        },
        Share() {
            return Share
        },
        Edit() {
            return Edit
        },
        ArrowLeft() {
            return ArrowLeft
        }
    },
    components: {
      EventCardBig,
      // eslint-disable-next-line vue/no-unused-components
      EventCard,
      HeaderForAll,
      postCardForM
    },

  data() {
    return {
      input: '' ,// 绑定搜索框的输入值
      eventItems: [],
      event:{},
      eventIds: []
    };
  },

  async created() {
    await this.loadEventItems();
  },

  methods: {
      async loadEventItems() {
        this.eventIds = await API.getAllEvent();
    },
  }
}
</script>

<template>
    <div class="common-layout-all">
      <HeaderForAll>
      </HeaderForAll>
      <el-row>
        <div class="container">
          <h1>所有活动</h1>
          <p>欢迎来到校园活动平台，发现最新最热门的校园活动！</p>
        </div>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div v-for="item in eventIds" :key="item.id">
            <event-card-big :id="item" />
          </div>
        </el-col>
      </el-row>
    </div>
</template>

<style scoped>
/* 整个页面的设置 */
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

/* 整个页面的设置 */
/* 走马灯 */
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

/* 走马灯 */
/* 无限滚动列表 */
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

.container {
  background-image: url('../../../../assets/star.png');
  /* 可以添加其他背景图片的样式，比如平铺、大小等 */
  /* background-size: cover; */
  /* background-repeat: no-repeat; */
  /* background-position: center center; */
}

</style>
