package org.fd.ase.grp15.common.iservice.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO implements Serializable {
    private Integer seq;

    private Integer status;

    private String reviewerUsername;

    private Integer reviewScore;

    private String reviewComment;

    private Integer reviewConfidence;

    private String rebuttalMessage;

    private Integer rebuttalScore;
}
