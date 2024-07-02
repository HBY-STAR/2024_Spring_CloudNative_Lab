import request from '@/utils/request'

// 登录
export const getLogin = (params: any) =>
  request({
    url: '/user/login',
    method: 'post',
    data: params
  })

// 注册
export const register = (params: any) =>
  request({
    url: '/user/register',
    method: 'post',
    data: params
  })

// 注销
export const logOut = () =>
  request({
    url: '/user/logout',
    method: 'post'
  })

//查看自己在某会议的身份
export const handleConferenceRole = (conferenceName: any) =>
  request({
    url: `/user/userConferenceRole/self/${conferenceName}`,
    method: 'get'
  })

// 查看个人信息
export const getMyProfile = () =>
  request({
    url: `/user/userInfo/self`,
    method: 'get'
  })

//根据用户名查找用户
export const searchByUsername = (username: any) =>
  request({
    url: `/user/userInfo/byUsername/${username}`,
    method: 'get'
  })

//根据真实姓名查找用户
export const searchByRealName = (username: any) =>
  request({
    url: `/user/userInfo/byRealName/${username}`,
    method: 'get'
  })
