package org.fd.ase.grp15.ase_contribute_service.controller;

import org.fd.ase.grp15.ase_contribute_service.request.ContributeRequest;
import org.fd.ase.grp15.ase_contribute_service.response.ResponseWithData;
import org.fd.ase.grp15.ase_contribute_service.service.ContributeServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

@RestController
public class ContributeController {
    @Resource
    private ContributeServiceImpl service;

    @PostMapping("/contribute")
    public ResponseWithData contribute(@RequestBody @Validated ContributeRequest.In in){
        return new ResponseWithData(service.contribute(in));
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
