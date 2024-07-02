package org.fd.ase.grp15.ase_review_service.config;

import org.fd.ase.grp15.ase_review_service.web.bind.dto.ReviewRebuttalDTO;
import org.fd.ase.grp15.ase_review_service.entity.ReviewRebuttal;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<ReviewRebuttal, ReviewRebuttalDTO> typeMap = mapper.createTypeMap(ReviewRebuttal.class, ReviewRebuttalDTO.class);
        typeMap.setConverter(context -> {
            ReviewRebuttal src = context.getSource();
            ReviewRebuttalDTO des = new ReviewRebuttalDTO();

            des.setContributionId(src.getId().getContributionId());
            des.setSeq(src.getId().getSeq());
            des.setStatus(src.getStatus());
            des.setConferenceName(src.getConferenceName());
            des.setReviewerUsername(src.getReviewerUsername());
            des.setReviewComment(src.getReviewComment());
            des.setReviewScore(src.getReviewScore());
            des.setReviewConfidence(src.getReviewConfidence());
            des.setRebuttalMessage(src.getRebuttalMessage());
            des.setRebuttalScore(src.getRebuttalScore());

            return des;
        });

        return mapper;
    }

}
