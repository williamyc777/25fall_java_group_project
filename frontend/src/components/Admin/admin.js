import axiosInstance from "@/utils/axios";
import {ref} from 'vue'

export async function getBriefEvent(id) {
    const event = ref([])
    await axiosInstance.get('/event/brief'
        , {
            params: {
                id: id
            }
        }).then(response => {
            event.value = response.data.data;
        }
    ).catch(error => {
        console.error(error);
    });
    return event.value
}

export async function getPost(id) {
    const post = ref([])
    await axiosInstance.get('/event/brief'
        , {
            params: {
                eventId: id
            }
        }).then(response => {
            post.value = response.data.data;
        }
    ).catch(error => {
        console.error(error);
    });
    return post.value
}

export async function getAllEvents() {
    const events = ref([])
    await axiosInstance.get('/event/all'
    ).then(response => {
        events.value = response.data.data;
    }).catch(error => {
        console.error(error);
    });
    return events.value
}

export async function getAllPosts() {
    const posts = ref([])
    await axiosInstance.get('/post/getPostSquare'
    ).then(response => {
        posts.value = response.data.data;
    }).catch(error => {
        console.error(error);
    });
    return posts.value
}

export async function getAllUser() {
    const users = ref([])
    await axiosInstance.get('/admin/user'
    ).then(response => {
        users.value = response.data.data;
    }).catch(error => {
        console.error(error);
    });
    return users.value

}

export async function deleteEvent(eventId) {
    await axiosInstance.delete('/admin/event', {
        params: {
            eventId: eventId
        }
    }).then(response => {
        console.log(response.data);
        alert("删除成功")
    }).catch(error => {
        console.error(error);
    });
}

export async function deletePost(id) {
    await axiosInstance.delete('/admin/post', {
        params: {
            postId: id
        }
    }).then(response => {
        console.log(response.data);
        alert("删除成功")
    }).catch(error => {
        console.error(error);
    });
}

export async function changePermission(permissionForm) {
    console.log(permissionForm);
    await axiosInstance.post('/admin/permission', permissionForm
    ).then(response => {
        console.log(response.data);
        alert("修改成功")
    }).catch(error => {
        console.error(error);
    });
}