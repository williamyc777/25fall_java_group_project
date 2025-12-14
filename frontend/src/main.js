import { createApp, reactive } from 'vue'
import router from './utils/router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import VueMarkdownEditor from '@kangc/v-md-editor';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/base-editor.css';

import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import Prism from 'prismjs';

import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';

import App from './App.vue'

// I am not sure whether it works or not
// import axios from 'axios'
// import axiosInstance from "@/utils/axios/test";
// app.config.globalProperties.$axios = axios
// app.config.globalProperties.$request = axiosInstance

// set the title of the page
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title

    }
    next()
})

document.title = '南科大活动中心'

export const globalStore = reactive({
    'notification': false
})

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

VueMarkdownEditor.use(vuepressTheme, {
    Prism,
});

VMdPreview.use(vuepressTheme, {
    Prism,
});

app.use(ElementPlus)
app.use(VueMarkdownEditor)
app.use(router).mount('#app')
app.provide('globalStore', globalStore)
export default app
