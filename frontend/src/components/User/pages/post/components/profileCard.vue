<template>
    <el-popover
        :width="300"
        popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
        placement="bottom-start"
        :show-arrow="false"
        offset="0"
    >
        <template #reference>
            <div class="card-container" @click="toProfile">
                <el-button-group >
                    <el-button class="profile-avatar">
                        <div class="block">
                            <el-avatar :size="40" :src="props.avatar" />
                        </div>
                    </el-button>
                    <el-button  class="profile-name">
                        <p>{{ props.name }}</p>
                    </el-button>
                </el-button-group>
            </div>
        </template>
        <template #default>
            <div
                class="demo-rich-conent"
                style="display: flex; gap: 16px; flex-direction: column"
            >

                <el-avatar
                    :size="60"
                    :src="props.avatar"
                    style="margin-bottom: 8px"
                />
                <div>
                    <p
                        class="demo-rich-content__name"
                        style="margin: 0; font-weight: 500"
                    >
                        {{props.name}}
                    </p>
                    <p
                        class="demo-rich-content__mention"
                        style="margin: 0; font-size: 14px; color: var(--el-color-info)"
                    >
                        @{{props.id}}
                    </p>
                </div>

                <p class="demo-rich-content__desc" style="margin: 0">
                    {{props.bio}}
                </p>

                <div class="levitate" >

                    <div v-if="isSelf" style="width: 100%">
                        <div>
                            <div @click="logout">
                                <div class="levi_butt">Log out</div>
                            </div>
                            <el-divider class="el-divider-modified1"/>
                            <div @click="toSelf">
                                <div class="levi_butt">Self</div>
                            </div>
                        </div>
                    </div>

                    <div v-else style="width: 100%">
                        <div>
                            <div @click="toChat">
                                <div class="levi_butt">Chat</div>
                            </div>
                            <el-divider class="el-divider-modified1"/>
                            <div @click="invite">
                                <div class="levi_butt">Invite</div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </template>
    </el-popover>

</template>

<script setup>
import {computed, defineProps, ref} from 'vue';
import axiosInstance from "@/utils/axios";
import { useRouter } from 'vue-router';
const router = useRouter();

const props = defineProps({
    avatar: {
        type: String,
        default: 'squareUrl' // 设置头像的默认值
    },
    name: {
        type: String,
        default: 'John Doe' // 设置名字的默认值
    },
    id: {
        type: String,
        default: '666666'
    },
    bio: {
        type: String,
        default: 'Have no bio yet.'
    },
    //对应人物页面
    link: {
        type: String
    }

});

const toshow = ref(false)

const isSelf = computed(() => {
    return localStorage.getItem('userId') === props.id
})

function toProfile() {
    // if (isSelf.value) {
    //     router.push({path: '/self'})
    // } else {
    //     let studentId = 0
    //     axiosInstance.get('/user/students', {
    //         params: {
    //             id: props.id
    //         }
    //     }).then(response => {
    //         studentId = response.data.data.username
    //         router.push({
    //             path: '/bulletin', query: {
    //                 tag: studentId
    //             }
    //         })
    //     }).catch(error => {
    //         console.error(error);
    //     });
    // }
    router.push({ path: '/profile', query: { userID: props.id } })
}

function mouseEnter() {
    toshow.value = true
}

function mouseOut() {
    toshow.value = false
}

function toSelf() {
    router.push({path: '/self'})
}

function logout() {
    router.push({path: '/'})
    localStorage.setItem('token', '')
    localStorage.setItem('userId', '')
}

function toChat() {
    router.push({
        path: '/message', query: {
            messageType: 'chats',
            userId: props.id
        }
    })
}

function invite() {
    router.push({
        path: '/invite', query: {
            userId: props.id
        }
    })
}
</script>

<style scoped>
.profile-avatar{
    border: none;
    height: 45px;
    width: 45px;
}
.profile-name{
    border: none;
    height: 45px;
}
</style>
