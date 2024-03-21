package org.fd.ase.grp15.ase_contribute_service.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.fd.ase.grp15.ase_contribute_service.entity.ContributeStatus;
import org.fd.ase.grp15.ase_contribute_service.entity.Contribution;
import org.fd.ase.grp15.ase_contribute_service.entity.vo.ListContribution;
import org.fd.ase.grp15.ase_contribute_service.repository.ContributeRepository;
import org.fd.ase.grp15.ase_contribute_service.request.ContributeRequest;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.IConferenceService;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@Service
public class ContributeServiceImpl {

    @Autowired
    private ContributeRepository contributeRepository;
    @DubboReference(check = false)
    private IUserConferenceRoleService iUserConferenceRoleService;

    @DubboReference(check = false)
    private IConferenceService conferenceService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String contribute(ContributeRequest.In in) {
        // TODO
        // 步骤如下：
        // 1. 调用conferenceService.getConferenceInfoByName获取会议信息(基于dubbo的rpc调用)
        // 2. 检查会议状态是否为“投稿中”，以及投稿截止时间是否已过
        // 3. 调用iUserConferenceRoleService.addRoleToUserInConference给用户添加author身份(基于dubbo的rpc调用)
        // 4. 创建Contribution对象并保存到数据库
        // 如果在过程中出现异常，可以抛出RuntimeException
        try {
            ConferenceDTO conference = conferenceService.getConferenceInfoByName(in.getConferenceName());
            if (conference == null){
                return "会议不存在";
            }
            else if (!conference.getConferenceStatus().equals("投稿中")){
                return "会议未在投稿阶段";
            }
            else if (!LocalDateTime.now().isBefore(conference.getSubmissionDeadline())){
                return "会议投稿已截止";
            }
            //为用户添加Author身份
            iUserConferenceRoleService.addRoleToUserInConference(in.getUsername(), conference.getConferenceName(), ConferenceRole.AUTHOR);

            //创建并保存新的投稿
            Contribution contribution = new Contribution();
            contribution.setAuthor(in.getUsername());
            contribution.setRealName(in.getRealName());
            contribution.setConferenceName(conference.getConferenceName());
            contribution.setTitle(in.getTitle());
            contribution.setAbstractContent(in.getAbstractContent());
            contribution.setEssayId(in.getEssayId());
            contribution.setContributeTime(sdf.format(LocalDateTime.now()));
            contribution.setStatus(0);
            contributeRepository.save(contribution);

            return "投稿成功";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ListContribution> listContributionsByUsername(String author) {
        // TODO
        List<Contribution> temp = contributeRepository.findAllByAuthor(author);
        if (temp == null)
            return null;
        return temp.stream().map(contribution -> new ListContribution(
                contribution.getId(), contribution.getConferenceName(), contribution.getContributeTime(), contribution.getTitle(), contribution.getStatus()
        )).collect(Collectors.toList());
    }

    public List<ListContribution> listContributionsByConferenceName(String name) {
        // TODO
        List<Contribution> temp = contributeRepository.findAllByConferenceName(name);
        if (temp == null)
            return null;
        return temp.stream().map(contribution -> new ListContribution(
                contribution.getId(), contribution.getConferenceName(), contribution.getContributeTime(), contribution.getTitle(), contribution.getStatus()
        )).collect(Collectors.toList());
    }

    public Contribution detailById(String idStr) {
        // TODO
        Long id;
        try{
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return contributeRepository.findContributionById(id);
    }
}
