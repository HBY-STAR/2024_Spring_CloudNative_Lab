package org.fd.ase.grp15.common.iservice;

import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.user.dto.UserConferenceRoleDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;


@HttpExchange(url = "http://ase-user-service:8081/interior/user-conference-role")
public interface IUserConferenceRoleService {

    /**
     * 加载用户在某个会议中的全部角色（CHAIR，PC_MEMBER，AUTHOR）
     *
     * @param username       用户名
     * @param conferenceName 会议名
     * @return 角色列表
     */
    @GetExchange(url = "/user-roles-in-conference")
    public List<ConferenceRole> loadUserRolesInConference(@RequestParam String username, @RequestParam String conferenceName);


    /**
     * 加载用户在某个会议中的全部角色（CHAIR，PC_MEMBER，AUTHOR）,返回DTO(包含负责的主题)
     *
     * @param username       用户名
     * @param conferenceName 会议名
     * @return 角色列表
     */
    @GetExchange(url = "/user-roles-in-conference2")
    List<UserConferenceRoleDTO> loadUserRolesInConference2(@RequestParam String username, @RequestParam String conferenceName);

    /**
     * 加载某个会议中的全部PC MEMBER,返回DTO(包含负责的主题)
     *
     * @param conferenceName 会议名
     * @return 角色列表
     */
    @GetExchange(url = "/pc-member-in-conference")
    List<UserConferenceRoleDTO> loadPC_MEMBERInConference(@RequestParam String conferenceName);


    /**
     * 加载用户在他所参加的会议中的全部角色（CHAIR，PC_MEMBER，AUTHOR）
     *
     * @param username 用户名
     * @return 角色列表
     */
    @GetExchange(url = "/user-roles-in-all-conferences")
    public List<UserConferenceRoleDTO> loadUserRolesInConference(@RequestParam String username);

    /**
     * 为用户添加会议角色
     *
     * @param username       用户名
     * @param conferenceName 会议名
     * @param role           角色
     * @return 添加结果
     */
    @PostExchange(url = "/add-role-to-user-in-conference/{username}/{conferenceName}/{role}")
    public String addRoleToUserInConference(@PathVariable String username, @PathVariable String conferenceName, @PathVariable ConferenceRole role);


    /**
     * 为用户添加会议角色,并指定负责的主题
     *
     * @param username          用户名
     * @param conferenceName    会议名
     * @param role              角色
     * @param responsibleTopics 负责的主题
     * @return 添加结果
     */
    @PostExchange(url = "/add-role-to-user-in-conference2/{username}/{conferenceName}/{role}")
    public String addRoleToUserInConference(@PathVariable String username, @PathVariable String conferenceName, @PathVariable ConferenceRole role, @RequestBody List<String> responsibleTopics);

    /**
     * 检查用户在会议中的角色
     *
     * @param username       用户名
     * @param conferenceName 会议名
     * @param role           角色
     * @return 检查结果
     */
    @GetExchange(url = "/check-role-of-user-in-conference")
    public Boolean checkRoleOfUserInConference(@RequestParam String username, @RequestParam String conferenceName, @RequestParam ConferenceRole role);
}
