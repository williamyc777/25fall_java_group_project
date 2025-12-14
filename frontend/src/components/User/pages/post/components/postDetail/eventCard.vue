<template>
    <el-popover
        :width="400"
        popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
        placement="bottom-start"
        :show-arrow="false"
        offset="0"
    >
        <template #reference>
            <div class="card-container" @click="goToEvent">
                <el-button-group >
                    <el-button class="profile-avatar" >
                        <div class="block">
                            <el-avatar shape="square" :size="40" :src="posterUrl" />
                        </div>
                    </el-button>
                    <el-button class="profile-name">
                        <p>{{ eventName }}</p>
                    </el-button>
                </el-button-group>
            </div>
        </template>
        <template #default>
            <div
                class="demo-rich-conent"
                style="display: flex; gap: 16px; flex-direction: column"
            >
                <!--                <el-avatar-->
                <!--                    shape="square"-->
                <!--                    :size="60"-->
                <!--                    src="{{props.avatar}}"-->
                <!--                    style="margin-bottom: 8px"-->
                <!--                />-->
                <div>
                    <div class="demo-image__lazy">
                        <el-image :src="posterUrl" />
                    </div>
                    <!--                    <el-image style="max-height: 30vh;"  :src="posterUrl"></el-image>-->
                </div>
                <div>
                    <p
                        class="demo-rich-content__name"
                        style="margin: 0; font-weight: 500"
                    >
                        {{eventName}}
                    </p>
                    <p
                        class="demo-rich-content__mention"
                        style="margin: 0; font-size: 14px; color: var(--el-color-info)"
                    >
                        @{{id}}
                    </p>
                </div>

                <p class="demo-rich-content__desc" style="margin: 0">
                    {{introduction}}
                </p>
            </div>
        </template>
    </el-popover>

</template>

<script setup>
import { defineProps, watch, ref } from 'vue';
import axiosInstance from "@/utils/axios";
import { useRouter } from "vue-router";

const router = useRouter();

const props = defineProps({
    nameVisible: {
        type: Boolean,
        default: true
    },
    id: {
        type: String,
        default: '0' // 设置活动id
    },
});

const title = ref('');
const eventName = ref('');
const author = ref('');
const authorId = ref('');
const introduction = ref('');
const posterUrl = ref('');

async function fetchData() {
    try {
        const response = await axiosInstance.get(`/event/brief?id=${props.id}`);
        const temp = response.data.data;

        title.value = temp.title;
        eventName.value = temp.eventName;
        author.value = temp.authorName;
        authorId.value = temp.authorId;
        introduction.value = temp.introduction;
        posterUrl.value = temp.postUrl;
    } catch (error) {
        console.error('Failed to fetch post data:', error);
    }
}

// Watch for changes in props.id and call fetchData when it changes
watch(
    () => props.id,
    (newId) => {
        fetchData();
    },
    { immediate: true } // Execute the watch handler immediately with the initial value
);

function goToEvent() {
    router.push({ path: '/event', query: { id: props.id } });
}
</script>

<style scoped>
.profile-avatar {
    border: none;
    height: 45px;
    width: 45px;
}
.profile-name {
    border: none;
    height: 45px;
}

.demo-image__lazy {
    height: 35vh;
    overflow-y: auto;
}
.demo-image__lazy .el-image {
    display: block;
    min-height: 200px;
    margin-bottom: 10px;
}
.demo-image__lazy .el-image:last-child {
    margin-bottom: 0;
}
</style>
