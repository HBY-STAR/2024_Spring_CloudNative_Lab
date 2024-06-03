// @ts-ignore
import Cookies from 'js-cookie'
const TokenKey = 'satoken'
export const getToken = () => Cookies.get(TokenKey)
export const setToken = (token: any) => Cookies.set(TokenKey, token, { expires: 1 })
export const delToken = () => Cookies.remove(TokenKey)

export const getUsername = () => Cookies.get('username')
export const setUsername = (username: any) => Cookies.set('username', username, { expires: 1 })
export const delUsername = () => Cookies.remove('username')
