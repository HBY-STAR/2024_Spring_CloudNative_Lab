package org.fd.ase.grp15.ase_review_service.service.mock;

import org.fd.ase.grp15.common.iservice.IConferenceService;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceApplicationDTO;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.fd.ase.grp15.common.iservice.conference.param.ApplyConferenceParam;
import org.fd.ase.grp15.common.iservice.conference.param.AuditConferenceParam;
import org.fd.ase.grp15.common.iservice.conference.param.StartSubmissionParam;

import java.util.List;

public class ConferenceServiceMock implements IConferenceService {
    public String apply(ApplyConferenceParam.In in) {
        throw new RuntimeException("ConferenceServiceMock.apply() rpc call failed");
    }

    public List<ConferenceDTO> getAllConferences() {
        throw new RuntimeException("ConferenceServiceMock.getAllConferences() rpc call failed");
    }

    public List<ConferenceApplicationDTO> getAllMyAppliedConference() {
        throw new RuntimeException("ConferenceServiceMock.getAllMyAppliedConference() rpc call failed");
    }

    public List<ConferenceDTO> getAllMyJoinedConference() {
        throw new RuntimeException("ConferenceServiceMock.getAllMyJoinedConference() rpc call failed");
    }

    public List<String> getMyRoleInConference(String conferenceName) {
        throw new RuntimeException("ConferenceServiceMock.getMyRoleInConference() rpc call failed");
    }

    @Override
    public ConferenceDTO getConferenceInfoByName(String conferenceName) {
        throw new RuntimeException("ConferenceServiceMock.getConferenceInfoByName() rpc call failed");
    }

    @Override
    public String changeConferenceStatus(String conferenceName, String status) {
        throw new RuntimeException("ConferenceServiceMock.changeConferenceStatus() rpc call failed");
    }

    public String startSubmission(StartSubmissionParam.In in) {
        throw new RuntimeException("ConferenceServiceMock.startSubmission() rpc call failed");
    }

    public List<ConferenceApplicationDTO> getAllConferenceApplications() {
        throw new RuntimeException("ConferenceServiceMock.getAllConferenceApplications() rpc call failed");
    }

    public String auditConference(AuditConferenceParam.In in) {
        throw new RuntimeException("ConferenceServiceMock.auditConference() rpc call failed");
    }

    public List<String> getAllTopics(String conferenceName) {
        throw new RuntimeException("ConferenceServiceMock.getAllTopics() rpc call failed");
    }
}
