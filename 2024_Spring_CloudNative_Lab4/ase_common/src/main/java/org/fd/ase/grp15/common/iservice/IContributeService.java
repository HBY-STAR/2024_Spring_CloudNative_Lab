package org.fd.ase.grp15.common.iservice;

import jakarta.validation.Valid;
import org.fd.ase.grp15.common.iservice.contribute.ContributionDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange(url = "http://ase-contribute-service:8084/interior/contribute")
public interface IContributeService {

    @GetExchange(url = "/contribution-info/by-id/{id}")
    public ContributionDTO byId(@PathVariable long id);

    /**
     * 更改投稿状态信息
     *
     * @param contributeId 投稿Id,
     * @param status 投稿状态:未审稿(0),审稿中(1),复议中(2),录用(100),未录用(101)
     * @return 更改结果
     */
    @PostExchange(url = "/change-contribution-status")
    public String changeContributionStatus(@RequestParam Long contributeId, @RequestParam int status);

    @GetExchange(url = "/contribution-info/by-conference-name/{conferenceName}")
    public List<ContributionDTO> findAllByConferenceName(@PathVariable String conferenceName);
}
