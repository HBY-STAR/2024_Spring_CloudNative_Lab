import request from '@/utils/request'

// 下载文件
export const downloadFile = (id: any) =>
  request({
    url: `/file/download/${id}`,
    method: 'get'
  })