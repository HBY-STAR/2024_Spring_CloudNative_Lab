package org.fd.ase.grp15.ase_contribute_service.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.fd.ase.grp15.ase_contribute_service.converter.AuthorInfoListConverter;
import org.fd.ase.grp15.ase_contribute_service.converter.StringListConverter;

import java.util.List;

@Entity
@Table(name = "contribution")
@Getter
@Setter
public class Contribution{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conference_name")
    private String conferenceName;

    @Column(name = "author")
    private String author;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "writers", length = 4096)
    @Convert(converter = AuthorInfoListConverter.class)
    private List<AuthorInfo> writers;

    @Column(name = "title")
    private String title;

    @Column(name = "abstract_content", length = 4096)
    private String abstractContent;

    @Column(name = "essay_id")
    private String essayId;

    @Column(name = "status")
    private int status;

    @Column(name = "contribute_time")
    private String contributeTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "topics", length = 4096)
    @Convert(converter = StringListConverter.class)
    private List<String> topics;

    public Contribution(String author, String realName,List<AuthorInfo> writers,List<String> topics, String conferenceName, String title, String abstractContent, String essayId, String contributeTime){
        this.author = author;
        this.realName = realName;
        this.conferenceName = conferenceName;
        this.title = title;
        this.abstractContent = abstractContent;
        this.essayId = essayId;
        this.contributeTime = contributeTime;
        this.status = ContributeStatus.WAITCHECK.getCode();
        this.writers = writers;
        this.topics = topics;
    }

    public Contribution() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
