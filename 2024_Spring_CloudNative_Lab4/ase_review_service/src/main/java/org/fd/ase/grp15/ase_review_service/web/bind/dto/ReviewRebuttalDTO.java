package org.fd.ase.grp15.ase_review_service.web.bind.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ReviewRebuttalDTO {

    @NonNull
    private Long contributionId;

    @NonNull
    private Integer seq;

    @NonNull
    private Integer status;

    @NonNull
    private String reviewerUsername;

    @NonNull
    private String conferenceName;

    private Integer reviewScore; // 评审人对投稿的score

    private String reviewComment; // 评审人对投稿的comment

    private Integer reviewConfidence; // 评审人对自己的评审的confidence

    private String rebuttalMessage; // Author提交的rebuttal message

    private Integer rebuttalScore; // 评审人提交的最终的rebuttal score
}
