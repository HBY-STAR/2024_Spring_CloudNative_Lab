package org.fd.ase.grp15.ase_user_service.controller.interior;

import org.fd.ase.grp15.ase_user_service.service.impl.UserConferenceRoleServiceImpl;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.fd.ase.grp15.common.iservice.user.dto.UserConferenceRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interior/user-conference-role")
public class IUserConferenceRoleController implements IUserConferenceRoleService {

    @Autowired
    private UserConferenceRoleServiceImpl userConferenceRoleService;

    @Override
    @GetMapping("/user-roles-in-conference")
    public List<ConferenceRole> loadUserRolesInConference(@RequestParam String username, @RequestParam String conferenceName) {
        return userConferenceRoleService.loadUserRolesInConference(username, conferenceName);
    }

    @Override
    @GetMapping("/user-roles-in-conference2")
    public List<UserConferenceRoleDTO> loadUserRolesInConference2(@RequestParam String username, @RequestParam String conferenceName) {
        return userConferenceRoleService.loadUserRolesInConference2(username, conferenceName);
    }

    @Override
    @GetMapping("/pc-member-in-conference")
    public List<UserConferenceRoleDTO> loadPC_MEMBERInConference(@RequestParam String conferenceName) {
        return userConferenceRoleService.loadPC_MEMBERInConference(conferenceName);
    }

    @Override
    @GetMapping("/user-roles-in-all-conferences")
    public List<UserConferenceRoleDTO> loadUserRolesInConference(@RequestParam String username) {
        return userConferenceRoleService.loadUserRolesInConference(username);
    }

    @Override
    @PostMapping("/add-role-to-user-in-conference/{username}/{conferenceName}/{role}")
    public String addRoleToUserInConference(@PathVariable String username, @PathVariable String conferenceName, @PathVariable ConferenceRole role) {
        return userConferenceRoleService.addRoleToUserInConference(username, conferenceName, role);
    }

    @Override
    @PostMapping("/add-role-to-user-in-conference2/{username}/{conferenceName}/{role}")
    public String addRoleToUserInConference(@PathVariable String username, @PathVariable String conferenceName, @PathVariable ConferenceRole role, @RequestBody List<String> responsibleTopics) {
        return userConferenceRoleService.addRoleToUserInConference(username, conferenceName, role, responsibleTopics);
    }

    @Override
    @GetMapping("/check-role-of-user-in-conference")
    public Boolean checkRoleOfUserInConference(@RequestParam String username, @RequestParam String conferenceName, @RequestParam ConferenceRole role) {
        return userConferenceRoleService.checkRoleOfUserInConference(username, conferenceName, role);
    }
}
