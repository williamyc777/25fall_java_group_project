<template>
  <div class="header">
    <!-- Logo -->
    <div class="logo-section">
      <img src="../../assets/logo.png" alt="Company Logo" class="logo" />
      <span class="site-name">校园活动网</span>
    </div>

    <!-- Navigation -->
    <div class="nav-section">
      <nav>
        <ul>
          <li v-for="item in menuItems" :key="item.id">
            <a :href="item.url">{{ item.label }}</a>
          </li>
        </ul>
      </nav>
    </div>

    <!-- User Profile -->
    <div class="user-profile-section">
      <div class="user-profile">
        <Avatar :userId="userId" class="avatar"></Avatar>
      </div>
    </div>
  </div>
</template>

<script>
import Avatar from './avatar/Avatar.vue';
import axiosInstance from "@/utils/axios";

export default {
  components: {Avatar},
  data() {
    return {
      menuItems: [
        { id: 1, label: '主页', url: '/main' },
        { id: 2, label: '帖子广场', url: '/square' },
        { id: 3, label: '所有活动', url: '/allEvent' },
        { id: 4, label: '我的活动', url: '/event/my'},
        { id: 6, label: '我的消息', url: '/message'},
        { id: 7, label: '帮助文档', url: '/doc'}
      ],
      userId: localStorage.getItem('userId')
    };
  },

  mounted() {
    axiosInstance.get('/user/permission',
        {
          headers: {
            'Authorization': localStorage.getItem('token')
          }
        }
    ).then((res) => {
      console.log(res)
      if (res.data.data.canCreate) {
        this.menuItems.push({id: 5, label: '活动管理', url: '/event/manage'})
      }
    }).catch((err) => {
      console.log(err)
    })

  }
};
</script>

<style scoped>
/* Header Styles */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 30px;
  background-color: #34495e;
  color: #ecf0f1;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease;
}

.header:hover {
  background-color: #2c3e50;
}

/* Logo Styles */
.logo-section {
  display: flex;
  align-items: center;
}

.logo {
  width: 40px;
  height: auto;
  margin-right: 10px;
}

.site-name {
  font-size: 1.5rem;
  font-weight: bold;
  color: #ecf0f1;
}

/* Navigation Styles */
.nav-section {
  display: flex;
  align-items: center;
}

nav ul {
  list-style: none;
  display: flex;
  margin: 0;
  padding: 0;
}

nav li {
  margin-right: 20px;
}

nav a {
  text-decoration: none;
  color: #ecf0f1;
  font-size: 1rem;
  transition: color 0.3s ease;
}

nav a:hover {
  color: #1abc9c;
}

/* User Profile Styles */
.user-profile-section {
  display: flex;
  align-items: center;
}

.user-profile {
  display: flex;
  align-items: center;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
  border: 2px solid #ecf0f1;
  transition: border-color 0.3s ease;
}

.user-profile:hover .avatar {
  border-color: #1abc9c;
}

.user-profile span {
  font-size: 1rem;
  color: #ecf0f1;
}
</style>
