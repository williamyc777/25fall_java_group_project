<script setup>
import { ref, onMounted, defineProps, computed } from 'vue'
import { Pointer, Share, StarFilled } from "@element-plus/icons";
import { ChatDotSquare } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import InfoBox from "@/components/User/pages/post/components/infoBox.vue";
import axiosInstance from "@/utils/axios";

const router = useRouter();

const ChatDotSquareIcon = ChatDotSquare;
const PointerIcon = Pointer;
const ShareIcon = Share;
const StarFilledIcon = StarFilled;

const props = defineProps({
    postID: {
        type: String,
        default: '666666'
    },
});

const postLink = ref("666666")
const postTitle = ref("default title")
const postContent = ref("default content")
const postRelevantEventID = ref("1")
const postLikeAmount = ref("666")
const postCollectAmount = ref("666")
const postCommentAmount = ref("666")
const username = ref("default username")
const userID = ref("666666")
const userBio = ref("default userBio")
const userAvatar = ref("")

const eventTitle = ref("default event")
const posterUrl = ref("")

async function fetchData() {
    try {
        const response = await axiosInstance.get(`/post/getFullPost?postID=${props.postID}`)
        const postData = response.data.data;

        postTitle.value = postData.postTitle;
        postContent.value = postData.postContent;
        postLikeAmount.value = postData.postLikeAmount;
        postCollectAmount.value = postData.postCollectAmount;
        postCommentAmount.value = postData.postCommentAmount;
        username.value = postData.username;
        userID.value = postData.userID;
        userBio.value = postData.userBio;
        userAvatar.value = postData.userAvatar;

        // Fetch event details
        await fetchEventDetails(postData.postRelevantEventID);
    } catch (error) {
        console.error('Error fetching post data:', error);
    }
}

async function fetchEventDetails(eventID) {
    try {
        const response = await axiosInstance.get(`/event/detail?id=${eventID}`);
        const eventData = response.data.data;

        eventTitle.value = eventData.title;
        posterUrl.value = eventData.postUrl;
    } catch (error) {
        console.error('Error fetching event details:', error);
    }
}

onMounted(() => {
    fetchData();
});

const truncatedContent = computed(() => {
    return postContent.value.length > 350 ? postContent.value.substring(0, 350) + '...' : postContent.value;
});

function goToPost() {
    let url = router.resolve({ path: '/square/post', query: { id: props.postID } }).href;
    window.open(url, '_blank');
}
</script>


<template>
    <el-card style="max-height: 34vh; margin-bottom: 10px; border-radius: 0.5vw" shadow="hover" @click="goToPost">
        <el-row>
            <el-col :span="20">
                <el-row style="margin-bottom: 10px;">
                    <el-col :span="22">
                        <span class="title">
                            {{ postTitle }}
                        </span>
                    </el-col>
                </el-row>
                <el-row :gutter="5">
                    <el-col :span="4">
                        <el-row style="margin-bottom: 5px">
                            <el-tag type="primary">{{ eventTitle }}</el-tag>
                        </el-row>
                        <el-row>
                            <img :src="posterUrl" style="width: 6vw"  alt=""/>
                        </el-row>
                    </el-col>
                    <el-col :span="20">
                        <span class="content">
                            {{ truncatedContent }}
                        </span>
                    </el-col>
                </el-row>
            </el-col>
            <el-col :span="4">
                <el-row>
                    <el-divider/>
                    <el-col :span="12">
                        <info-box :given-number="Number(postLikeAmount)" background="http://10.16.88.247:8082/image/like.png"></info-box>
                    </el-col>
                    <el-col :span="12">
                        <info-box :given-number="Number(postCollectAmount)" background="http://10.16.88.247:8082/image/star.png"></info-box>
                    </el-col>
                    <el-col :span="12">
                        <info-box :given-number="Number(postCommentAmount)" background="http://10.16.88.247:8082/image/comment.png"></info-box>
                    </el-col>
                    <el-col :span="12">
                        <info-box given-number="" background="http://10.16.88.247:8082/image/share.png"></info-box>
                    </el-col>
                    <el-divider/>
                </el-row>
            </el-col>
        </el-row>
    </el-card>
</template>


<style scoped>
.card-box{
    //border: none;
    padding: 0 !important;

    margin: 0 !important;
}
.button-left {
    width: 100px;
}
.button-right {
    width: 50px;
}
.title {
    display: block;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-weight: bold;
    font-size: 20px;
}
.box {
    height: 8vh;
    background-color: #ccc;
    margin: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
}
.number {
    position: absolute;
    color: white;
    font-family: 'Arial Rounded MT Bold', sans-serif;
}
</style>
