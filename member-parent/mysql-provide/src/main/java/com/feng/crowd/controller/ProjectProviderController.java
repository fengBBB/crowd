package com.feng.crowd.controller;

import com.feng.crowd.entity.vo.DetailProjectVO;
import com.feng.crowd.entity.vo.PortalTypeVO;
import com.feng.crowd.entity.vo.ProjectVO;
import com.feng.crowd.service.api.MemberService;
import com.feng.crowd.service.api.ProjectService;
import com.feng.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ProjectProviderController {
    @Autowired
    private ProjectService Projectservice;


    @RequestMapping("/save/ProjectVO/Remote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){
        try {
            Projectservice.saveProjectVORemote(projectVO, memberId);
            return  ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return  ResultEntity.fialed(e.getMessage());
        }
    }

    @RequestMapping("/get/PortalTypeVOList")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeVOList(){
       try{
        List<PortalTypeVO> PortalTypeVOList=Projectservice.getPortalTypeVOList();
        return  ResultEntity.successWithoutData(PortalTypeVOList);
       }catch (Exception e){
           e.printStackTrace();
           return  ResultEntity.fialed(e.getMessage());
       }
    }

    @RequestMapping("/get/DetailProjectVO")
    public ResultEntity<DetailProjectVO> getDetailProjectVO(Integer id){
        try{
            DetailProjectVO detailProjectVO=Projectservice.getDetailProjectVO(id);
            return  ResultEntity.successWithoutData(detailProjectVO);
        }catch (Exception e){
            e.printStackTrace();
            return  ResultEntity.fialed(e.getMessage());
        }
    }
}
