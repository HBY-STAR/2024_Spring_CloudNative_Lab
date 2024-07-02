package org.fd.ase.grp15.ase_conference_service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import org.checkerframework.checker.units.qual.A;
import org.fd.ase.grp15.ase_conference_service.entity.Conference;
import org.fd.ase.grp15.ase_conference_service.entity.ConferenceApplication;
import org.fd.ase.grp15.ase_conference_service.repository.ConferenceApplicationRepository;
import org.fd.ase.grp15.ase_conference_service.exception.ConferenceServiceException;
import org.fd.ase.grp15.ase_conference_service.exception.code.ConferenceServiceErrorCode;
import org.fd.ase.grp15.ase_conference_service.repository.ConferenceRepository;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.IConferenceService;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceApplicationDTO;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.fd.ase.grp15.common.iservice.conference.param.ApplyConferenceParam;
import org.fd.ase.grp15.common.iservice.conference.param.AuditConferenceParam;
import org.fd.ase.grp15.common.iservice.conference.param.StartSubmissionParam;
import org.fd.ase.grp15.common.iservice.user.dto.UserConferenceRoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class ConferenceServiceImpl implements IConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceApplicationRepository conferenceApplicationRepository;


    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    @Autowired
    private IUserConferenceRoleService userConferenceRoleService;

    @Autowired
    private ModelMapper modelMapper;

    private final List<String> STATUS = Arrays.asList("准备中", "投稿中", "审稿中", "复议中", "完成");

    /**
     * 用户注册
     *
     * @param in 注册参数
     * @return 注册结果
     */
    @Validated
    public String apply(@Valid ApplyConferenceParam.In in) {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`

        var conferenceStartAt = LocalDateTime.parse(in.getConferenceStartAt(), FORMATTER);
        var conferenceEndAt = LocalDateTime.parse(in.getConferenceEndAt(), FORMATTER);
        // 检查开始时间是否晚于结束时间
        if (conferenceStartAt.isAfter(conferenceEndAt)) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_START_TIME_AFTER_END_TIME, "会议开始时间晚于会议终止时间");
        }
        // 检查开始时间是否是未来的日期
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(conferenceStartAt)) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_NOT_FUTURE_DATE, "会议开始日期应是未来的日期");
        }

        // 检查会议全称是否和已有会议重复
        if (conferenceRepository.findConferenceByConferenceName(in.getConferenceName()) != null) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_CONFERENCE_NAME_EXISTED, "会议名称已存在");
        }

        // 检查是否已有同名的会议在审核中
        List<ConferenceApplication> sameNameApplication = conferenceApplicationRepository.findAllByConferenceName(in.getConferenceName());
        for (ConferenceApplication curConferenceApplication : sameNameApplication) {
            if (curConferenceApplication.getApplicationStatus().equals("审核中")) {
                throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_SAME_NAME_CONFERENCE_APPLYING, "同名的会议已经在审核中");
            }
        }

        // 映射参数
        ConferenceApplication conferenceApplication = modelMapper.map(in, ConferenceApplication.class);

        conferenceApplication.setConferenceStartAt(conferenceStartAt);
        conferenceApplication.setConferenceEndAt(conferenceEndAt);
        conferenceApplication.setApplicationStatus("审核中");
        conferenceApplication.setApplicantUsername(userName);

        // 保存会议
        conferenceApplicationRepository.save(conferenceApplication);

        return "申请成功";
    }

    /**
     * 获取所有会议信息
     *
     * @return 包含所有会议的列表
     */
    @Validated
    public List<ConferenceDTO> getAllConferences() {
        return conferenceRepository
                .findAll()
                .stream()
                .map(conference -> modelMapper.map(conference, ConferenceDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 查看我申请的所有会议
     *
     * @return 包含我的所有申请的会议的列表
     */
    @Validated
    public List<ConferenceApplicationDTO> getAllMyAppliedConference() {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`
        return conferenceApplicationRepository
                .findAllByApplicantUsername(userName)
                .stream()
                .map(conferenceApplication -> modelMapper.map(conferenceApplication, ConferenceApplicationDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 查看我参与的所有会议
     *
     * @return 包含我的所有参加的会议的列表
     */
    @Validated
    public List<ConferenceDTO> getAllMyJoinedConference() {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`

        // TODO 小BUG，如果一个人是主席又是审稿人，那么他会在userConferenceRole中出现两次，导致返回的Conference列表包含两个一样的会议
        List<UserConferenceRoleDTO> userConferenceRoles = userConferenceRoleService.loadUserRolesInConference(userName);
        List<ConferenceDTO> myJoinedConferences = new ArrayList<>();

        List<ConferenceDTO> finalMyJoinedConferences = myJoinedConferences;
        userConferenceRoles.forEach(userConferenceRoleDTO -> {
            var cfr = conferenceRepository.findConferenceByConferenceName(userConferenceRoleDTO.getConferenceName());
            if (cfr != null) {
                var cfrDTO = modelMapper.map(cfr, ConferenceDTO.class);
                finalMyJoinedConferences.add(cfrDTO);
            }
        });

        // 去重
        myJoinedConferences = myJoinedConferences.stream().distinct().collect(Collectors.toList());

        return myJoinedConferences;
    }

    /**
     * 查看我在某个会议中的角色
     *
     * @param conferenceName 会议全称
     * @return 我在该会议中的角色的列表，如["AUTHOR", "PC_MEMBER"]
     */
    @Validated
    public List<String> getMyRoleInConference(@Valid String conferenceName) {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`

        Conference conference = conferenceRepository.findConferenceByConferenceName(conferenceName);
        if (conference == null) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_CONFERENCE_NAME_NOT_EXISTED, "会议不存在");
        }

        List<ConferenceRole> conferenceRoles = userConferenceRoleService.loadUserRolesInConference(userName, conferenceName);
        List<String> myRoles = new ArrayList<>();
        for (ConferenceRole conferenceRole : conferenceRoles) {
            myRoles.add(conferenceRole.toString());
        }

        return myRoles;
    }

    /**
     * 查看某个会议中的详细信息
     *
     * @param conferenceName 会议全称
     * @return 会议详细信息
     */
    @Override
    public ConferenceDTO getConferenceInfoByName(String conferenceName) {
        StpUtil.checkLogin();
        Conference conference = conferenceRepository.findConferenceByConferenceName(conferenceName);
        if (conference == null) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_CONFERENCE_NAME_NOT_EXISTED, "会议不存在");
        }

        return modelMapper.map(conference, ConferenceDTO.class);
    }

    @Override
    public String changeConferenceStatus(String conferenceName, String status) {
        if (!STATUS.contains(status)) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_ILLEGAL_CONFERENCE_STATUS, "会议状态非法！会议状态只能是[\"准备中\", \"投稿中\", \"审稿中\", \"复议中\", \"完成\"]的一种！");
        }

        Conference conference = conferenceRepository.findConferenceByConferenceName(conferenceName);
        if (conference == null) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_CONFERENCE_NAME_NOT_EXISTED, "会议不存在");
        }

        conference.setConferenceStatus(status);
        conferenceRepository.save(conference);
        return "修改状态成功";
    }

    /**
     * 开启投稿
     *
     * @param in 开启投稿参数
     * @return 开启结果
     */
    @Validated
    public String startSubmission(@Valid StartSubmissionParam.In in) {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`

        if (!userConferenceRoleService.checkRoleOfUserInConference(userName, in.getConferenceName(), ConferenceRole.CHAIR)) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_NOT_CHAIR, "只有CHAIR才能开启投稿");
        }

        Conference curConference = conferenceRepository.findConferenceByConferenceName(in.getConferenceName());
        if (curConference == null) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_CONFERENCE_NAME_NOT_EXISTED, "会议不存在");
        }

        var submissionDeadline = LocalDateTime.parse(in.getSubmissionDeadline(), FORMATTER);
        var reviewResultAnnounceAt = LocalDateTime.parse(in.getReviewResultAnnounceAt(), FORMATTER);
        // 检查会议投稿截止日期是否晚于会议评审结果发布日期
        if (submissionDeadline.isAfter(reviewResultAnnounceAt)) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_SUBMISSION_TIME_AFTER_RESULT_TIME, "会议投稿截止日期晚于会议结果评审发布日期时间");
        }
        // 检查会议投稿截止日期是否是未来的日期
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(submissionDeadline)) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_NOT_FUTURE_DATE, "会议投稿截止日期应是未来的日期");
        }
        var conferenceStartAt = curConference.getConferenceStartAt();
        // 检查会议评审结果发布日期是否晚于会议开始日期
        if (reviewResultAnnounceAt.isAfter(conferenceStartAt)) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_RESULT_TIME_AFTER_START_TIME, "会议结果评审发布日期晚于会议开始日期");
        }
        // 检查会议状态是否是“准备中”
        if (!curConference.getConferenceStatus().equals("准备中")) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_ILLEGAL_CONFERENCE_STATUS, "开启投稿需要会议状态为“准备中”");
        }
        curConference.setSubmissionDeadline(submissionDeadline);
        curConference.setReviewResultAnnounceAt(reviewResultAnnounceAt);
        conferenceRepository.save(curConference);
        String msg = changeConferenceStatus(in.getConferenceName(), "投稿中");
        return "开始投稿成功";
    }

    /**
     * 管理员查看所有会议申请
     *
     * @return 包含所有会议申请的列表
     */
    @Validated
    public List<ConferenceApplicationDTO> getAllConferenceApplications() {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`

        // 判断是否是管理员
        if (!userName.equals("admin")) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_NOT_ADMINISTRATOR, "权限不足，请联系管理员！");
        }
        return conferenceApplicationRepository
                .findAll()
                .stream()
                .map(conferenceApplication -> modelMapper.map(conferenceApplication, ConferenceApplicationDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 管理员审核会议
     *
     * @param in 审核参数
     * @return 审核结果
     */
    @Validated
    public String auditConference(@Valid AuditConferenceParam.In in) {
        String userName = StpUtil.getLoginIdAsString(); // 获取当前会话账号用户名, 如果未登录，则抛出异常：`NotLoginException`

        // 判断是否是管理员
        if (!userName.equals("admin")) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_NOT_ADMINISTRATOR, "权限不足，请联系管理员！");
        }

        ConferenceApplication conferenceApplication = conferenceApplicationRepository.findConferenceApplicationByApplicationID(in.getApplicationId());
        if (!conferenceApplication.getApplicationStatus().equals("审核中")) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_APPLICATION_STATUS, "申请状态不为'审核中'");
        }

        if (in.getApplicationPass()) {      // 审核通过
            String applicantUsername = conferenceApplication.getApplicantUsername();
            userConferenceRoleService.addRoleToUserInConference(applicantUsername, conferenceApplication.getConferenceName(), ConferenceRole.CHAIR);
            List<String > topics = conferenceApplication.getTopics();
            userConferenceRoleService.addRoleToUserInConference(applicantUsername, conferenceApplication.getConferenceName(), ConferenceRole.PC_MEMBER, topics);

            conferenceApplication.setApplicationStatus("已通过");
            conferenceApplicationRepository.save(conferenceApplication);

            // 映射参数
            Conference conference = modelMapper.map(conferenceApplication, Conference.class);
            conference.setConferenceStatus("准备中");
            conferenceRepository.save(conference);

            return "审核通过成功";
        } else {
            conferenceApplication.setApplicationStatus("未通过");
            conferenceApplicationRepository.save(conferenceApplication);
            return "审核拒绝成功";
        }
    }

    /**
     * 获取指定会议的名称
     * @param conferenceName 会议全称
     * @return 置顶会议的名称
     */
    public List<String> getAllTopics(String conferenceName) {
        Conference conference = conferenceRepository.findConferenceByConferenceName(conferenceName);
        if (conference == null) {
            throw new ConferenceServiceException(ConferenceServiceErrorCode.ERR_CONFERENCE_NAME_NOT_EXISTED, "会议不存在");
        }
        return conference.getTopics();
    }

}
