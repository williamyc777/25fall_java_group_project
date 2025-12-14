<script setup>
import { Pointer, Share, StarFilled } from "@element-plus/icons";
import { ChatDotSquare } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import Comment from '@/components/Modules/comment/Comment.vue'

const router = useRouter();

const ChatDotSquareIcon = ChatDotSquare;
const PointerIcon = Pointer;
const ShareIcon = Share;
const StarFilledIcon = StarFilled;

</script>

<template>
    <!--    <el-card class="card-box" shadow="never">-->

    <el-card style=" max-height: 27vh; margin-bottom: 10px; border-radius: 0.5vw" shadow="hover" >
        <!--            <template #header>Yummy hamburger</template>-->
        <el-row>
            <el-col :span="24">

                        <comment comment-block-id="1"></comment>


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

</style>
