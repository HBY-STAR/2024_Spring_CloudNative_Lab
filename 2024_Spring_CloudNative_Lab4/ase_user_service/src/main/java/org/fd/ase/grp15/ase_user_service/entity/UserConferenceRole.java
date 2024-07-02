package org.fd.ase.grp15.ase_user_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Table(name = "user_conference_role")
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserConferenceRole {

    @Embeddable
    @Data
    @RequiredArgsConstructor
    @NoArgsConstructor
    public static class UserConferenceRoleId implements Serializable {
        @NonNull
        private String username;
        @NonNull
        private String conferenceName;
        @NonNull
        private String role;
    }

    @EmbeddedId
    @NonNull
    private UserConferenceRoleId id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_conference_role_responsible_topic")
    private List<ResponsibleTopic> responsibleTopics;
}
