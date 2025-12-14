import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import Login from "@/components/Login.vue";
import Square from "@/components/User/pages/post/postSquare.vue"
import Post from "@/components/User/pages/post/postDetail.vue"
import Message from "@/components/User/pages/message/Message.vue"

import EventPage from "@/components/User/pages/event/EventPage.vue"
import CreateEvent from "@/components/User/pages/event/CreateEvent.vue";
import ManageEvent from "@/components/User/pages/event/ManageEvent.vue";
import MyEvent from "@/components/User/pages/event/MyEvent.vue";
import ExamplePage from "@/components/Modules/event/ExamplePage.vue";

import mainPage from "@/components/User/pages/main/mainPage.vue"
import searchPostPage from "@/components/User/pages/main/searchPostPage.vue";
import searchEventPage from "@/components/User/pages/main/searchEventPage.vue";
import signUp from "@/components/User/pages/main/signUp.vue";
import Profile from "@/components/User/pages/profile/profile.vue";
import ProfileExample from "@/components/User/pages/profile/profileExample.vue";

import notFound from "@/components/User/pages/post/notFound.vue";
import PostCollect from "@/components/User/pages/post/components/postCollect.vue";
import PostWrite from "@/components/User/pages/post/components/postWrite.vue";

import Admin from "@/components/Admin/AdminPage.vue";
import AllEvent from "@/components/User/pages/main/AllEvent.vue";

import Doc from "@/components/User/pages/doc/Doc.vue";
import DocforA from "@/components/User/pages/doc/DocforA.vue";

const routes = [
    {path: '/', component: Login},
    {path: '/signup', component: signUp},
    {path: '/square', component: Square},
    {path: '/square/post', component: Post},
    {path: '/square/post/collect', component: PostCollect},
    {path: '/square/post/write', component: PostWrite},


    {path: '/profile', component: Profile},
    {path: '/profileTest', component: ProfileExample},

    {path: '/event', component: EventPage},
    {path: '/event/create', component: CreateEvent},
    {path: '/event/manage', component: ManageEvent},
    {path: '/event/cardTest', component: ExamplePage},
    {path: '/event/my', component: MyEvent},

    {path: '/main', component: mainPage},

    // {path: '/search', component: searchPage},
    {path: '/message', component: Message},

    {path: '/notFound', component: notFound},

    {path: '/post/search', component: searchPostPage},
    {path: '/event/search', component: searchEventPage},
      
    {path: '/admin', component: Admin},
    {path: '/allEvent', component: AllEvent},

    {path: '/doc', component: Doc},
    {path: '/docAdmin', component: DocforA}


]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
