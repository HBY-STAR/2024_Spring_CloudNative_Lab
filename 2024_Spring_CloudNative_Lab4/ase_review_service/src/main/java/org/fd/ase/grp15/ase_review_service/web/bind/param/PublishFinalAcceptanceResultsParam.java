package org.fd.ase.grp15.ase_review_service.web.bind.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PublishFinalAcceptanceResultsParam {

    @Data
    @NoArgsConstructor
    public static class In {
        @NotNull(message = "conferenceName不能为空")
        @NotBlank(message = "conferenceName不能为空")
        private String conferenceName;

    }
}
