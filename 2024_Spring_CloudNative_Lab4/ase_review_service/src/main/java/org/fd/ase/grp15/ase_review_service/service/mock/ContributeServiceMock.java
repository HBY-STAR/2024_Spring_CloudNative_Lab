package org.fd.ase.grp15.ase_review_service.service.mock;

import org.fd.ase.grp15.common.iservice.IContributeService;
import org.fd.ase.grp15.common.iservice.contribute.ContributionDTO;

import java.util.ArrayList;
import java.util.List;

public class ContributeServiceMock implements IContributeService {

    @Override
    public ContributionDTO byId(long id) {
        ContributionDTO dto = new ContributionDTO();
        dto.setId(-1L);
        dto.setConferenceName("fallback-conference");
        dto.setAuthor("fallback-author");
        dto.setRealName("fallback-realName");
        dto.setWriters(new ArrayList<>());
        dto.setTitle("fallback-title");
        dto.setAbstractContent("fallback-abstractContent");
        dto.setEssayId("fallback-essayId");
        dto.setStatus(0);
        dto.setContributeTime("fallback-contributeTime");
        dto.setUpdateTime("fallback-updateTime");
        dto.setTopics(new ArrayList<>());
        return dto;
    }

    @Override
    public String changeContributionStatus(Long contributeId, int status) {
        return null;
    }

    @Override
    public List<ContributionDTO> findAllByConferenceName(String conferenceName) {
        List<ContributionDTO> results = new ArrayList<>();
        ContributionDTO dto = new ContributionDTO();
        dto.setId(-1L);
        dto.setConferenceName("fallback-conference");
        dto.setAuthor("fallback-author");
        dto.setRealName("fallback-realName");
        dto.setWriters(new ArrayList<>());
        dto.setTitle("fallback-title");
        dto.setAbstractContent("fallback-abstractContent");
        dto.setEssayId("fallback-essayId");
        dto.setStatus(0);
        dto.setContributeTime("fallback-contributeTime");
        dto.setUpdateTime("fallback-updateTime");
        dto.setTopics(new ArrayList<>());
        results.add(dto);
        return results;
    }
}
