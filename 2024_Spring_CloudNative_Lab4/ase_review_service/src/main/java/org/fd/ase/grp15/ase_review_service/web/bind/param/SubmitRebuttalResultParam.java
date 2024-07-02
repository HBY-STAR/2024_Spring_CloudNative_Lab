package org.fd.ase.grp15.ase_review_service.web.bind.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SubmitRebuttalResultParam {

    @Data
    @NoArgsConstructor
    public static class In {
        @NotNull(message = "contributionId不能为空")
        private Long contributionId;

        @NotNull(message = "seq不能为空")
        private Integer seq;

        @NotNull(message = "rebuttalScore不能为空")
        private Integer rebuttalScore = 0;

    }
}
