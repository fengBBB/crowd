package com.feng.crowd.controller;

import com.feng.crowd.api.MysqlRemoteService;
import com.feng.crowd.constant.CrowdConstant;
import com.feng.crowd.entity.vo.PortalTypeVO;
import com.feng.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class portalController {

    @Autowired
    private MysqlRemoteService mysqlService;

    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap){
        ResultEntity<List<PortalTypeVO>> portalTypeVOList= mysqlService.getPortalTypeVOList();

        if(ResultEntity.SUCCESS.equals(portalTypeVOList.getResult())){
            List<PortalTypeVO> portalTypeVOS= portalTypeVOList.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,portalTypeVOS);
        }
        return  "portal";
    }


}
