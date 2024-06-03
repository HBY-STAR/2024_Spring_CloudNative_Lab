package org.fd.ase.grp15.ase_review_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Table(schema = "review_rebuttal")
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ReviewRebuttal {


    @Embeddable
    @Data
    @RequiredArgsConstructor
    @NoArgsConstructor
    public static class ReviewRebuttalId implements Serializable {
        @NonNull
        private Long contributionId;

        @NonNull
        private Integer seq;
    }

    @EmbeddedId
    @NonNull
    private ReviewRebuttalId id;

    @Column
    @NonNull
    private Integer status;

    @Column
    @NonNull
    private String conferenceName;

    @Column
    @NonNull
    private String reviewerUsername;

    @Column
    private Integer reviewScore; // 评审人对投稿的score

    @Column
    private String reviewComment; // 评审人对投稿的comment

    @Column
    private Integer reviewConfidence; // 评审人对自己的评审的confidence

    @Column
    private String rebuttalMessage; // Author提交的rebuttal message

    @Column
    private Integer rebuttalScore; // 评审人提交的最终的rebuttal score


}
