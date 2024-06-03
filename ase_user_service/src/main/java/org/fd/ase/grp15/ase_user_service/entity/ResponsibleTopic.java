package org.fd.ase.grp15.ase_user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "responsible_topic")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ResponsibleTopic {

    @Id
    @NonNull
    private String name;

    @ManyToMany(mappedBy = "responsibleTopics")
    @JsonIgnore
    @ToString.Exclude
    private List<UserConferenceRole> userConferenceRoles;
}
