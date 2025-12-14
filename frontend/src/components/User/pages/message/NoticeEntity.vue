<script setup>
const props = defineProps({
  sender: {
    type: String,
    required: true
  },
  time: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  },
})

</script>

<template>
  <div class="entity">
    <div class="header">
      <p class="title">{{title}}</p>
      <p class="extend_sender">By: {{sender}}</p>
      <p class="extend_time">{{time}}</p>
    </div>
    <div class="content">
      <p class="body">{{content}}</p>
    </div>
  </div>
</template>

<style scoped>
  .entity {
    width: 100%;
    background-color: #ffffff;
    border-radius: 10px;
    margin-bottom: 10px;
  }

  .header {
    width: 100%;
    display: flex;
    flex-direction: column;
    line-height: 0;
    justify-content: space-between;
    align-items: flex-start;
  }

  .title {
    font-size: 30px;
    font-weight: bold;
    margin-left: 10px;
    margin-bottom: 20px;
  }


  .extend_sender {
    font-size: 15px;
    margin-left: 10px;
  }

  .extend_time {
    font-size: 10px;
    margin-left: 10px;
  }

  .content {
    width: 100%;
    display: flex;
    flex-direction: column;
    line-height: 2;
    justify-content: flex-start;
    align-items: flex-start;
  }

  .body {
    font-size: 20px;
    margin-left: 20px;
    margin-top: 7px;
    margin-bottom: 10px;
    text-align: left;
  }

</style>