package org.fd.ase.grp15.ase_user_service.config;

import org.fd.ase.grp15.ase_user_service.entity.ResponsibleTopic;
import org.fd.ase.grp15.ase_user_service.entity.UserConferenceRole;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.user.dto.UserConferenceRoleDTO;
import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    protected ModelMapper userModelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        TypeMap<UserConferenceRole, UserConferenceRoleDTO> typeMap = modelMapper.createTypeMap(UserConferenceRole.class, UserConferenceRoleDTO.class);
        typeMap.setConverter(ctx -> {
            UserConferenceRole src = ctx.getSource();
            UserConferenceRoleDTO dest = new UserConferenceRoleDTO();
            dest.setUsername(src.getId().getUsername());
            dest.setConferenceName(src.getId().getConferenceName());
            dest.setRole(ConferenceRole.valueOf(src.getId().getRole()));
            dest.setResponsibleTopics(src.getResponsibleTopics().stream().map(ResponsibleTopic::getName).toList());
            return dest;
        });
        return modelMapper;
    }
}
