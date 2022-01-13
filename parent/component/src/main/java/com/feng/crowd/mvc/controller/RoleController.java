package com.feng.crowd.mvc.controller;

import com.feng.crowd.entity.Role;
import com.feng.crowd.service.api.RoleService;
import com.feng.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;


    @PreAuthorize("hasRole('部长')")
    @ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(@RequestParam(value = "pageNum" ,defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                                          @RequestParam(value = "keyword" ,defaultValue = "") String keyWord){
        PageInfo<Role> pageInfo=roleService.getPageInfo(pageNum,pageSize,keyWord);
        return  ResultEntity.successWithoutData(pageInfo);
    }


    @ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){
        roleService.update(role);
        return  ResultEntity.successWithoutData();
    }


    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> remove(@RequestBody List<Integer> roleIdList){
        roleService.removeRole(roleIdList);
        return  ResultEntity.successWithoutData();
    }
}
