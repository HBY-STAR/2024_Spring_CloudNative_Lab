package org.fd.ase.grp15.ase_user_service.service.impl;

import org.fd.ase.grp15.ase_user_service.entity.ResponsibleTopic;
import org.fd.ase.grp15.ase_user_service.entity.UserConferenceRole;
import org.fd.ase.grp15.ase_user_service.repository.UserConferenceRoleRepository;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.fd.ase.grp15.common.iservice.user.dto.UserConferenceRoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserConferenceRoleServiceImpl implements IUserConferenceRoleService {

    @Autowired
    private UserConferenceRoleRepository userConferenceRoleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ConferenceRole> loadUserRolesInConference(String username, String conferenceName) {
        List<UserConferenceRole> entities = userConferenceRoleRepository.findAllByUsernameAndConferenceName(username, conferenceName);
        List<ConferenceRole> res = new ArrayList<>();
        for (var e : entities) {
            for (var v : ConferenceRole.values()) {
                if (Objects.equals(e.getId().getRole(), v.toString())) {
                    res.add(v);
                }
            }
        }
        return res;
    }


    @Override
    public List<UserConferenceRoleDTO> loadUserRolesInConference2(String username, String conferenceName) {
        List<UserConferenceRole> entities = userConferenceRoleRepository.findAllByUsernameAndConferenceName(username, conferenceName);
        return entities.stream().map(e -> modelMapper.map(e, UserConferenceRoleDTO.class)).toList();
    }

    @Override
    public List<UserConferenceRoleDTO> loadPC_MEMBERInConference(String conferenceName) {
        List<UserConferenceRole> entities = userConferenceRoleRepository.findAllByConferenceNameAndRole(conferenceName);
        return entities.stream().map(e -> modelMapper.map(e, UserConferenceRoleDTO.class)).toList();
    }

    @Override
    public List<UserConferenceRoleDTO> loadUserRolesInConference(String username) {
        List<UserConferenceRole> entities = userConferenceRoleRepository.findAllByUsername(username);
        List<UserConferenceRoleDTO> res = new ArrayList<>();

        for (var e : entities) {
            res.add(modelMapper.map(e, UserConferenceRoleDTO.class));
        }
        return res;
    }


    @Override
    @Transactional
    public String addRoleToUserInConference(String username, String conferenceName, ConferenceRole role) {
        return addRoleToUserInConference(username, conferenceName, role, new ArrayList<>());
    }

    @Override
    public String addRoleToUserInConference(String username, String conferenceName, ConferenceRole role, List<String> responsibleTopics) {
        var id = new UserConferenceRole.UserConferenceRoleId(username, conferenceName, role.toString());
        UserConferenceRole entity = new UserConferenceRole(id);
        entity.setResponsibleTopics(responsibleTopics.stream().map(ResponsibleTopic::new).toList());

        if (userConferenceRoleRepository.existsById(id)) {
            return "已存在";
        }

        userConferenceRoleRepository.save(entity);
        return "添加成功";
    }

    /**
     * 为用户移除会议角色
     *
     * @param username       用户名
     * @param conferenceName 会议名
     * @param role           角色
     * @return 移除结果
     */
    public String removeRoleFromUserInConference(String username, String conferenceName, ConferenceRole role) {
        var id = new UserConferenceRole.UserConferenceRoleId(username, conferenceName, role.toString());
        if (userConferenceRoleRepository.existsById(id)) {
            userConferenceRoleRepository.deleteById(id);
            return "删除成功";
        }

        return "不存在";
    }

    @Override
    public Boolean checkRoleOfUserInConference(String username, String conferenceName, ConferenceRole role) {
        var id = new UserConferenceRole.UserConferenceRoleId(username, conferenceName, role.toString());
        return userConferenceRoleRepository.existsById(id);
    }
}
