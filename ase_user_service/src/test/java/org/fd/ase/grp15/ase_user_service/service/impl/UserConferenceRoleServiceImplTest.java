package org.fd.ase.grp15.ase_user_service.service.impl;

import org.fd.ase.grp15.ase_user_service.repository.UserConferenceRoleRepository;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.user.dto.UserConferenceRoleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class UserConferenceRoleServiceImplTest {

    @Autowired
    private UserConferenceRoleServiceImpl userConferenceRoleService;

    @Autowired
    private UserConferenceRoleRepository userConferenceRoleRepository;

    void prepare() {
        userConferenceRoleRepository.deleteAll();
        userConferenceRoleService.addRoleToUserInConference("huajuan", "conference1", ConferenceRole.PC_MEMBER, new ArrayList<>() {{
            add("topic1");
            add("topic2");
        }});
        userConferenceRoleService.addRoleToUserInConference("huajuan", "conference1", ConferenceRole.AUTHOR);
        userConferenceRoleService.addRoleToUserInConference("tom", "conference2", ConferenceRole.PC_MEMBER, new ArrayList<>() {{
            add("topic1");
            add("topic3");
            add("topic4");
        }});
    }

    @Test
    void loadUserRolesInConferenceByUsernameAndConferenceName() {
        prepare();

        List<ConferenceRole> roles1 = userConferenceRoleService.loadUserRolesInConference("huajuan", "conference1");
        List<ConferenceRole> roles2 = userConferenceRoleService.loadUserRolesInConference("tom", "conference2");

        assertEquals(2, roles1.size());
        assertEquals(1, roles2.size());

        assertTrue(roles1.contains(ConferenceRole.PC_MEMBER));
        assertTrue(roles1.contains(ConferenceRole.AUTHOR));
        assertTrue(roles2.contains(ConferenceRole.PC_MEMBER));
    }

    @Test
    @Transactional
    void loadUserRolesInConferenceByUsernameAndConferenceName2() {
        prepare();

        List<UserConferenceRoleDTO> roles1 = userConferenceRoleService.loadUserRolesInConference2("huajuan", "conference1");
        List<UserConferenceRoleDTO> roles2 = userConferenceRoleService.loadUserRolesInConference2("tom", "conference2");

        assertEquals(2, roles1.size());
        assertEquals(1, roles2.size());

        assertTrue(roles1.stream().anyMatch(role -> role.getRole().equals(ConferenceRole.PC_MEMBER)));
        assertTrue(roles1.stream().anyMatch(role -> role.getRole().equals(ConferenceRole.AUTHOR)));
        assertTrue(roles2.stream().anyMatch(role -> role.getRole().equals(ConferenceRole.PC_MEMBER)));

        roles1.stream().filter(role -> role.getRole().equals(ConferenceRole.PC_MEMBER)).findFirst().get().getResponsibleTopics().contains("topic1");
        roles1.stream().filter(role -> role.getRole().equals(ConferenceRole.PC_MEMBER)).findFirst().get().getResponsibleTopics().contains("topic2");

        roles2.stream().filter(role -> role.getRole().equals(ConferenceRole.PC_MEMBER)).findFirst().get().getResponsibleTopics().contains("topic1");
        roles2.stream().filter(role -> role.getRole().equals(ConferenceRole.PC_MEMBER)).findFirst().get().getResponsibleTopics().contains("topic3");
        roles2.stream().filter(role -> role.getRole().equals(ConferenceRole.PC_MEMBER)).findFirst().get().getResponsibleTopics().contains("topic4");
    }

    @Test
    void loadUserRolesInConferenceByUsername() {
        prepare();

        userConferenceRoleService.addRoleToUserInConference("huajuan", "conference2", ConferenceRole.PC_MEMBER);

        List<UserConferenceRoleDTO> roles1 = userConferenceRoleService.loadUserRolesInConference("huajuan");
        List<UserConferenceRoleDTO> roles2 = userConferenceRoleService.loadUserRolesInConference("tom");

        assertEquals(3, roles1.size());
        assertEquals(1, roles2.size());
    }

    @Test
    void addRoleToUserInConference() {
        loadUserRolesInConferenceByUsernameAndConferenceName();
    }

    @Test
    void removeRoleFromUserInConference() {
        prepare();

        userConferenceRoleService.removeRoleFromUserInConference("huajuan", "conference1", ConferenceRole.PC_MEMBER);
        userConferenceRoleService.removeRoleFromUserInConference("huajuan", "conference1", ConferenceRole.AUTHOR);

        userConferenceRoleService.removeRoleFromUserInConference("tom", "conference2", ConferenceRole.PC_MEMBER);

        List<ConferenceRole> roles1 = userConferenceRoleService.loadUserRolesInConference("huajuan", "conference1");
        List<ConferenceRole> roles2 = userConferenceRoleService.loadUserRolesInConference("huajuan", "conference2");

        assertEquals(0, roles1.size());
        assertEquals(0, roles2.size());
    }

    @Test
    void checkRoleOfUserInConference() {
        prepare();

        assertTrue(userConferenceRoleService.checkRoleOfUserInConference("huajuan", "conference1", ConferenceRole.PC_MEMBER));
        assertTrue(userConferenceRoleService.checkRoleOfUserInConference("huajuan", "conference1", ConferenceRole.AUTHOR));
        assertFalse(userConferenceRoleService.checkRoleOfUserInConference("huajuan", "conference1", ConferenceRole.CHAIR));

        assertTrue(userConferenceRoleService.checkRoleOfUserInConference("tom", "conference2", ConferenceRole.PC_MEMBER));
        assertFalse(userConferenceRoleService.checkRoleOfUserInConference("tom", "conference2", ConferenceRole.AUTHOR));
    }
}