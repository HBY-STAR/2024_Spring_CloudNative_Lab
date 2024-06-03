package org.fd.ase.grp15.ase_contribute_service.controller.interior;

import org.fd.ase.grp15.ase_contribute_service.service.ContributeServiceImpl;
import org.fd.ase.grp15.common.iservice.IContributeService;
import org.fd.ase.grp15.common.iservice.contribute.ContributionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@RestController
@RequestMapping("/interior/contribute")
public class InteriorContributeController implements IContributeService {

    @Autowired
    private ContributeServiceImpl contributeService;

    @GetExchange(url = "/contribution-info/by-id/{id}")
    @Override
    public ContributionDTO byId(@PathVariable long id) {
        return contributeService.byId(id);
    }

    @PostExchange(url = "/change-contribution-status")
    @Override
    public String changeContributionStatus(@RequestParam Long contributeId, @RequestParam int status) {
        return contributeService.changeContributionStatus(contributeId, status);
    }

    @GetExchange(url = "/contribution-info/by-conference-name/{conferenceName}")
    @Override
    public List<ContributionDTO> findAllByConferenceName(@PathVariable String conferenceName) {
        return contributeService.findAllByConferenceName(conferenceName);
    }
}
