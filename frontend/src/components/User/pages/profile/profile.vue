<template>
    <div class="bg">
        <el-row :class="main-header">
            <el-col :span="24">
                <header-for-all/>
            </el-col>
        </el-row>
        <el-row :class="main-main" gutter="10">
            <section>

                <div class="sec_new">
<!--                    &lt;!&ndash; 编辑弹窗 &ndash;&gt;-->
<!--                    <el-dialog title="Edit Profile" v-if="editDialogVisible" style="z-index: 999;">-->
<!--                        <el-form :model="editForm">-->
<!--                            <el-form-item label="Name">-->
<!--                                <el-input v-model="editForm.name"></el-input>-->
<!--                            </el-form-item>-->
<!--                            <el-form-item label="Bio">-->
<!--                                <el-input v-model="editForm.bio"></el-input>-->
<!--                            </el-form-item>-->
<!--                            &lt;!&ndash; 可以添加更多表单项 &ndash;&gt;-->
<!--                        </el-form>-->
<!--                        <template #footer>-->
<!--                            <el-button @click="editDialogVisible = false">Cancel</el-button>-->
<!--                            <el-button type="primary" @click="submitEdit">Save</el-button>-->
<!--                        </template>-->
<!--                    </el-dialog>-->

<!--                    &lt;!&ndash; 头像上传弹窗 &ndash;&gt;-->
<!--                    <input type="file" ref="fileInput" @change="handleFileChange" style="display: none;">-->
                    <el-row>
                        <el-col span="8">
                            <div class="profile">
                                <el-row><el-avatar
                                    :size="250"
                                    :src="avatar"
                                    shape="square"
                                    style="margin-bottom: 8px; opacity: 1"
                                    fit="fill"
                                /></el-row>
                                <el-button-group>
                                    <el-button v-if="!isSelf" @click="chat" :icon="ChatDotSquare">
                                        Chat
                                    </el-button>
                                    <el-button v-if="isSelf" @click="edit" :icon="EditPen">
                                        Edit
                                    </el-button>
                                    <el-button v-if="isSelf" @click="avatarDialogVisible=true" :icon="User">
                                        Avatar
                                    </el-button>
                                    <el-button v-if="isSelf" @click="logout" :icon="House">
                                        Logout
                                    </el-button>
                                </el-button-group>
                                <h1>{{ userName }}</h1>
                                <p style="margin: 0; font-size: 14px; color: var(--el-color-info)">@{{ userID }}</p>
                                <p>{{ bio }}</p>
                            </div>
                        </el-col>
                        <el-col span="8">
                            <!-- 这里可以放其他内容 -->
<!--                            TODO: 编辑username 和 bio并上传&ndash;&gt;-->
                            <div v-if="editDialogVisible" style="position: relative">
                                <h4>Edit Profile</h4>
                                <el-form :model="editForm">
                                    <el-form-item label="Name">
                                        <el-input v-model="editForm.name"></el-input>
                                    </el-form-item>
                                    <el-form-item label="Bio">
                                        <el-input v-model="editForm.bio" :autosize="{ minRows: 2, maxRows: 4 }"
                                                  type="textarea"></el-input>
                                    </el-form-item>
                                    <!-- 可以添加更多表单项 -->
                                </el-form>
                                <el-button @click="editDialogVisible=false">cancel</el-button>
                                <el-button @click="submitEdit">submit</el-button>
<!--                                <template #footer>-->
<!--                                    <el-button @click="editDialogVisible = false">Cancel</el-button>-->
<!--                                    <el-button type="primary" @click="submitEdit">Save</el-button>-->
<!--                                </template>-->
                            </div>
                            <div v-if="avatarDialogVisible">
<!--                                todo 上传头像-->
                                <div class="avatar-upload">
                                    <input type="file" @change="onFileChange" accept="image/*" />
                                    <img v-if="previewImage" :src="previewImage" alt="Selected Image" class="avatar-preview" />
                                    <el-button v-if="selectedFile" @click="uploadAvatar" type="primary">上传头像</el-button>
                                </div>

                            </div>


                        </el-col>

                    </el-row>
                    <el-row>
                        <el-col span="12">
                            <el-scrollbar :noresize="false" style="width: 70vw">
                                <div class="scrollbar-flex-content">
                                    <SimplePost
                                        v-for="post in posts"
                                        :key="post.id + 'b'"
                                        :link="post.link"
                                        :title="post.title"
                                        :content="post.content"
                                        :image="post.image"
                                        :eventLink="post.eventLink"
                                        :eventSmallImage="post.eventSmallImage"
                                        :eventName="post.eventName"
                                        :eventId="post.eventId"
                                        :eventBio="post.eventBio"
                                    />
                                </div>
                            </el-scrollbar>
                        </el-col>
<!--                        <el-col span="12">-->
<!--                            <el-scrollbar style="width: 70vw">-->
<!--                                <div class="scrollbar-flex-content">-->
<!--                                    <p v-for="item in 50" :key="item" class="scrollbar-demo-item">-->
<!--                                        {{ item }}-->
<!--                                    </p>-->
<!--                                </div>-->
<!--                            </el-scrollbar>-->
<!--                        </el-col>-->
                    </el-row>
                </div>
                <img src="@/components/User/pages/profile/images/stars.png" alt="" id="stars">
                <img src="@/components/User/pages/profile/images/moon.png" alt="" id="moon">
                <img src="@/components/User/pages/profile/images/mountains_behind.png" alt="" id="mountain_behind">
                <div id="text">{{userName}}</div>
                <img src="@/components/User/pages/profile/images/mountains_front.png" alt="" id="mountain_front">

            </section>
        </el-row>

        <el-row :class="main-footer">footer</el-row>
    </div>
</template>


<script>
import { ref, onMounted, onBeforeMount } from "vue";
import axios from 'axios';
import SimplePost from "@/components/Modules/SimplePost.vue";
import HeaderForAll from "@/components/Modules/HeaderForAll.vue";
import Avatar from "@/components/Modules/avatar/Avatar.vue";
import { useRoute, useRouter } from 'vue-router';
import axiosInstance from "@/utils/axios";
import ProfileIntersection from "@/components/User/pages/profile/profileIntersection.vue";
import {ChatDotSquare, EditPen} from "@element-plus/icons-vue";
import { House, User} from "@element-plus/icons";


export default {
    computed: {
        User() {
            return User
        },
        Avatar() {
            return Avatar
        },
        EditPen() {
            return EditPen
        },
        House() {
            return House
        },
        ChatDotSquare() {
            return ChatDotSquare
        }
    },
    mounted() {
        //获取到一些关键元素
        let stars = this.$el.querySelector('#stars');
        let moon = this.$el.querySelector('#moon');
        let mountain_behind = this.$el.querySelector('#mountain_behind');
        let text = this.$el.querySelector('#text');
        // let btn = this.$el.querySelector('#btn');
        let mountain_front = this.$el.querySelector('#mountain_front');

        //给窗口添加鼠标滚动事件
        window.addEventListener('scroll', () => {
            let value = window.scrollY;
            stars.style.left = value * 0.25 + 'px';
            moon.style.top = value * 1.2 + 'px';
            mountain_behind.style.top = value * 0.5 + 'px';
            text.style.marginRight = value * 4 + 'px';
            text.style.marginTop = value * 0.5 + 'px';
            // btn.style.marginRight = value * 1.5 + 'px';
            // btn.style.marginTop = value * 0.5 + 'px';
        });
    },
    components: {ProfileIntersection, Avatar, HeaderForAll, SimplePost },
    setup() {
        const route = useRoute();
        const router = useRouter();
        const avatar = ref('');
        const userName = ref('');
        const userID = ref('');
        const bio = ref('');
        const posts = ref([]);
        const isSelf = ref(false);
        const previewImage = ref();
        const selectedFile = ref();

        const editDialogVisible = ref(false);
        const avatarDialogVisible = ref(false);
        const editForm = ref({
            name: userName,
            bio: bio,
        });

        onBeforeMount(async () => {
            const userIdFromRoute = route.query.userID;
            if (userIdFromRoute) {
                await fetchData(userIdFromRoute);
                if (route.query.userID.toString() === localStorage.getItem('userId').toString()) {
                    isSelf.value = true;
                    console.log('true')
                } else {
                    isSelf.value = false;
                    console.log('false')
                }
                console.log(route.query.userID)
                console.log(localStorage.getItem('userId'))
            } else {
                await router.push({ path: '/notFound', query: { errorCode: 2 } });
                console.error('User ID not found in the route query parameters.');
            }
        });

        const fetchData = async (userId) => {
            try {
                const profileResponse = await axiosInstance.get(`/profile/info/get?userID=${userId}`);
                const profileData = profileResponse.data.data;

                avatar.value = profileData.avatar;
                userName.value = profileData.name;
                userID.value = profileData.id;
                bio.value = profileData.bio;

                // const postCollectionResponse = await axios.post(`/profile/postCollection?userID=${userId}`);
                // const postCollectionData = postCollectionResponse.data;
                //
                // for (const postId of postCollectionData.postIds) {
                //     const postResponse = await axios.get(`/posts/${postId}`);
                //     posts.value.push(postResponse.data);
                // }
            } catch (error) {
                console.error('Error fetching profile or posts data:', error);
            }
        };


        const edit = () => {
            editDialogVisible.value = true;
            console.log("edit")
        };

        const submitEdit = async () => {
            try {
                const response = await axiosInstance.post("/profile/info/edit", {
                    name: editForm.value.name,
                    bio: editForm.value.bio,
                });
                if (response.data.success) {
                    userName.value = editForm.value.name;
                    bio.value = editForm.value.bio;
                    editDialogVisible.value = false;
                } else {
                    console.error("Error updating profile:", response.data.message);
                }
            } catch (error) {
                console.error("Error updating profile:", error);
            } finally {
                editDialogVisible.value=false;
            }
        };

        const onFileChange = (event) => {
            const file = event.target.files[0];
            if (file) {
                selectedFile.value = file;
                previewImage.value = URL.createObjectURL(file);
            }
        };

        const uploadAvatar = async () => {
            if (!selectedFile.value) {
                console.error("No file selected for upload.");
                return;
            }

            const formData = new FormData();
            formData.append('file', selectedFile.value);

            try {
                const response = await axiosInstance.post('/image/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                const imageUrl = response.data.data;
                console.log('url:',imageUrl)
                await axiosInstance.post('/profile/avatar/edit', { avatar: imageUrl });

                // 更新头像成功后，清除选择的文件和预览
                selectedFile.value = null;
                previewImage.value = null;
                avatarDialogVisible.value = false;
            } catch (error) {
                console.error('Error uploading image:', error);
            }
        };

        const logout = () => {
            router.push({path: '/'})
            localStorage.setItem('token', '')
            localStorage.setItem('userId', '')
        };

        const chat = () => {
            router.push({
                path: '/message', query: {
                    messageType: 'chats',
                    userId: userID
                }
            })
        };

        return {
            avatar,
            userName,
            userID,
            bio,
            posts,
            isSelf,
            editDialogVisible,
            avatarDialogVisible,
            editForm,
            edit,
            submitEdit,
            onFileChange,
            uploadAvatar,
            logout,
            chat,
            previewImage,
            selectedFile
        };
    },

};

</script>




<style scoped>
.id {

}


bg {
    width: 100vw;
    background-color: #000; /* 设置黑色背景 */
    color: #fff; /* 设置文本颜色为白色，以便于对比 */
}
*{
    padding: 0;
    margin: 0;
    box-sizing: border-box;
}
body{
    background: linear-gradient(#2b1055,#7597de);
    min-height: 100vh;

    overflow-x: hidden;
    scroll-behavior: smooth;
}
header{
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    padding: 30px 100px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    z-index: 100;
}
header #logo{
    color: #fff;
    text-decoration: none;
    font-size: 2em;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 2px;
}
header ul{
    display: flex;
    justify-content: center;
    align-items: center;
}
header ul li{
    list-style: none;
    margin-left: 20px;
}
header ul li a{
    text-decoration:none;
    color: #fff;
    padding: 6px 25px;
    border-radius: 20px;
}
header ul li a:hover,
header ul li a.active{
    background-color: #fff;
    color: #2b1065;
}
section{
    position: relative;
    width: 100vw;
//height: 100vh;
    padding: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    background-color: #000000;
}
section::before{
    content: '';
    position: absolute;
    bottom: 0;
    height: 100px;
    width: 100%;
    background: linear-gradient(to top,#1c0522,transparent);
    z-index: 1000;

}
section img{
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    pointer-events: none;
}
section img#moon{
    mix-blend-mode: screen;/*设置背景融合方式*/
}
section img#mountain_front{
    z-index: 15;
}
section #text{
    position: absolute;
    color: #fff;
    font-size: 7.5vw;
    white-space: nowrap;
    z-index: 10;
    transform: translateY(100px);
    right: -350px;
}
section #btn{
    text-decoration: none;
    color: #2b1055;
    padding: 8px 30px;
    border-radius: 20px;
    background-color: #fff;
    font-size: 1.5em;
    z-index: 10;
    display: inline-block;
}
.sec_new{
    position: relative;
    padding: 100px;
    background-color: #cccccc;
    z-index: 200;
    width: 85vw;
    height: 100vh;
    opacity: 0.9;
    border-radius: 10px;
}
.sec{
    position: relative;
    padding: 100px;
    background-color: #1c0522;
    z-index: 200;
}
.sec h2{
    font-size: 3.5em;
    color: #fff;
    margin-bottom: 10px;
}
.sec p{
    color: #fff;
    font-size: 1.2em;
}

.scrollbar-flex-content {
    display: flex;
}
.scrollbar-demo-item {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100px;
    height: 50px;
    margin: 10px;
    text-align: center;
    border-radius: 4px;
    background: var(--el-color-danger-light-9);
    color: var(--el-color-danger);
}

.avatar-upload {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 20px;
}

.avatar-preview {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
    margin-top: 10px;
}
</style>
