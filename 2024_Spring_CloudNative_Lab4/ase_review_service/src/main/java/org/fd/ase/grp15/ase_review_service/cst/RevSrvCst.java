package org.fd.ase.grp15.ase_review_service.cst;

public class RevSrvCst {
    public static final int SCORE_REJECT = -2;
    public static final int SCORE_WEAK_REJECT = -1;
    public static final int SCORE_WEAK_ACCEPT = 1;
    public static final int SCORE_ACCEPT = 2;

    public static final int CONFIDENCE_VERY_LOW = -2;
    public static final int CONFIDENCE_LOW = -1;
    public static final int CONFIDENCE_HIGH = 1;
    public static final int CONFIDENCE_VERY_HIGH = 2;

    public static final int REV_RBT_WAITING_FOR_REVIEW = 0;
    public static final int REV_RBT_WAITING_FOR_REBUTTAL = 1;
    public static final int REV_RBT_REBUTTAL_SUBMITTED = 2;
    public static final int REV_RBT_DONE = 100;


    public static final String CONFERENCE_PREPARING = "准备中";

    public static final String CONFERENCE_SUBMITTING = "投稿中";

    public static final String CONFERENCE_REVIEWING = "审稿中";

    public static final String CONFERENCE_REBUTTING = "复议中";

    public static final String CONFERENCE_DONE = "完成";


    public static final Integer CONTRIB_STATUS_UNDER_REBUTTAL = 2;
    public static final Integer CONTRIB_STATUS_ACCEPTED = 100;

    public static final Integer CONTRIB_STATUS_REJECTED = 101;
}
