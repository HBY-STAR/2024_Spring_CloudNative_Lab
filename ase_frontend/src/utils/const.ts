export const invitationStatus = ['待确认', '已同意', '已拒绝']
export const paperStatus = (status: number): string => {
    switch (status){
        case 0: return '未审稿';
        case 1: return '审稿中';
        case 2: return '复审中';
        case 100: return '已录用';
        case 101: return '未录用';
        default: return '---';

    }
}

export const reviewStatus = (status: number): string => {
    switch (status){
        case 0: return '待审稿';
        case 1: return '未提交rebuttal';
        case 2: return '复审中';
        case 100: return '已完成';
        default: return '---'
    }
}

export const getScore = (score: number): number => {
    switch (score){
        case -2: return 1;
        case -1: return 2;
        case 1: return 3;
        case 2: return 4;
        default: return 0;
    }
}

export const toScore = (score: number): number => {
    switch (score){
        case 1: return -2;
        case 2: return -1;
        case 3: return 1;
        case 4: return 2;
        default: return 0;
    }
}
