import request from '@/utils/request'

export const fistReview = (params: any) =>
    request({
        url: '/review/submitReviewResult',
        method: 'post',
        data: params,
    })

export const secondReview = (params: any) =>
    request({
        url: '/review/submitRebuttalResult',
        method: 'post',
        data: params,
    })

export const rebuttal = (params: any) =>
    request({
        url: '/review/submitRebuttal',
        method: 'post',
        data: params,
    })

export const startReview = (params: any) =>
    request({
        url: '/review/startReview',
        method: 'post',
        data: params,
    })

export const publish = (params: any) =>
    request({
        url: '/review/publishReviewResults',
        method: 'post',
        data: params,
    })

export const publishFinal = (params: any) =>
    request({
        url: '/review/publishFinalAcceptanceResults',
        method: 'post',
        data: params,
    })

export const getAllReviews = () =>
    request({
        url: '/review/essaysAssignedToMe',
        method: 'get',
    })