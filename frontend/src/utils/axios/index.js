import axios, {AxiosRequestConfig, AxiosResponse, request} from "axios"

const axiosInstance = axios.create({
    baseURL: '/api',
    timeout: 3000,
})

axiosInstance.interceptors.request.use(
    (request) => {
        request.headers['Authorization'] = localStorage.getItem('token')
        if (request.method === 'post') {
            if (request.url === '/teacher/room' ||
                request.url === '/teacher/schedule' ||
                request.url === '/teacher/zoning' ||
                request.url === '/teacher/building' ||
                request.url === '/teacher/student' ||
                request.url === '/event/create'
            ) {
                request.headers['Content-Type'] = 'application/json'
            } else {
                request.headers['Content-Type'] = 'multipart/form-data'
            }
        }
        else if(request.method === 'put') {
            if (request.url === '/student/profile' || request.url === '/teacher/room' || request.url === '/teacher/schedule') {
                request.headers['Content-Type'] = 'application/json'
            } else {
                request.headers['Content-Type'] = 'multipart/form-data'
            }
        }
        return request
    }
)

axiosInstance.interceptors.response.use(
    (response) => {
        console.log(response)
        return response
    },
    (error) => {
        console.log(error)
        switch (error.response.status) {
            case 404:
                console.log("response 404")
                break
            default:
                break
        }
        return Promise.reject(error)
    }
)


export default axiosInstance