package org.fd.ase.grp15.common.iservice;

import jakarta.validation.Valid;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "http://ase-conference-service:8083/interior/conference")
public interface IConferenceService {


    /**
     * 查看某个会议中的详细信息
     *
     * @param conferenceName 会议全称
     * @return 会议详细信息
     */
    @GetExchange(url = "/conference-info")
    public ConferenceDTO getConferenceInfoByName(@RequestParam String conferenceName);

    /**
     * 更改会议状态信息
     *
     * @param conferenceName 会议全称,
     * @param status 会议状态，只能是[准备中、投稿中、审稿中、复议中、完成]中的一个
     * @return 更改结果
     */
    @PostExchange(url = "/change-conference-status")
    public String changeConferenceStatus(@RequestParam String conferenceName, @RequestParam String status);

}
