<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps } from 'vue';

const props = defineProps({
    background: {
        type: String,
        default: '/assets/Like/like.png'
    },
    givenNumber: {
        type: Number,
        default: '1234'
    }
});

const num = ref(null);
const adjustFontSize = () => {
    const NUM = parseInt(num.value.innerText);
    if (NUM > 9999) {
        num.value.innerText = '9999+';
    } else {
        const length = NUM.toString().length;
        if (length <= 2) {
            num.value.style.fontSize = '40px';
        } else if (length <= 3) {
            num.value.style.fontSize = '30px';
        } else if (length <= 4) {
            num.value.style.fontSize = '20px';
        }
    }
};

onMounted(() => {
    adjustFontSize();
    window.addEventListener('resize', adjustFontSize);
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', adjustFontSize);
});
</script>

<template>
    <div class="box" :style="{ backgroundImage: `url(${background})` }">
        <span ref="num" class="number">{{props.givenNumber}}</span>
    </div>
</template>

<style scoped>
.box {
    height: 8vh;
    margin: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    border-radius: 10px;
    background-size: cover; /* 使用 cover 或 contain */
    background-position: center; /* 确保背景图片居中显示 */
    background-repeat: no-repeat; /* 防止背景图片重复 */
}
.number {
    position: absolute;
    color: white;
    font-family: 'Arial Rounded MT Bold', sans-serif;
}
</style>
