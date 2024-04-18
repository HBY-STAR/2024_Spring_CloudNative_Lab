import axios from 'axios'; // 引入axios
import { getToken } from '@/utils/auth';
import router from '@/router';
import { ElMessage } from 'element-plus';

const service = axios.create({
  baseURL: import.meta.env.VITE_BASE_PATH as string,
  timeout: 99999
});
// http request 拦截器
service.interceptors.request.use(
  (config: any) => {
    // 全局添加 token
    if (getToken()) {
      config.headers['satoken'] = getToken();
    }
    return config;
  },
  (error) => {
    console.error(error);
    return Promise.reject(error);
  }
);
// http response 拦截器
service.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // 网络超时
    if (error.message && error.message.includes('timeout')) {
      console.error('请求超时');
      return error.response;
    }
    // 某个服务调用失败(当spring-cloud-gateway调用具体服务失败时
    if (error.response.data && error.response.data.requestId) {
      const path = error.response.data.path;
      let match = path.match(/^\/([^\/]+)/);
      match = match ? match[1] : '';
      const match_2_service_name = {
        'user': '用户服务',
        'notification': '通知服务',
        'conference': '会议服务',
        'contribute': '投稿服务',
        'file': '文件服务',
      };
      const serviceName = match_2_service_name[match] ? match_2_service_name[match] : '未知服务';
      ElMessage.error(`${serviceName}-调用失败`);
      return error.response;
    }
    if (error.response && error.response.status && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message);
      console.log(error.response);
      return error.response;
    }
    ElMessage.warning('接口访问异常');
    console.log(error.response);
    return error.response;
  }
);
export default service;
