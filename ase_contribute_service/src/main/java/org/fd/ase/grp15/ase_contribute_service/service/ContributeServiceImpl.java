package org.fd.ase.grp15.ase_contribute_service.service;

import org.apache.dubbo.config.annotation.DubboReference;
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
import java.util.Date;
import java.util.Objects;

@Service
public class ContributeServiceImpl {

    @Autowired
    private ContributeRepository repository;
    @DubboReference(check = false, providedBy = "ase-user-service", providerNamespace = "lab2-services", providerPort = 20881)
    private IUserConferenceRoleService iUserConferenceRoleService;

    @DubboReference(check = false, providedBy = "ase-conference-service", providerNamespace = "lab2-services", providerPort = 20883)
    private IConferenceService conferenceService;

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

        Contribution contribution = new Contribution(in.getUsername(), in.getRealName(), in.getConferenceName(),
                in.getTitle(), in.getAbstractContent(), in.getEssayId(), time);
        // 给予用户author身份
        iUserConferenceRoleService.addRoleToUserInConference(in.getUsername(), in.getConferenceName(), ConferenceRole.AUTHOR);
        repository.save(contribution);
        return "投稿成功";
    }

    public List<ListContribution> listContributionsByUsername(String author) {
        List<Contribution> contributions = repository.findAllByAuthor(author);
        return transferToListVo(contributions);
    }

    public List<ListContribution> listContibutionsByConferenceName(String name) {
        List<Contribution> contributions = repository.findAllByConferenceName(name);
        return transferToListVo(contributions);
    }

    public Contribution detailById(String idStr) {
        long id = Long.parseLong(idStr);
        return repository.findContributionById(id);
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


}
