package org.fd.ase.grp15.ase_review_service.controller.interior;

import org.checkerframework.checker.units.qual.A;
import org.fd.ase.grp15.ase_review_service.service.impl.ReviewServiceImpl;
import org.fd.ase.grp15.common.iservice.IReviewService;
import org.fd.ase.grp15.common.iservice.review.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/interior/review")
public class InteriorReviewRebuttalController implements IReviewService {

    @Autowired
    private ReviewServiceImpl reviewService;

    @Override
    @GetMapping("/by-contribute-id/{contributeId}")
    public List<ReviewDTO> getContributionReview(@PathVariable Long contributeId) {
        return reviewService.getContributionReview(contributeId);
    }
}
