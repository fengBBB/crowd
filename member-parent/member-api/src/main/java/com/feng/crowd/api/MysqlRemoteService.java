package com.feng.crowd.api;

import com.feng.crowd.entity.po.MemberPo;
import com.feng.crowd.entity.vo.DetailProjectVO;
import com.feng.crowd.entity.vo.PortalTypeVO;
import com.feng.crowd.entity.vo.ProjectVO;
import com.feng.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("feng-crowd-mybatis")
public interface MysqlRemoteService {


    @RequestMapping("/get/memberpo/by/loggin/acct/remote")
    ResultEntity<MemberPo> getMemberPOByLogginAcctremote(@RequestParam("loginacct")String loginacct);

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPo memberPo);

    @RequestMapping("/save/ProjectVO/Remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId);

    @RequestMapping("/get/PortalTypeVOList")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeVOList();

    @RequestMapping("/get/DetailProjectVO")
    public ResultEntity<DetailProjectVO> getDetailProjectVO(@RequestParam("id") Integer id);
}
