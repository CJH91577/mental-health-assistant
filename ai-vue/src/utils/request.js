import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // 请求的前缀
  timeout: 20000, // 请求的超时时间（AI 流式与上传场景需要更长）
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = token
    }
    return config
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 需要重新登录的业务码（401 系列 / token 相关）
const AUTH_CODES = ['401', 'A0230', 'A0231', 'A0300', 'A0301']

// 跳转登录（保留当前意图由登录页自行跳转）
const redirectToLogin = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  if (!window.location.pathname.startsWith('/auth/login')) {
    window.location.href = '/auth/login'
  }
}

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const { data } = response
    // 兼容：响应被其他层包装的情况
    if (data && typeof data === 'object' && 'code' in data) {
      // 处理业务状态码
      if (data.code === '200' || data.code === 200) {
        return data.data
      }
      // 未登录/token 失效：清除并跳转
      if (AUTH_CODES.includes(String(data.code))) {
        ElMessage.error(data.msg || data.message || '登录状态已失效，请重新登录')
        redirectToLogin()
        return Promise.reject(new Error(data.msg || '未登录'))
      }
      // 其他业务错误：提示并抛出，调用方可自行 catch
      const msg = data.msg || data.message || '请求失败'
      ElMessage.error(msg)
      return Promise.reject(new Error(msg))
    }
    return data
  },
  (error) => {
    // 网络错误/超时
    if (error.response) {
      const status = error.response.status
      if (status === 401 || status === 403) {
        ElMessage.error('登录状态已失效，请重新登录')
        redirectToLogin()
      } else {
        const data = error.response.data
        ElMessage.error((data && (data.msg || data.message)) || `请求失败（HTTP ${status}）`)
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请稍后重试')
    } else {
      ElMessage.error('网络异常，请检查后端服务是否启动')
    }
    return Promise.reject(error)
  }
)

export default service
