<template>
    <div class="not-found">
        <el-card shadow="hover" class="not-found-card">
            <h1 class="title">404</h1>
            <p class="message">Sorry, the page you are looking for could not be found.</p>
            <p>{{ errorDescription }}</p>
            <el-button type="primary" @click="goHome">Go to Home</el-button>
        </el-card>
    </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { ref, watchEffect } from 'vue'

const router = useRouter()
const route = useRoute()
const errorDescription = ref('')

const goHome = () => {
    router.push('/')
}

const errorType = ref(route.query.errorCode)

const getErrorDescription = (type) => {
    switch (type) {
        case '1':
            return 'The post is not exist.'
        case '2':
            return 'The user is not exist.'
        case '3':
            return 'Internal server error. Please try again later.'
        default:
            return 'An unknown error has occurred.'
    }
}

watchEffect(() => {
    errorDescription.value = getErrorDescription(errorType.value)
})
</script>

<style scoped>
.not-found {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f2f2f2;
}

.not-found-card {
    text-align: center;
    padding: 50px;
    border-radius: 10px;
    background: #fff;
}

.title {
    font-size: 96px;
    font-weight: bold;
    color: #ff6f61;
    margin-bottom: 20px;
}

.message {
    font-size: 20px;
    color: #606266;
    margin-bottom: 30px;
}

.el-button {
    font-size: 16px;
    padding: 12px 20px;
}
</style>
