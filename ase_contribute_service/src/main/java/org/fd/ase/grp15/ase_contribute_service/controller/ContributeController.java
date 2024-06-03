package org.fd.ase.grp15.ase_contribute_service.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.fd.ase.grp15.ase_contribute_service.request.ContributeRequest;
import org.fd.ase.grp15.ase_contribute_service.response.ResponseWithData;
import org.fd.ase.grp15.ase_contribute_service.service.ContributeServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
public class ContributeController {
    @Resource
    private ContributeServiceImpl service;

    @PostMapping("/contribute")
    public ResponseWithData contribute(@RequestBody @Validated ContributeRequest.In in){
        return new ResponseWithData(service.contribute(in));
    }

    @PostMapping("/contribute/update")
    public ResponseWithData updateContribute(@RequestBody @Validated ContributeRequest.UpdateContribute updateContribute){
        return new ResponseWithData(service.updateContribute(updateContribute));
    }

    @GetMapping("/contribute/listByName/{username}")
    public ResponseWithData listContribute(@PathVariable("username") String username){
        return new ResponseWithData(service.listContributionsByUsername(username), "");
    }

    @GetMapping("/contribute/listByConference/{conferenceName}")
    public ResponseWithData conferenceContribute(@PathVariable("conferenceName") String name){
        return new ResponseWithData(service.listContibutionsByConferenceName(name), "");
    }

    @GetMapping("/contribute/detail/{contributeId}")
    public ResponseWithData contributeDetail(@PathVariable("contributeId") String contributeId){
        return new ResponseWithData(service.detailById(contributeId), "");
    }

}
