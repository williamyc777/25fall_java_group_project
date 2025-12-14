<script setup>
import { ElMessage } from 'element-plus';
import 'element-plus/theme-chalk/el-message.css';
import PostComments from "@/components/User/pages/post/components/postDetail/postComments.vue";
import ProfileCard from "@/components/User/pages/post/components/profileCard.vue";
import { ArrowLeft, ArrowRight, Delete, MoreFilled, Open, Pointer, Share, StarFilled } from "@element-plus/icons";
import { ChatDotSquare } from "@element-plus/icons-vue";
import Comment from '@/components/Modules/comment/Comment.vue';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import { ref, onMounted, computed } from 'vue';
import EventCard from "@/components/User/pages/post/components/postDetail/eventCard.vue";
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import AvatarWithName from "@/components/Modules/avatar/AvatarWithName.vue";
import axios from 'axios';
import { useRoute, useRouter } from "vue-router";
import axiosInstance from "@/utils/axios";
import EventCardBig from "@/components/Modules/event/EventCardBig.vue";
import PostCard from "@/components/User/pages/post/components/postsSquare/postCard.vue";

const route = useRoute(); // Initialize route
const router = useRouter();

const postIds = ref([1, 2]);
// API call to get post data
const postID = route.query.id; // Example post ID
let postData = ref({});
let userData = ref({});
let eventData = ref({});

let postTitle = ref('');
let postContent = ref();
let postRelevantEventID = ref();
let username = ref();
let userID = ref();
let userBio = ref();
let userAvatar = ref();
let likeOrNot = ref();
let collectOrNot = ref();

onMounted(async () => {
  try {
    const response = await axiosInstance.get('/post/getFullPost', {
      params: {
        postID: postID
      }
    });
    let temp = response.data.data;
    postTitle.value = temp.postTitle;
    postContent.value = temp.postContent;
    postRelevantEventID.value = temp.postRelevantEventID;
    username.value = temp.username;
    userID.value = temp.userID;
    userBio.value = temp.userBio;
    userAvatar.value = temp.userAvatar;
    isLiked.value = temp.likeOrNot;
    isStarred.value = temp.collectOrNot;
    console.log(temp)
    console.log(postRelevantEventID.value)
  } catch (error) {
    await router.push({ path: '/notFound', query: { errorCode: 1 } });
    console.error('Failed to fetch post data:', error);
  } finally {
  }
});

// Right side buttons functionality
let isStarred = ref(false);
let isLiked = ref(false);
const toggleStar = async () => {
  if (isStarred.value === false) {
    try {
      await axiosInstance.post('/post/collectThePost', {
        postID: postID
      });
      isStarred.value = !isStarred.value;
    } catch (error) {
      console.error('Failed to collect the post:', error);
    }
  } else {
    try {
      await axiosInstance.post('/post/discollectThePost', {
        postID: postID
      });
      isStarred.value = !isStarred.value;
    } catch (error) {
      console.error('Failed to discollect the post:', error);
    }
  }
};

const toggleLike = async () => {
  if (isLiked.value === false) {
    try {
      await axiosInstance.post('/post/likeThePost', {
        postID: postID
      });
      isLiked.value = !isLiked.value;
    } catch (error) {
      console.error('Failed to like the post:', error);
    }
  } else {
    try {
      await axiosInstance.post('/post/dislikeThePost', {
        postID: postID
      });
      isLiked.value = !isLiked.value;
    } catch (error) {
      console.error('Failed to dislike the post:', error);
    }
  }
};

const toggleShare = async () => {
  try {
    await navigator.clipboard.writeText(window.location.href);
    ElMessage({
      message: 'URL has been copied to clipboard',
      type: 'success',
    });
  } catch (error) {
    console.error('Failed to copy URL:', error);
    ElMessage({
      message: 'Failed to copy URL',
      type: 'error',
    });
  }
};

const dialogVisible = ref(false);
const handleDelete = async () => {
  if (localStorage.getItem('userId').toString() === userID.value.toString()) {
    try {
      await axiosInstance({
        method: 'delete',
        url: '/admin/post',
        params: {
          postId: postID
        }
      });
      await router.push({ path: '/notFound', query: { errorCode: 1 } });
    } catch (error) {
      console.error('Failed to delete the post:', error);
    }
  } else {
    console.log('delete failed')
    console.log(localStorage.getItem('userId'))
    console.log(userID.value)
  }
};

const closeDialog = () => {
  dialogVisible.value = false;
};

const closePage = () => {
  window.close();
};

const commentSectionRef = ref(null);
const scrollToCommentSection = () => {
  commentSectionRef.value.$el.scrollIntoView({ behavior: 'smooth' });
};

const activeNames = ref(['contentFold']);
const handleChange = (val) => {
  console.log(val);
};
const toggleCollapse = () => {
  activeNames.value = activeNames.value.length ? [] : ['contentFold'];
};

// Computed property to check if the delete button should be disabled

</script>

<template>
  <div class="common-layout-all">
    <el-row :class="main-header">
      <el-col :span="24">
        <header-for-all />
      </el-col>

      <el-backtop :right="10" :bottom="10" />
    </el-row>
    <el-row :class="main-main" gutter="10">
      <el-col :span="3">
        <el-card>
          <div class="common-layout" style="height: 20vh">
            <el-container>
              <el-header :height="10">
                <h4>Tips</h4>
              </el-header>
              <el-main>
                <span>Here are the details of the post</span>
              </el-main>
            </el-container>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <!--subheader-->
        <el-affix offset="10">
          <el-row :justify="space-between">
            <el-col>
              <el-card>
                <el-row>
                  <el-col>
                    <span class="Title">{{postTitle}}</span>
                  </el-col>
                  <el-divider />
                  <el-col>
                    <el-row>
                      <!--profile-->
                      <el-col :span="3">
                        <profile-card :name="username" :id="userID" :avatar="userAvatar" :bio="userBio" ></profile-card>
                      </el-col>
                      <!--activity-->
                      <el-col :span="4" />
                      <el-col :span="8">
                        <event-card :id="postRelevantEventID"></event-card>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-card>
            </el-col>
          </el-row>
        </el-affix>
        <!--main-->
        <el-row>
          <el-col>
            <el-row>
              <!--gap-->
            </el-row>
            <el-row>
              <!--theme-->
              <el-col :span="24">
                <el-card>
                  <el-collapse v-model="activeNames">
                    <el-collapse-item title="Content" name="contentFold">
                      <v-md-preview :text="postContent"></v-md-preview>
                    </el-collapse-item>
                  </el-collapse>
                </el-card>
              </el-col>
            </el-row>
            <el-row></el-row>
            <el-row>
              <!--comment-->
              <el-col>
                <comment ref="commentSection" :postId="Number(postID)" />
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <!--buttons-->
        <el-row></el-row>
      </el-col>
      <!--aside-right-->
      <el-col :span="1">
        <el-affix :offset="10">
          <el-row gutter="10">
            <el-col style="margin-bottom: 4px">
              <el-button type="info" class="button-left" style="width: 100%;">?</el-button>
            </el-col>
            <el-col style="margin-bottom: 4px">
              <div v-show="!isLiked">
                <el-button type="primary" :icon="Pointer" @click="toggleLike" class="button-left" style="width: 100%;" plain />
              </div>
              <div v-show="isLiked">
                <el-button type="primary" :icon="Pointer" @click="toggleLike" class="button-left" style="width: 100%;" />
              </div>
            </el-col>
            <el-col style="margin-bottom: 4px">
              <div v-show="!isStarred">
                <el-button type="primary" :icon="StarFilled" @click="toggleStar" class="button-left" style="width: 100%;" plain />
              </div>
              <div v-show="isStarred">
                <el-button type="primary" :icon="StarFilled" @click="toggleStar" class="button-left" style="width: 100%;" />
              </div>
            </el-col>
            <el-col style="margin-bottom: 4px">
              <el-button type="primary" :icon="Share" @click="toggleShare" class="button-left" style="width: 100%;" plain />
            </el-col>
            <!--                        <el-col style="margin-bottom: 4px">-->
            <!--                            <el-button type="primary" :icon="ArrowLeft" class="button-left" style="width: 100%;" plain />-->
            <!--                        </el-col>-->
            <!--                        <el-col style="margin-bottom: 4px">-->
            <!--                            <el-button type="primary" :icon="ArrowRight" class="button-left" style="width: 100%;" plain />-->
            <!--                        </el-col>-->
            <el-col style="margin-bottom: 4px">
              <el-button type="primary" :icon="Open" @click="toggleCollapse" class="button-left" style="width: 100%;" plain />
            </el-col>
            <el-col style="margin-bottom: 4px">
              <el-button type="danger" :icon="Delete" @click="handleDelete" class="button-left" style="width: 100%;" />
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <!--                            <el-card style="border-radius: 0.5vw">-->
              <!--                                <el-carousel height="40vh" motion-blur interval="6000">-->
              <!--                                    <el-carousel-item v-for="item in postIds" :key="item.id">-->
              <!--                                        <div class="event-card-wrapper">-->
              <!--                                            <post-card :id="item" />-->
              <!--                                        </div>-->
              <!--                                    </el-carousel-item>-->
              <!--                                </el-carousel>-->
              <!--                            </el-card>-->
            </el-col>
          </el-row>
        </el-affix>
      </el-col>
    </el-row>
    <!--        <el-row :class="main-footer">-->
    <!--            footer-->
    <!--        </el-row>-->
  </div>

  <el-dialog :visible="dialogVisible" title="Post Deleted">
    <span>The post has been successfully deleted.</span>
    <span slot="footer" class="dialog-footer">
            <el-button @click="closePage">Close Page</el-button>
        </span>
  </el-dialog>
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

.Title {
  font-weight: bold;
  font-size: 20px;
}

.thumb-button {
  height: 40px;
}

/* 整个页面的设置 */
/* 弹出提示 */
.el-popper.is-customized .el-popper__content {
  /* 设置渐变色作为背景 */
  background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
  /* Set padding to ensure the height is 32px */
  padding: 6px 12px;
}

.el-popper.is-customized .el-popper__arrow::before {
  /* 设置箭头的渐变色 */
  background: linear-gradient(45deg, #b2e68d, #bce689);
  right: 0;
}

/* 弹出提示 */
</style>