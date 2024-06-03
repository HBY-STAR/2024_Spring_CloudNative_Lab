package org.fd.ase.grp15.ase_conference_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conference")
@Getter
@Setter
public class Conference extends AuditModel {
    @Id
    private String conferenceName; // 会议全称，主键

    @Column(name = "conferenceAbbr")
    private String conferenceAbbr; // 会议简称

    @Column(name = "conference_StartAt")
    private LocalDateTime conferenceStartAt;    // 会议举办时间-左端点

    @Column(name = "conference_EndAt")
    private LocalDateTime conferenceEndAt;      // 会议举办时间-右端点

    @Column(name = "venue")
    private String venue;          // 会议举办地点

    @Column(name = "submissionDeadline")
    private LocalDateTime submissionDeadline;   // 会议投稿截止日

    @Column(name = "reviewResult_AnnounceAt")
    private LocalDateTime reviewResultAnnounceAt;      // 会议评审结果发布日期

    @Column(name = "conferenceStatus")
    private String conferenceStatus;          // 会议状态(准备中、投稿中、审稿中、复议中、完成)

    @Column(name = "topics", length = 4096)
    private List<String> topics;              // 会议topic（不少于1个）

}
