<script setup>
import {ref, toRefs, computed, onMounted} from "vue";
import store from "@/utils/store"
import {useRouter} from "vue-router";
import axiosInstance from "@/utils/axios";

const router = useRouter()

const props = defineProps({
    userId: {
        type: String,
        required: true
    },
    needLevi: {
        type: Boolean,
        default: true
    },
    needSmall: {
        type: Boolean,
        default: false
    },
    sizeSmall: {
        type: String,
        default: '25px'
    },
    sizeMini: {
        type: String,
        default: '38px'
    },
    sizeWrapper: {
        type: String,
        default: '45px'
    }
})

const smallStyle = computed(() => {
    return "width: " + props.sizeSmall + "; height: " + props.sizeSmall + ";"
})
const miniStyle = computed(() => {
    return "width: " + props.sizeMini + "; height: " + props.sizeMini + ";"
})
const wrapperStyle = computed(() => {
    return "width: " + props.sizeWrapper + "; height: " + props.sizeWrapper + ";"
})


const toshow = ref(false)

const isSelf = computed(() => {
    return localStorage.getItem('userId') === props.userId
})

function toProfile() {
    if (isSelf.value) {
        router.push({path: '/self'})
    } else {
        let studentId = 0
        axiosInstance.get('/user/students', {
            params: {
                id: props.userId
            }
        }).then(response => {
            studentId = response.data.data.username
            router.push({
                path: '/bulletin', query: {
                    tag: studentId
                }
            })
        }).catch(error => {
            console.error(error);
        });
    }
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
            userId: props.userId
        }
    })
}

function invite() {
    router.push({
        path: '/invite', query: {
            userId: props.userId
        }
    })
}

let avatar_url = computed(() => {
    return 'http://10.16.88.247:8084/' + props.userId + '.png'
})

</script>


<template>
    <div>
        <div @mouseover="mouseEnter" @mouseleave="mouseOut" class="wrapper" :style="wrapperStyle">
            <img v-if="needSmall" @click="toProfile" :style="smallStyle"
                 class="header-entry-small" alt="avatar" id="avatar1" :src="avatar_url">
            <img v-else @click="toProfile" :style="miniStyle"
                 class="header-entry-mini" alt="avatar" id="avatar2" :src="avatar_url">
            <div v-if="props.needLevi">
                <div class="levitate" v-show="toshow" @mouseover="mouseEnter" @mouseout="mouseOut">

                    <div v-if="isSelf" style="width: 100%">
                        <div>
                            <div @click="logout">
                                <div class="levi_butt">Log out</div>
                            </div>
                        </div>
                    </div>

                    <div v-else style="width: 100%">
                        <div>
                            <div @click="toChat">
                                <div class="levi_butt">Chat</div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        </div>

    </div>
</template>

<style scoped>
.header-entry-mini {
    margin-top: 7px;
    z-index: 2;
    display: block;
    width: 38px;
    height: 38px;
    border-radius: 50%;
    cursor: pointer;
}

.header-entry-small {
    margin-left: 10px;
    margin-top: 10px;
    top: 13px;
    left: 15px;
    z-index: 2;
    display: block;
    width: 25px;
    height: 25px;
    border-radius: 50%;
    cursor: pointer;
}

.wrapper {
    top: 10px;
    left: 10px;
    width: 45px;
    height: 45px;
    justify-content: center;
    align-items: center;
    align-content: center;
}

.levitate {
    left: -20px;
    height: 100px;
    width: 100px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    position: relative;
    align-content: center;
    align-items: center;
    border: 1px solid #cccccc;
    border-radius: 10px;
    background-color: #ffffff;
    z-index: 3;
}

.levi_butt {
    cursor: pointer;
    align-items: center;
    align-content: center;
    text-align: center;
}

.el-divider-modified1 {
    margin: 15px 0;
    width: 100%;
}

</style>
