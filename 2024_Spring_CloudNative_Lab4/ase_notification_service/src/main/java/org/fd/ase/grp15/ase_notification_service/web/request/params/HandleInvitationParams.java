package org.fd.ase.grp15.ase_notification_service.web.request.params;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HandleInvitationParams {

    private String username;
    private String notificationId;
    private boolean accept;
    private List<String> responsibleTopics;

}
