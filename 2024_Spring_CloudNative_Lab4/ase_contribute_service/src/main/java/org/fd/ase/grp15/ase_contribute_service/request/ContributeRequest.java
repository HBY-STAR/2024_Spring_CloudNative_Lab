package org.fd.ase.grp15.ase_contribute_service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.fd.ase.grp15.ase_contribute_service.entity.AuthorInfo;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class ContributeRequest {

    @Data
    public static class In{
        @NotBlank(message = "会议名称不能为空")
        private String conferenceName;

        @NotBlank(message = "摘要内容不能为空")
        @Length(max = 800, message = "摘要不能超过800个字符")
        private String abstractContent;

        @NotBlank(message = "投稿人不能为空")
        private String username;

        private String realName;

        @NotNull(message = "至少应有一个作者")
        @NotEmpty(message = "至少应有一个作者")
        private List<AuthorInfo> writers;

        @NotBlank(message = "论文标题不能为空")
        @Length(max = 50, message = "论文标题不能超过50个字符")
        private String title;

        private String essayId;

        @NotEmpty(message = "至少应有一个topic")
        @NotNull(message = "至少应有一个topic")
        private List<String> topics;
    }

    @Data
    public static class UpdateContribute {
        private String id;

        @NotBlank(message = "会议名称不能为空")
        private String conferenceName;

        @NotBlank(message = "摘要内容不能为空")
        @Length(max = 800, message = "摘要不能超过800个字符")
        private String abstractContent;

        @NotBlank(message = "投稿人不能为空")
        private String username;

        private String realName;

        @NotNull(message = "至少应有一个作者")
        @NotEmpty(message = "至少应有一个作者")
        private List<AuthorInfo> writers;

        @NotBlank(message = "论文标题不能为空")
        @Length(max = 50, message = "论文标题不能超过50个字符")
        private String title;

        private String essayId;

        @NotEmpty(message = "至少应有一个topic")
        @NotNull(message = "至少应有一个topic")
        private List<String> topics;
    }
}
