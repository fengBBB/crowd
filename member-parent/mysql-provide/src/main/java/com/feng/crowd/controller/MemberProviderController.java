package com.feng.crowd.controller;

import com.feng.crowd.constant.CrowdConstant;
import com.feng.crowd.entity.po.MemberPo;
import com.feng.crowd.entity.vo.ProjectVO;
import com.feng.crowd.service.api.MemberService;
import com.feng.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberProviderController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/memberpo/by/loggin/acct/remote")
    ResultEntity<MemberPo> getMemberPOByLogginAcctremote(@RequestParam("loginacct")String loginacct){
        try{
            MemberPo memberPo=memberService.getMemberPoByLoginAcct(loginacct);
            return  ResultEntity.successWithoutData(memberPo);
        }catch (Exception e){
                return  ResultEntity.fialed(e.getMessage());
        }
    }


    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPo memberPo){
            try{
                memberService.saveMember(memberPo);
                return  ResultEntity.successWithoutData();
            }catch (Exception e){
                if(e instanceof DuplicateKeyException){
                    return ResultEntity.fialed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
                }
                return ResultEntity.fialed(e.getMessage());
            }
    }


}
