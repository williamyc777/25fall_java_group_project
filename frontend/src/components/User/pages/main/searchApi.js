import axiosInstance from "@/utils/axios";
import {ref} from 'vue'


export async function getBriefEvent(id) {
    const event = ref([])
    await axiosInstance.get('/event/brief'
        , {
            params: {
                eventId: id
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

export async function searchEvent(content) {
    const eventIds = ref([])
    await axiosInstance.get('/search/event'
        , {
            params: {
                content: content
            }
        }).then(response => {
        eventIds.value = response.data.data;
        }
    ).catch(error => {
        console.error(error);
    });
    return eventIds.value
}


export async function searchPost(content) {
    const postIds = ref([])
    await axiosInstance.get('/search/post'
        , {
            params: {
                content: content
            }
        }).then(response => {
        postIds.value = response.data.data;
        }
    ).catch(error => {
        console.error(error);
    });
    return postIds.value
}