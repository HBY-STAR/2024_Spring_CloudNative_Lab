import request from '@/utils/request'

// 投稿
export const contributePaper = (params: any) =>
  request({
    url: '/contribute',
    method: 'post',
    data: params
  })

//重新投稿
export const reContribute = (params: any) =>
    request({
        url: '/contribute/update',
        method: 'post',
        data: params
    })
// 获取用户的投稿记录
export const getMyPapers = (username: any) =>
  request({
    url: `/contribute/listByName/${username}`,
    method: 'get'
  })

// 获取会议的投稿记录
export const getConferencePapers = (conferenceName: any) =>
  request({
    url: `/contribute/listByConference/${conferenceName}`,
    method: 'get',
  })

// 获取投稿详情
export const getPaperDetail = (contributeId: any) =>
  request({
    url: `/contribute/detail/${contributeId}`,
    method: 'get'
  })
