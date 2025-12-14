<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElLoading, ElMessage } from 'element-plus'
import postCard from '@/components/User/pages/post/components/postsSquare/postCard.vue'
import {
    ArrowLeft,
    ArrowRight,
    Delete,
    Edit,
    Search,
    Share,
} from "@element-plus/icons";
import SimplePost from "@/components/Modules/SimplePost.vue";
import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
import '@kangc/v-md-editor/lib/style/codemirror-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';
import Codemirror from 'codemirror';
import 'codemirror/mode/markdown/markdown';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/css/css';
import 'codemirror/mode/htmlmixed/htmlmixed';
import 'codemirror/mode/vue/vue';
import 'codemirror/addon/edit/closebrackets';
import 'codemirror/addon/edit/closetag';
import 'codemirror/addon/edit/matchbrackets';
import 'codemirror/addon/display/placeholder';
import 'codemirror/addon/selection/active-line';
import 'codemirror/addon/scroll/simplescrollbars';
import 'codemirror/addon/scroll/simplescrollbars.css';
import 'codemirror/lib/codemirror.css';
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import { useRoute,useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();
VMdEditor.Codemirror = Codemirror;
VMdEditor.use(githubTheme, {
    Hljs: hljs,
});

const eventID = ref(route.query.eventID);

const bindingEventID = ref(route.query.eventID);
const postTitle = ref()
const markdownText = ref('')
const errorMessage = ref('')

const fullscreenLoading = ref(false)

const editDialogVisible = ref(!!route.query.eventID);
const shareDialogVisible = ref(false)
const imageDialogVisible = ref(false)

const postIDs = ref([]);
const postIds = ref([1,2,3]);
const currentPage = ref(1);
const pageSize = ref(8);

const paginatedPostIDs = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value;
    const end = start + pageSize.value;
    return postIDs.value.slice(start, end);
});

const goToSearch = () => {
    router.push({ path: '/post/search' })
}
const goToSquare = () => {
    router.push({ path: '/square' })
}
const goToCollect = () => {
    router.push({ path: '/square/post/collect' })
}
const goToHistory = () => {
    router.push({ path: '/square/post/write' })
}

const handlePageChange = (newPage) => {
    currentPage.value = newPage;
};

const handleEditPost = () => {
    editDialogVisible.value = true;
    imageDialogVisible.value = false;
}
const handleSharePost = () => {
    shareDialogVisible.value = true;
}

const handleImageUpload = () => {
    imageDialogVisible.value = true;
    editDialogVisible.value = false;
}

async function getAllPostOnSquare() {
    try {
        const response = await axiosInstance.get(`/post/getPostSquare`)
        const postData = response.data.data;
        postIDs.value = postData.map(post => post.postID);
    } catch (error) {
        console.error('Error fetching post data:', error);
    }
}

onMounted(() => {
    getAllPostOnSquare();
});

const postUpload = async () => {
    if (!postTitle.value) {
        errorMessage.value = 'Title is required.'
        ElMessage.error(errorMessage.value)
        return
    }
    if (postTitle.value.length > 30) {
        errorMessage.value = 'Title must be within 30 characters.'
        ElMessage.error(errorMessage.value)
        return
    }
    if (!/^\d+$/.test(bindingEventID.value)) {
        errorMessage.value = 'Relevant Event ID must be all numbers.'
        ElMessage.error(errorMessage.value)
        return
    }

    fullscreenLoading.value = true;
    try {
        const response = await axiosInstance.post('/post/releasePost', {
            postTitle: postTitle.value,
            postContent: markdownText.value,
            postRelevantEvent: bindingEventID.value,
        });
        if (response.data.code === 0) {
            ElMessage.success('Post uploaded successfully!');
            editDialogVisible.value = false;
            window.location.reload(); // 刷新页面
        } else {
            ElMessage.error(`Upload failed: ${response.data.msg}`);
        }
    } catch (error) {
        ElMessage.error('Failed to upload post.');
        console.error('Failed to upload post:', error);
    } finally {
        fullscreenLoading.value = false;
        imageDialogVisible.value = false;
        bindingEventID.value = "";
        postTitle.value = "";
        markdownText.value = "";
    }
}

import axiosInstance from "@/utils/axios"
import PostCard from "@/components/User/pages/post/components/postsSquare/postCard.vue";

const handleUploadImage = async (event, insertImage, files) => {
    console.log(files);

    if (files && files.length > 0) {
        const formData = new FormData();
        formData.append('file', files[0]);

        try {
            const response = await axiosInstance.post('/image/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            if (response.data.code === 0) {
                const imageUrl = `${response.data.data}`;
                insertImage({
                    url: imageUrl,
                    desc: 'sample',
                });
                console.log(imageUrl)
            } else {
                console.error('Upload failed:', response.data.msg);
            }
        } catch (error) {
            console.error('Failed to upload image:', error);
        }
    }
};
</script>

<template>
    <el-dialog
        v-model="shareDialogVisible"
        title="Share Post"
        width="30%"
    >
        <!-- Share Dialog Content -->
        <span>This is the content of the share dialog.</span>
        <!-- Footer Buttons -->
        <template #footer>
            <el-button @click="shareDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="shareDialogVisible = false">Confirm</el-button>
        </template>
    </el-dialog>

    <el-dialog
        v-model="editDialogVisible"
        title="Start mine!"
        width="1300"
        :before-close="handleClose"
    >
        <el-divider/>
        <h4>Title</h4>
        <el-input v-model="postTitle" placeholder="Enter Title"></el-input>
        <el-divider/>
        <h4>Relevant Event</h4>
        <el-input v-model="bindingEventID" placeholder="Enter Event ID"></el-input>
        <el-divider/>
        <h4>Content</h4>
        <v-md-editor v-model="markdownText" height="400px"
                     :disabled-menus="[]"
                     @upload-image="handleUploadImage"></v-md-editor>
        <el-dialog
            v-model="imageDialogVisible"
            width="500"
            title="Start mine!"
            append-to-body
        >
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="handleEditPost">Back</el-button>
                </div>
            </template>
        </el-dialog>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="editDialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="postUpload"  v-loading.fullscreen.lock="fullscreenLoading">
                    Upload
                </el-button>
            </div>
        </template>
    </el-dialog>

    <div class="common-layout-all">
        <el-row >
            <el-col :span="24">
                <header-for-all/>
            </el-col>

            <el-backtop :right="10" :bottom="10"/>
        </el-row>
        <el-row :class="main-main" gutter="10">
            <el-col :span="3">
                <el-card>
                    <div class="common-layout" style="height: 25vh">
                        <el-container>
                            <el-header :height="10">
                                <h4>Tips</h4>
                            </el-header>
                            <el-main>
                                <span>Here is the page where you can see all the posts.</span>
                            </el-main>
                        </el-container>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="15">
                <!--subheader-->
                <el-row :justify="space-between">
                    <el-col>
                        <el-affix :offset="10">
                            <el-card>
                                <el-row>
                                    <el-col :span="10">
                                        <el-button-group class="ml-4">
                                            <el-button type="primary" :icon="Edit" @click="handleEditPost"/>
                                            <el-button type="primary"  @click="goToSquare">Square</el-button>
                                            <el-button type="primary"  @click="goToCollect">Collection</el-button>
                                            <el-button type="primary"  @click="goToHistory" v-loading.fullscreen.lock="fullscreenLoading">History</el-button>
                                        </el-button-group>
                                    </el-col>
                                    <el-col :span="14">
                                        <el-button :icon="Search" round style="width: 100%" @click="goToSearch">Search</el-button>
                                    </el-col>
                                </el-row>
                            </el-card>
                        </el-affix>

                    </el-col>
                </el-row>
                <!--main-->
                <el-row>
                    <el-col :span="24">
                        <div>
                            <postCard v-for="postID in paginatedPostIDs" :key="postID" :post-i-d="postID"></postCard>
                        </div>
                    </el-col>
                </el-row>
                <!--pagination-->
                <el-row>
                    <el-col>
                        <el-affix offset="5" position="bottom">
                            <el-card style="border: none; display: flex; justify-content: center; align-items: center;"
                                     shadow="never">
                                <el-pagination
                                    background
                                    layout="prev, pager, next"
                                    :total="postIDs.length"
                                    :page-size="pageSize"
                                    @current-change="handlePageChange"
                                    :current-page="currentPage"
                                />
                            </el-card>
                        </el-affix>
                    </el-col>
                </el-row>
            </el-col>
            <!--aside-right-->
            <el-col :span="6">
                <el-affix :offset="10">
                    <el-row gutter="10">
                        <el-col>
                            <el-card>
                                <div class="common-layout" style="height: 20vh">
                                    <el-container>
                                        <el-header :height="10">
                                            <h4>Announcement</h4>
                                        </el-header>
                                        <el-main>
                                            <span>Amazing events and posts waiting for you!</span>
                                        </el-main>
                                    </el-container>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col>
                            <el-card style="border-radius: 0.5vw">
                                <el-carousel height="30vh" motion-blur interval="6000">
                                    <el-carousel-item v-for="item in postIds" :key="item.id">
                                        <div class="event-card-wrapper">
                                            <simple-post :post-i-d="item" />
                                        </div>
                                    </el-carousel-item>
                                </el-carousel>
                            </el-card>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col>
                        </el-col>
                    </el-row>
                </el-affix>
            </el-col>
        </el-row>
<!--        <el-row :class="main-footer">-->
<!--            <el-col>-->
<!--                <header-for-all/>-->
<!--            </el-col>-->
<!--        </el-row>-->
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

</style>



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

</style>
