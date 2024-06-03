package org.fd.ase.grp15.ase_contribute_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fd.ase.grp15.ase_contribute_service.entity.AuthorInfo;
import org.fd.ase.grp15.common.iservice.review.ReviewDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributionDetailDTO {
    private Long id;

    private String conferenceName;

    private String author;

    private String realName;

    private List<AuthorInfo> writers;

    private String title;

    private String abstractContent;

    private String essayId;

    private int status;

    private String contributeTime;

    private String updateTime;

    private List<String> topics;

    private List<ReviewDTO> reviewRebuttal;
}
