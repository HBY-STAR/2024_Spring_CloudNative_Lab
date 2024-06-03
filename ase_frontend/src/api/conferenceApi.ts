import request from '@/utils/request'

// 获取所有会议信息
export const getAllConferences = () =>
  request({
    url: `/conference/getAllConferences`,
    method: 'get'
  })

// 查看我的会议申请
export const getAllApplications = () =>
  request({
    url: `/conference/getAllMyAppliedConference`,
    method: 'get'
  })

// 申请会议
export const applyConference = (params: any) =>
  request({
    url: '/conference/apply',
    method: 'post',
    data: params
  })

// 查看我的参与的会议
export const getAllMyConferences = () =>
  request({
    url: `/conference/getAllMyJoinedConference`,
    method: 'get'
  })

// 查看会议详情
export const getConferenceDetail = (conferenceName: any) =>
  request({
    url: `/conference/getConferenceInfoByName/${conferenceName}`,
    method: 'get'
  })

//查看自己在某会议的身份
export const getMyConferenceRole = (conferenceName: any) =>
  request({
    url: `/conference/getMyRoleInConference/${conferenceName}`,
    method: 'get'
  })

//开启投稿
export const startSubmiission = (params: any) =>
  request({
    url: '/conference/startSubmission',
    method: 'post',
    data: params
  })

// 管理员查看所有会议申请
export const getAllAdminApplications = () =>
  request({
    url: `/conference/getAllConferenceApplications`,
    method: 'get'
  })

// 审核会议
export const reviewConference = (params: any) =>
  request({
    url: '/conference/auditConference',
    method: 'post',
    data: params
  })

//根据会议名称获取所有topic
export const getAllTopics = (conferenceName: string) =>
    request({
        url: `/conference/getAllTopics/${conferenceName}`,
        method: 'post'
    })

