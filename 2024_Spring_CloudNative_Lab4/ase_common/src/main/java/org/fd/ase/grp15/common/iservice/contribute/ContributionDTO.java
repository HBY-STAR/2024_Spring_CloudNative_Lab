package org.fd.ase.grp15.common.iservice.contribute;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ContributionDTO implements Serializable {

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
}
