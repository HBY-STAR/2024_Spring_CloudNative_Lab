package org.fd.ase.grp15.ase_contribute_service.config;

import org.fd.ase.grp15.ase_contribute_service.entity.Contribution;
import org.fd.ase.grp15.common.iservice.contribute.ContributionDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<Contribution, ContributionDTO> typeMap = modelMapper.createTypeMap(Contribution.class, ContributionDTO.class);
        typeMap.setConverter(ctx -> {
            Contribution src = ctx.getSource();
            ContributionDTO dest = new ContributionDTO();
            dest.setId(src.getId());
            dest.setConferenceName(src.getConferenceName());
            dest.setAuthor(src.getAuthor());
            dest.setRealName(src.getRealName());
            List<org.fd.ase.grp15.ase_contribute_service.entity.AuthorInfo> writers1 = src.getWriters();
            List<org.fd.ase.grp15.common.iservice.contribute.AuthorInfo> writers2;
            if (writers1 == null) {
                writers2 = new ArrayList<>();
            } else {
                writers2 = writers1.stream().map(w1 -> modelMapper.map(w1, org.fd.ase.grp15.common.iservice.contribute.AuthorInfo.class)).toList();
            }
            dest.setWriters(writers2);
            dest.setTitle(src.getTitle());
            dest.setAbstractContent(src.getAbstractContent());
            dest.setEssayId(src.getEssayId());
            dest.setStatus(src.getStatus());
            dest.setContributeTime(src.getContributeTime());
            dest.setUpdateTime(src.getUpdateTime());
            dest.setTopics(src.getTopics());
            return dest;
        });

        return modelMapper;
    }
}
