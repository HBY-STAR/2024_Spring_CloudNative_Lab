package org.fd.ase.grp15.ase_review_service.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.fd.ase.grp15.ase_review_service.service.impl.ReviewServiceImpl;
import org.fd.ase.grp15.ase_review_service.web.bind.dto.EssayAssignedToMe;
import org.fd.ase.grp15.ase_review_service.web.bind.param.StartReviewParam;
import org.fd.ase.grp15.ase_review_service.web.bind.param.SubmitReviewResultsParam;
import org.fd.ase.grp15.ase_review_service.web.bind.param.*;
import org.fd.ase.grp15.ase_review_service.web.bind.response.NoDataSuccessfulResponse;
import org.fd.ase.grp15.ase_review_service.web.bind.response.WithDataSuccessfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewRebuttalController {

    @Autowired
    private ReviewServiceImpl reviewService;

    @RequestMapping("/essaysAssignedToMe")
    private WithDataSuccessfulResponse<List<EssayAssignedToMe>> essaysAssignedToMe() {
        String username = StpUtil.getLoginIdAsString();
        List<EssayAssignedToMe> essaysAssignedToMe = reviewService.essaysAssignedTo(username);
        String message = String.format("共有%d篇文章待审", essaysAssignedToMe.size());
        return new WithDataSuccessfulResponse<>(message, essaysAssignedToMe);
    }

    @RequestMapping("/submitReviewResult")
    public NoDataSuccessfulResponse submitReviewResult(@Validated @RequestBody SubmitReviewResultsParam.In in) {
        String result = reviewService.submitReviewResult(in);
        return new NoDataSuccessfulResponse(result);
    }

    @RequestMapping("/submitRebuttal")
    public NoDataSuccessfulResponse submitRebuttal(@Validated @RequestBody SubmitRebuttalParam.In in) {
        String result = reviewService.submitRebuttal(in);
        return new NoDataSuccessfulResponse(result);
    }

    @RequestMapping("/publishFinalAcceptanceResults")
    public NoDataSuccessfulResponse publishFinalAcceptanceResults(@Validated @RequestBody PublishFinalAcceptanceResultsParam.In in) {
        var conferenceName = in.getConferenceName();
        String result = reviewService.publishFinalAcceptanceResults(conferenceName);
        return new NoDataSuccessfulResponse(result);
    }

    @RequestMapping("/submitRebuttalResult")
    public NoDataSuccessfulResponse submitRebuttalResult(@Validated @RequestBody SubmitRebuttalResultParam.In in) {
        String result = reviewService.submitRebuttalResult(in);
        return new NoDataSuccessfulResponse(result);
    }

    //发布评审结果
    @RequestMapping("/publishReviewResults")
    public NoDataSuccessfulResponse publishReviewResults(@Validated @RequestBody PublishReviewResultsParam.In in) {
        var conferenceName = in.getConferenceName();
        String result = reviewService.publishReviewResults(conferenceName);
        return new NoDataSuccessfulResponse(result);
    }

    @RequestMapping("/startReview")
    public NoDataSuccessfulResponse startReview(@Validated @RequestBody StartReviewParam.In in) {
        String result = reviewService.startReview(in);
        return new NoDataSuccessfulResponse(result);
    }
}
