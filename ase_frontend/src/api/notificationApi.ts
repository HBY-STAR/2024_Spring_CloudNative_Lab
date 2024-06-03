import request from '@/utils/request'

// 获取自己发送的邀请
export const handleSend = (username: any) =>
  request({
    url: `/notification/search/allSentInvitation/${username}`,
    method: 'get'
  })

// 获取自己收到的邀请
export const handleReceive = (username: any) =>
  request({
    url: `/notification/search/allReceivedInvitation/${username}`,
    method: 'get'
  })

// 处理邀请
export const handleInvitation = (params: any) =>
  request({
    url: '/notification/handleInvitation',
    method: 'post',
    data: params
  })

// 邀请PCMember
export const invitePCMember = (params: any) =>
  request({
    url: '/notification/invitePCMember',
    method: 'post',
    data: params
  })

// 查询会议的邀请记录
export const handleConferenceInvitation = (conferenceName: any) =>
  request({
    url: `/notification/search/conferenceInvitations/${conferenceName}`,
    method: 'get'
  })
