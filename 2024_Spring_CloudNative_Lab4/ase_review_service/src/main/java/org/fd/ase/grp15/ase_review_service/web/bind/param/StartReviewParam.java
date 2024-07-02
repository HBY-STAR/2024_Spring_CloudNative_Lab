package org.fd.ase.grp15.ase_review_service.web.bind.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

public class StartReviewParam {
    @Data
    public static class In {

        @NotBlank(message = "会议全称不能为空")
        private String conferenceName; // 会议全称

        @Range(min = 1, max = 2, message = "审稿分配策略只能是1或2")
        private Integer reviewStrategy; // 审稿分配策略

    }
}
