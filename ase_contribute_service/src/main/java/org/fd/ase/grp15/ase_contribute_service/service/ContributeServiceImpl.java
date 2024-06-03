package org.fd.ase.grp15.ase_contribute_service.service;

import org.fd.ase.grp15.ase_contribute_service.entity.ContributeStatus;
import org.fd.ase.grp15.ase_contribute_service.entity.Contribution;
import org.fd.ase.grp15.ase_contribute_service.entity.vo.ListContribution;
import org.fd.ase.grp15.ase_contribute_service.repository.ContributeRepository;
import org.fd.ase.grp15.ase_contribute_service.request.ContributeRequest;
import org.fd.ase.grp15.ase_contribute_service.response.ContributionDetailDTO;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.IConferenceService;
import org.fd.ase.grp15.common.iservice.IContributeService;
import org.fd.ase.grp15.common.iservice.IReviewService;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.fd.ase.grp15.common.iservice.contribute.ContributionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class ContributeServiceImpl implements IContributeService {

    @Autowired
    private ContributeRepository repository;

    @Autowired
    private IUserConferenceRoleService iUserConferenceRoleService;

    @Autowired
    private IConferenceService conferenceService;

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private ModelMapper modelMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String contribute(ContributeRequest.In in) {
        Date date = new Date();
        String time = sdf.format(date);

        // 获得conference信息，并检查是否可以投稿
        ConferenceDTO cfr = conferenceService.getConferenceInfoByName(in.getConferenceName());
        if (!Objects.equals(cfr.getConferenceStatus(), "投稿中")) {
            throw new RuntimeException("该会议还未开启投稿");
        }
        if (!LocalDateTime.now().isBefore(cfr.getSubmissionDeadline())) {
            throw new RuntimeException("投稿已于" + cfr.getSubmissionDeadline() + "截止");
        }

        Contribution contribution = new Contribution(in.getUsername(), in.getRealName(),in.getWriters(),in.getTopics(), in.getConferenceName(),
                in.getTitle(), in.getAbstractContent(), in.getEssayId(), time);
        // 给予用户author身份
        iUserConferenceRoleService.addRoleToUserInConference(in.getUsername(), in.getConferenceName(), ConferenceRole.AUTHOR);
        repository.save(contribution);
        return "投稿成功";
    }

    public String updateContribute(ContributeRequest.UpdateContribute updateContribute) {
        Date date = new Date();
        String time = sdf.format(date);

        long id = Long.parseLong(updateContribute.getId());
        Contribution contribution = repository.findContributionById(id);
        if (contribution == null) {
            throw new RuntimeException("原投稿不存在，请检查投稿");
        }

        if (contribution.getStatus() != ContributeStatus.WAITCHECK.getCode()){
            throw new RuntimeException("当前投稿已在审核或审核完毕，无法修改");
        }

        contribution.setConferenceName(updateContribute.getConferenceName());
        contribution.setAuthor(updateContribute.getUsername());
        contribution.setRealName(updateContribute.getRealName());
        contribution.setWriters(updateContribute.getWriters());
        contribution.setTitle(updateContribute.getTitle());
        contribution.setAbstractContent(updateContribute.getAbstractContent());
        contribution.setEssayId(updateContribute.getEssayId());
        contribution.setUpdateTime(time);
        contribution.setTopics(updateContribute.getTopics());
        repository.save(contribution);
        return "更新投稿成功";
    }

    public List<ListContribution> listContributionsByUsername(String author) {
        List<Contribution> contributions = repository.findAllByAuthor(author);
        return transferToListVo(contributions);
    }

    public List<ListContribution> listContibutionsByConferenceName(String name) {
        List<Contribution> contributions = repository.findAllByConferenceName(name);
        return transferToListVo(contributions);
    }

    public ContributionDetailDTO detailById(String idStr) {
        long id = Long.parseLong(idStr);
        Contribution contribution = repository.findContributionById(id);
        if (contribution == null) return null;
        ContributionDetailDTO contributionDetailDTO = modelMapper.map(contribution, ContributionDetailDTO.class);
        if (contribution.getStatus() != ContributeStatus.WAITCHECK.getCode() && contribution.getStatus() != ContributeStatus.WAITCHECK.getCode()){
            contributionDetailDTO.setReviewRebuttal(reviewService.getContributionReview(contribution.getId()));
        }
        return contributionDetailDTO;
    }

    private List<ListContribution> transferToListVo(List<Contribution> contributions) {
        List<ListContribution> res = new ArrayList<>();
        // long id, String conferenceName, String time, String title, int status
        for (Contribution contribution : contributions) {
            //转 vo
            ListContribution c = new ListContribution(contribution.getId(), contribution.getConferenceName(),
                    contribution.getContributeTime(), contribution.getTitle(), contribution.getStatus());
            res.add(c);
        }
        return res;
    }


    @Override
    public ContributionDTO byId(long id) {
        Contribution contribution = repository.findContributionById(id);
        if (contribution == null) {
            throw new RuntimeException("投稿不存在");
        }
        return modelMapper.map(contribution, ContributionDTO.class);
    }

    @Override
    @Transactional
    public String changeContributionStatus(Long contributeId, int status) {
        Contribution contribution = repository.findContributionById(contributeId);
        if (contribution == null) {
            throw new RuntimeException("投稿不存在");
        }
        contribution.setStatus(status);
        repository.save(contribution);
        return "修改状态成功";
    }

    @Override
    public List<ContributionDTO> findAllByConferenceName(String name) {
        List<Contribution> contributions = repository.findAllByConferenceName(name);
        List<ContributionDTO> results = new ArrayList<ContributionDTO>();
        for(Contribution contribution : contributions) {
            results.add(modelMapper.map(contribution, ContributionDTO.class));
        }
        return results;
    }
}
