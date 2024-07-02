package org.fd.ase.grp15.ase_review_service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.validation.Valid;
import org.fd.ase.grp15.ase_review_service.cst.RevSrvCst;
import org.fd.ase.grp15.ase_review_service.exception.ReviewServiceException;
import org.fd.ase.grp15.ase_review_service.exception.code.ReviewServiceErrorCode;
import org.fd.ase.grp15.ase_review_service.web.bind.dto.EssayAssignedToMe;
import org.fd.ase.grp15.ase_review_service.web.bind.dto.ReviewRebuttalDTO;
import org.fd.ase.grp15.ase_review_service.entity.ReviewRebuttal;
import org.fd.ase.grp15.ase_review_service.repository.ReviewRebuttalRepository;
import org.fd.ase.grp15.ase_review_service.web.bind.param.SubmitRebuttalParam;
import org.fd.ase.grp15.ase_review_service.web.bind.param.SubmitRebuttalResultParam;

import org.fd.ase.grp15.common.iservice.IReviewService;

import org.fd.ase.grp15.ase_review_service.web.bind.param.StartReviewParam;
import org.fd.ase.grp15.ase_review_service.web.bind.param.SubmitReviewResultsParam;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.IConferenceService;
import org.fd.ase.grp15.common.iservice.IContributeService;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.fd.ase.grp15.common.iservice.contribute.AuthorInfo;
import org.fd.ase.grp15.common.iservice.contribute.ContributionDTO;
import org.fd.ase.grp15.common.iservice.user.dto.UserConferenceRoleDTO;
import org.fd.ase.grp15.common.iservice.review.ReviewDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private IContributeService contributeService;

    @Autowired
    private IConferenceService conferenceService;

    @Autowired
    private ReviewRebuttalRepository reviewRebuttalRepository;

    @Autowired
    private IUserConferenceRoleService userConferenceRoleService;

    @Autowired
    private ModelMapper modelMapper;

    public List<EssayAssignedToMe> essaysAssignedTo(String reviewerUsername) {
        List<ReviewRebuttal> reviewRebuttals = reviewRebuttalRepository.findAllByReviewerUsername(reviewerUsername);
        List<ReviewRebuttalDTO> reviewRebuttalDTOS = reviewRebuttals.stream().map(reviewRebuttal -> modelMapper.map(reviewRebuttal, ReviewRebuttalDTO.class)).toList();

        List<EssayAssignedToMe> essaysAssignedToMe = new ArrayList<>();
        for (var revRbt : reviewRebuttalDTOS) {
            ContributionDTO contrib = contributeService.byId(revRbt.getContributionId());
            EssayAssignedToMe eam = getEssayAssignedToMe(revRbt, contrib);
            essaysAssignedToMe.add(eam);
        }

        return essaysAssignedToMe;
    }

    private static EssayAssignedToMe getEssayAssignedToMe(ReviewRebuttalDTO revRbt, ContributionDTO contrib) {
        EssayAssignedToMe eam = new EssayAssignedToMe();
        eam.setContributionId(contrib.getId());
        eam.setUsername(contrib.getAuthor());
        eam.setRealName(contrib.getRealName());
        eam.setConferenceName(contrib.getConferenceName());
        eam.setTitle(contrib.getTitle());
        eam.setAbstractContent(contrib.getAbstractContent());
        eam.setEssayId(contrib.getEssayId());
        eam.setTopics(contrib.getTopics());
        eam.setReviewRebuttal(revRbt);
        return eam;
    }


    @Validated
    @Transactional
    public String submitReviewResult(@Valid SubmitReviewResultsParam.In in) {
        ContributionDTO contrib = contributeService.byId(in.getContributionId());
        if (contrib.getId() == -1) { // fallback
            throw new RuntimeException("ContributionService.byId rpc failed");
        }
        ConferenceDTO conf = conferenceService.getConferenceInfoByName(contrib.getConferenceName());
        if (conf != null && !"审稿中".equals(conf.getConferenceStatus())) { // fallback
            throw new RuntimeException("会议不在审稿中，无法提交评审结果");
        }

        var id = new ReviewRebuttal.ReviewRebuttalId(in.getContributionId(), in.getSeq());
        var revRbt = reviewRebuttalRepository.findById(id).orElse(null);

        if (revRbt == null) {
            throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_NOT_FOUND, "找不到对应的评审结果");
        }
        if (!Objects.equals(revRbt.getStatus(), RevSrvCst.REV_RBT_WAITING_FOR_REVIEW)) {
            String message = String.format("评审结果状态不匹配，期望状态为%s，实际状态为%s，请联系管理员", RevSrvCst.REV_RBT_WAITING_FOR_REVIEW, revRbt.getStatus());
            throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_STATUS_WRONG, message);
        }

        revRbt.setReviewComment(in.getReviewComment());
        revRbt.setReviewScore(in.getReviewScore());
        revRbt.setReviewConfidence(in.getReviewConfidence());

        // 更新评审结果状态。如果评审结果为“接受”或“弱接受”，则状态为“已完成”，不需要rebuttal了
        // 否则状态为“待rebuttal”
        Integer score = revRbt.getReviewScore();
        if (Objects.equals(score, RevSrvCst.SCORE_WEAK_ACCEPT) || Objects.equals(score, RevSrvCst.SCORE_ACCEPT)) {
            revRbt.setStatus(RevSrvCst.REV_RBT_DONE);
        } else {
            revRbt.setStatus(RevSrvCst.REV_RBT_WAITING_FOR_REBUTTAL);
        }

        reviewRebuttalRepository.save(revRbt);

        return "提交评审结果成功";
    }

    public String submitRebuttal(SubmitRebuttalParam.In param){
        ReviewRebuttal.ReviewRebuttalId revRId = new ReviewRebuttal.ReviewRebuttalId(param.getContributionId(),param.getSeq());
        var revRbt = reviewRebuttalRepository.findById(revRId).orElse(null);

        if (revRbt == null) {
            throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_NOT_FOUND, "找不到对应的评审结果");
        }
        if (!Objects.equals(revRbt.getStatus(), RevSrvCst.REV_RBT_WAITING_FOR_REBUTTAL)) {
            String message = String.format("评审结果状态不匹配，期望状态为%s，实际状态为%s，请联系管理员", RevSrvCst.REV_RBT_WAITING_FOR_REBUTTAL, revRbt.getStatus());
            throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_STATUS_WRONG, message);
        }
        revRbt.setRebuttalMessage(param.getRebuttalMessage());
        revRbt.setStatus(RevSrvCst.REV_RBT_REBUTTAL_SUBMITTED);
        reviewRebuttalRepository.save(revRbt);
        return "提交成功";
    }


    @GlobalTransactional
    public String publishFinalAcceptanceResults(String conferenceName) {
        List<ReviewRebuttal> reviewRebuttals = reviewRebuttalRepository.findAllByConferenceName(conferenceName);

        // 检查所有稿件的评审结果状态均为REV_RBT_DONE
        for(ReviewRebuttal reviewRebuttal : reviewRebuttals){
            List<String> errMessages = new ArrayList<>();
            if(reviewRebuttal.getStatus() != RevSrvCst.REV_RBT_DONE) {
                String message = getStatusErrorMessage(reviewRebuttal, RevSrvCst.REV_RBT_DONE);
                errMessages.add(message);
            }
            if (!errMessages.isEmpty()) {
                throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_STATUS_WRONG, String.join("\n", errMessages));
            }
        }

        // 判断是否录用，如果三个评审结果都为“接受”或“弱接受”，则录用，否则不录用
        Map<Long, List<ReviewRebuttal>> contribReviewRebuttalMap = new HashMap<>();
        for (var reviewRebuttal : reviewRebuttals) {
            Long contribId = reviewRebuttal.getId().getContributionId();
            contribReviewRebuttalMap.putIfAbsent(contribId, new ArrayList<>());
            contribReviewRebuttalMap.get(contribId).add(reviewRebuttal);
        }
        for (var contribId : contribReviewRebuttalMap.keySet()) {
            List<Integer> scores = contribReviewRebuttalMap
                    .get(contribId)
                    .stream()
                    .map(revRbt -> revRbt.getRebuttalScore() == null ? revRbt.getReviewScore() : revRbt.getRebuttalScore())
                    .toList();
            boolean accepted = scores.stream().allMatch(score -> score == RevSrvCst.SCORE_WEAK_ACCEPT || score == RevSrvCst.SCORE_ACCEPT);
            Integer status = accepted ? RevSrvCst.CONTRIB_STATUS_ACCEPTED : RevSrvCst.CONTRIB_STATUS_REJECTED;
            contributeService.changeContributionStatus(contribId, status);
        }

        conferenceService.changeConferenceStatus(conferenceName, RevSrvCst.CONFERENCE_DONE);
        return "发布录用结果成功";
    }


    private String getStatusErrorMessage(ReviewRebuttal reviewRebuttal, Integer expectedStatus) {
        return getStatusErrorMessage(reviewRebuttal, List.of(expectedStatus));
    }

    private String getStatusErrorMessage(ReviewRebuttal reviewRebuttal, List<Integer> expectedStatuses) {
        // status的代号(int)和中文描述(String)的映射
        Map<Integer, String> statusChnDescMap = new HashMap<>() {{
            put(RevSrvCst.REV_RBT_WAITING_FOR_REVIEW, "待审稿");
            put(RevSrvCst.REV_RBT_WAITING_FOR_REBUTTAL, "已审稿，待复议");
            put(RevSrvCst.REV_RBT_REBUTTAL_SUBMITTED, "已提交复议");
            put(RevSrvCst.REV_RBT_DONE, "完成");
        }};
        String template = "稿件(contributionId=%d, seq=%d)的评审结果状态不匹配，期望状态为%s，实际状态为%s。";
        String expStatus = expectedStatuses
                .stream()
                .map(statusChnDescMap::get)
                .map(s -> "【" + s + "】")
                .collect(Collectors.joining("或"));
        String actStatus = "【" + statusChnDescMap.get(reviewRebuttal.getStatus()) + "】";
        return String.format(template, reviewRebuttal.getId().getContributionId(), reviewRebuttal.getId().getSeq(), expStatus, actStatus);
    }

    public String submitRebuttalResult(SubmitRebuttalResultParam.In param){
        ReviewRebuttal.ReviewRebuttalId revRId = new ReviewRebuttal.ReviewRebuttalId(param.getContributionId(),param.getSeq());
        var revRbt = reviewRebuttalRepository.findById(revRId).orElse(null);

        if (revRbt == null) {
            throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_NOT_FOUND, "找不到对应的评审结果");
        }
            if (!Objects.equals(revRbt.getStatus(), RevSrvCst.REV_RBT_REBUTTAL_SUBMITTED)) {
                String message = String.format("评审结果状态不匹配，期望状态为%s，实际状态为%s，请联系管理员", RevSrvCst.REV_RBT_REBUTTAL_SUBMITTED, revRbt.getStatus());
                throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_STATUS_WRONG, message);
            }

            if(param.getRebuttalScore() != 0){
                revRbt.setRebuttalScore(param.getRebuttalScore());
        }

        revRbt.setStatus(RevSrvCst.REV_RBT_DONE);
        reviewRebuttalRepository.save(revRbt);
        return "提交评分成功";
    }

    @Override
    public List<ReviewDTO> getContributionReview(Long contributeId) {
        List<ReviewRebuttal> reviewRebuttals = reviewRebuttalRepository.findAllById_ContributionId(contributeId);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();

        for (ReviewRebuttal reviewRebuttal: reviewRebuttals){
            ReviewDTO reviewDTO = new ReviewDTO(reviewRebuttal.getId().getSeq(), reviewRebuttal.getStatus(),
                    reviewRebuttal.getReviewerUsername(), reviewRebuttal.getReviewScore(), reviewRebuttal.getReviewComment(), reviewRebuttal.getReviewConfidence(),
                    reviewRebuttal.getRebuttalMessage(), reviewRebuttal.getRebuttalScore());
            reviewDTOS.add(reviewDTO);
        }
        return reviewDTOS;
    }


    @GlobalTransactional
    public String publishReviewResults(String conferenceName) {
        // 检查会议的所有稿件评审状态不为REV_RBT_WAITING_FOR_REVIEW
        List<ReviewRebuttal> reviewRebuttals = reviewRebuttalRepository.findAllByConferenceName(conferenceName);
        for (ReviewRebuttal r: reviewRebuttals) {
            if (r.getStatus() == RevSrvCst.REV_RBT_WAITING_FOR_REVIEW) {
                String msg = getStatusErrorMessage(
                        r,
                        List.of(RevSrvCst.REV_RBT_DONE, RevSrvCst.REV_RBT_WAITING_FOR_REBUTTAL)
                );
                throw new ReviewServiceException(ReviewServiceErrorCode.REV_RBT_STATUS_WRONG, msg);
            }
        }
        // 会议状态改为复议中
        conferenceService.changeConferenceStatus(conferenceName, RevSrvCst.CONFERENCE_REBUTTING);
        //更新所有稿件的状态（三个状态全为REV_RBT_DONE则稿件状态改为已录用，否则改为复议中）
        HashMap<Long, int[]> contributionMap = new HashMap<>();
        for (ReviewRebuttal r: reviewRebuttals) {
            if (contributionMap.containsKey(r.getId().getContributionId())){
                int[] statusValue = contributionMap.get(r.getId().getContributionId());
                statusValue[r.getId().getSeq()-1] = r.getStatus();
                contributionMap.put(r.getId().getContributionId(), statusValue);
                if (statusValue[0] == RevSrvCst.REV_RBT_DONE && statusValue[1] == RevSrvCst.REV_RBT_DONE && statusValue[2] == RevSrvCst.REV_RBT_DONE){
                    contributeService.changeContributionStatus(r.getId().getContributionId(), RevSrvCst.CONTRIB_STATUS_ACCEPTED);
                }else if (statusValue[0] != -1 && statusValue[1] != -1 && statusValue[2] != -1){
                    contributeService.changeContributionStatus(r.getId().getContributionId(), RevSrvCst.CONTRIB_STATUS_UNDER_REBUTTAL);
                }
            } else {
                int[] statusValue = new int[]{-1,-1,-1};
                statusValue[r.getId().getSeq()-1] = r.getStatus();
                contributionMap.put(r.getId().getContributionId(), statusValue);
            }
        }
        return "发布评审结果成功";
    }

    public String startReview(@Valid StartReviewParam.In in) {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`
        // 检查是否是Chair
        String conferenceName = in.getConferenceName();
        if(!userConferenceRoleService.checkRoleOfUserInConference(userName, conferenceName, ConferenceRole.CHAIR)) {
            throw new ReviewServiceException(ReviewServiceErrorCode.ERR_NOT_CHAIR, "只有Chair才能开启审稿");
        }
        // 检查会议状态是否是投稿中
        ConferenceDTO curConference = conferenceService.getConferenceInfoByName(in.getConferenceName());
        if (!curConference.getConferenceStatus().equals("投稿中")) {
            throw new ReviewServiceException(ReviewServiceErrorCode.ERR_ILLEGAL_CONFERENCE_STATUS, "开启审稿需要会议状态为“投稿中”");
        }
        //  检查是否有至少三个PC MEMBER
        //  需要一个函数，输入conferenceName（和topic），输出会议中（对应topic）的所有PC MEMBER，返回类型List<string>。
        List<UserConferenceRoleDTO> allPC_MEMBER = userConferenceRoleService.loadPC_MEMBERInConference(in.getConferenceName());
        if(allPC_MEMBER.size() < 3) {
            throw new ReviewServiceException(ReviewServiceErrorCode.ERR_TOO_FEW_PC_MEMBERS, "开启审稿需要至少有三名PC MEMBER");
        }

        List<ContributionDTO> contributions = contributeService.findAllByConferenceName(in.getConferenceName());
        /*
         * 审稿也需要有一个表来存储审稿信息，
         */
        List<ReviewRebuttal> results = new ArrayList<ReviewRebuttal>();

        if(in.getReviewStrategy() == 1) {
            // 基于topic相关度的稿件分配策略
            Map<String, PriorityQueue<Map.Entry<String, Integer>>> topicHash = new HashMap<>();

            // 为每个topic下审稿人分得的稿件数建一个最小堆
            for (UserConferenceRoleDTO PC_MEMBER : allPC_MEMBER) {
                for (String topic : PC_MEMBER.getResponsibleTopics()) {
                    if (topicHash.containsKey(topic)) {
                        // 题目只要求每个topic下的分配平衡，因此这里存储的是每个topic的审稿人分得的稿件数
                        topicHash.get(topic).add(new AbstractMap.SimpleEntry<>(PC_MEMBER.getUsername(), 0));
                    }
                    else {
                        PriorityQueue<Map.Entry<String, Integer>> temp = new PriorityQueue<>(
                                Comparator.comparingInt(Map.Entry::getValue)
                        );
                        temp.add(new AbstractMap.SimpleEntry<>(PC_MEMBER.getUsername(), 0));
                        topicHash.put(topic, temp);
                    }
                }
            }

            for (ContributionDTO contribution : contributions) {
                String author = contribution.getAuthor();
                // 将AuthorINFO中的name提取出来作为一个List，方便后续查找
                List<String> writers = contribution.getWriters().stream()
                        .map(AuthorInfo::getName)
                        .toList();
                // 记录已分配的审稿人，防止不同的topic分配了同一个审稿人
                List<String> assignedPC_MEMBER = new ArrayList<>();
                int assigned = 0;
                for (String topic : contribution.getTopics()) {
                    if (assigned >= 3) {
                        // 已完成三个审稿人的分配
                        break;
                    }
                    if (!topicHash.containsKey(topic)) {
                        continue;
                    }
                    boolean isAuthor = false;
                    Map<String, Integer> assignment = new HashMap<>();
                    PriorityQueue<Map.Entry<String, Integer>> minHeap = topicHash.get(topic);
                    while (!minHeap.isEmpty()) {
                        if (assigned >= 3) {
                            // 已完成三个审稿人的分配
                            break;
                        }
                        isAuthor = false;
                        Map.Entry<String, Integer> curPC_MEMBER = minHeap.poll();
                        // 条件写成一行有点丑
                        if (curPC_MEMBER.getKey().equals(author)) {
                            isAuthor = true;
                        } else if (writers.contains(curPC_MEMBER.getKey())) {
                            isAuthor = true;
                        } else if (assignedPC_MEMBER.contains(curPC_MEMBER.getKey())) {
                            isAuthor = true;
                        }

                        if (isAuthor) {
                            // 审稿人在作者或投稿人中，跳过该投稿人
                            assignment.put(curPC_MEMBER.getKey(), curPC_MEMBER.getValue());
                        }
                        else {
                            // 审稿人可以审稿，该审稿人审稿数+1
                            assignment.put(curPC_MEMBER.getKey(), curPC_MEMBER.getValue() + 1);
                            assignedPC_MEMBER.add(curPC_MEMBER.getKey());
                            assigned += 1;
                            ReviewRebuttal.ReviewRebuttalId id = new ReviewRebuttal.ReviewRebuttalId(
                                    contribution.getId(), assigned
                            );
                            ReviewRebuttal reviewAssignment = new ReviewRebuttal(
                                    id, 0, curConference.getConferenceName(), curPC_MEMBER.getKey()
                            );
                            results.add(reviewAssignment);
                        }
                    }
                    // 对一篇论文每个topic分配一个审稿人
                    minHeap.addAll(assignment.entrySet());
                }
                if (assigned != 3) {
                    String errorMessage = String.format("对于论文“%s”, 不存在至少三名符合要求的PC_MEMBER", contribution.getTitle());
                    throw new ReviewServiceException(ReviewServiceErrorCode.ERR_TOO_FEW_PC_MEMBERS, errorMessage);
                }
            }
        }
        else {
            // 基于审稿平均负担的审稿分配策略

            PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(
                    Comparator.comparingInt(Map.Entry::getValue)
            );
            for (UserConferenceRoleDTO PC_MEMBER : allPC_MEMBER) {
                minHeap.add(new AbstractMap.SimpleEntry<>(PC_MEMBER.getUsername(), 0));
            }

            for (ContributionDTO contribution : contributions) {
                Map<String, Integer> assignment = new HashMap<>();
                String author = contribution.getAuthor();
                // 将AuthorINFO中的name提取出来作为一个List，方便后续查找
                List<String> writers = contribution.getWriters().stream()
                        .map(AuthorInfo::getName)
                        .toList();
                int assigned = 0;

                boolean isAuthor = false;
                while (!minHeap.isEmpty()) {
                    if (assigned >= 3) {
                        // 已找到3个审稿人
                        break;
                    }
                    isAuthor = false;
                    Map.Entry<String, Integer> curPC_MEMBER = minHeap.poll();
                    if (curPC_MEMBER.getKey().equals(author)) {
                        isAuthor = true;
                    } else if (writers.contains(curPC_MEMBER.getKey())) {
                        isAuthor = true;
                    }

                    if (isAuthor) {
                        // 审稿人在作者或投稿人中，跳过该投稿人
                        assignment.put(curPC_MEMBER.getKey(), curPC_MEMBER.getValue());
                    }
                    else {
                        // 审稿人可以审稿，该审稿人审稿数+1
                        assignment.put(curPC_MEMBER.getKey(), curPC_MEMBER.getValue() + 1);
                        assigned += 1;
                        ReviewRebuttal.ReviewRebuttalId id = new ReviewRebuttal.ReviewRebuttalId(
                                contribution.getId(), assigned
                        );
                        ReviewRebuttal reviewAssignment = new ReviewRebuttal(
                                id, 0, curConference.getConferenceName(), curPC_MEMBER.getKey()
                        );
                        results.add(reviewAssignment);
                    }
                }
                if(assigned != 3) {
                    String errorMessage = String.format("对于论文“%s”, 不存在至少三名符合要求的PC_MEMBER", contribution.getTitle());
                    throw new ReviewServiceException(ReviewServiceErrorCode.ERR_TOO_FEW_PC_MEMBERS, errorMessage);
                }

                // 在一篇论文的三个审稿人分配完成后，将已分配的审稿人和跳过的审稿人重新添加回最小堆中
                minHeap.addAll(assignment.entrySet());
            }
        }

        // 确定分配不会出错后，再保存到数据库中
        reviewRebuttalRepository.saveAll(results);
        conferenceService.changeConferenceStatus(in.getConferenceName(), "审稿中");
        return "开启审稿成功";
    }
}
