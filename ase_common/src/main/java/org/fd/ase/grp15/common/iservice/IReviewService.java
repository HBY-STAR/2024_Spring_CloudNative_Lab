package org.fd.ase.grp15.common.iservice;

import jakarta.validation.Valid;
import org.fd.ase.grp15.common.iservice.review.ReviewDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "http://ase-review-service:8085/interior/review")
public interface IReviewService {
    /**
     * 获取投稿评审信息
     *
     * @param contributeId 投稿Id
     * @return 评审结果
     */
    @GetExchange("/by-contribute-id/{contributeId}")
    public List<ReviewDTO> getContributionReview(@PathVariable Long contributeId);
}
