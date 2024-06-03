package org.fd.ase.grp15.ase_review_service.web.bind.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class EssayAssignedToMe {

    @NonNull
    private Long contributionId;

    @NonNull
    private String essayId;

    @NonNull
    private String username;

    @NonNull
    private String realName;

    @NonNull
    private String conferenceName;

    @NonNull
    private String title;

    @NonNull
    private String abstractContent;

    @NonNull
    private List<String> topics;

    @NonNull
    private ReviewRebuttalDTO reviewRebuttal;
}
