<script setup>
import { Pointer, Share, StarFilled } from "@element-plus/icons";
import { ChatDotSquare } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import InfoBox from "@/components/User/pages/post/components/infoBox.vue";
import SimplePost from "@/components/Modules/SimplePost.vue";

const router = useRouter();

const ChatDotSquareIcon = ChatDotSquare;
const PointerIcon = Pointer;
const ShareIcon = Share;
const StarFilledIcon = StarFilled;

function goToPost() {
    // router.push({ path: '/square/post' });

    let url = router.resolve({path: '/square/post'}).href;
    window.open(url, '_blank');
}
</script>

<template>
<!--    <el-card class="card-box" shadow="never">-->
        <el-card style=" max-height: 27vh; margin-bottom: 10px; border-radius: 0.5vw" shadow="hover" @click="goToPost">
<!--            <template #header>Yummy hamburger</template>-->
            <el-row>
                <el-col :span="20">
                    <el-row style="margin-bottom: 10px;">
                        <el-col :span="22">
                            <span class="title">
                                Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title
                            </span>
                        </el-col>
                    </el-row>
                    <el-row :gutter="5">
                        <el-col :span="8">
                            <el-row style="margin-bottom: 5px">
                                <el-tag type="primary">Event</el-tag>
                            </el-row>
                            <el-row>
                                <img
                                    src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                                    style="width: 6vw"/>
                            </el-row>
                        </el-col>
                        <el-col :span="16">
                    <span class="content">
                        content content content content content content content content
                        content content content content content content content content
                        content content content content content content content content
                        content content content content content content content content
                        content content content content content content content content
                        content content content content content content content content
                    </span>
                        </el-col>
                    </el-row>
                </el-col>
                <el-col :span="4">
                    <el-row>
                        <el-col :span="12">
                            <info-box :given-number="1" :background="'@/assets/Like/like.png'"></info-box>
                        </el-col>
                        <el-col :span="12">
                            <info-box></info-box>
                        </el-col>
                        <el-col :span="12">
                            <info-box :given-number="32456"></info-box>
                        </el-col>
                        <el-col :span="12">
                            <info-box></info-box>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
        </el-card>
<!--    </el-card>-->
    <el-row>

    </el-row>
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
