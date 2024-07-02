package org.fd.ase.grp15.ase_conference_service.controller.interior;

import org.fd.ase.grp15.ase_conference_service.service.impl.ConferenceServiceImpl;
import org.fd.ase.grp15.common.iservice.IConferenceService;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/interior/conference")
@RestController
public class InteriorConferenceController implements IConferenceService {

    @Autowired
    private ConferenceServiceImpl conferenceService;

    @GetMapping("/conference-info")
    @Override
    public ConferenceDTO getConferenceInfoByName(String conferenceName) {
        return conferenceService.getConferenceInfoByName(conferenceName);
    }

    @PostMapping("/change-conference-status")
    @Override
    public String changeConferenceStatus(String conferenceName, String status) {
        return conferenceService.changeConferenceStatus(conferenceName, status);
    }
}
