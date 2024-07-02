package org.fd.ase.grp15.ase_contribute_service.entity;

public enum ContributeStatus {
    /*
    未审稿
     */
    WAITCHECK(0),
    /*
    审稿中
    */
    CHECKING(1),
    /*
    复议中
    */
    REBUTTALING(2),
    /*
    录用
     */
    ACCEPTED(100),
    /*
    未录用
     */
    DECLINED(101);

    private int code;

    ContributeStatus(int code) {
        this.code = code;
    }

    public int getCode(){return code;}

    public ContributeStatus getStatus(int code){
        switch (code){
            case 0: return WAITCHECK;
            case 1: return CHECKING;
            case 2: return REBUTTALING;
            case 100: return ACCEPTED;
            case 101: return DECLINED;
            default: return null;
        }
    }


}
