package com.feng.crowd.mvc.controller;

import com.feng.crowd.entity.Auth;
import com.feng.crowd.entity.Role;
import com.feng.crowd.service.api.AdminService;
import com.feng.crowd.service.api.AuthService;
import com.feng.crowd.service.api.RoleService;
import com.feng.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignController {

    @Autowired
    RoleService roleService;

    @Autowired
    AdminService adminService;

    @Autowired
    AuthService authService;

    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolepage(@RequestParam("adminId") Integer adminId,
                                   ModelMap modelMap){
        //查询已分配角色
        List<Role> AssignRoleList=roleService.getAssignedRole(adminId);
        //查询未分配角色
        List<Role> unAssignRoleList=roleService.getUnAssignedRole(adminId);
        //存入模型
        modelMap.addAttribute("assignedRoleList",AssignRoleList);
        modelMap.addAttribute("unAssignedRoleList",unAssignRoleList);
        return  "assign-role";

    }

    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(@RequestParam("adminId") Integer adminid,
                                            @RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("keyword") String keyword,
                                            @RequestParam(value = "roleIdList",required = false) List<Integer> roleIdList){

        adminService.saveAdminRoleRelationship(adminid,roleIdList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }


    @ResponseBody
    @RequestMapping("/assgin/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){
          List<Auth> authList= authService.getAuthAll();
          return  ResultEntity.successWithoutData(authList);
    }

    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(@RequestParam("roleId") Integer roleId){
        List<Integer> authIdList=authService.getAssignedAuthIdByRoleId(roleId);
        return  ResultEntity.successWithoutData(authIdList);
    }


    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelathinship(@RequestBody Map<String,List<Integer>> map){
        authService.saveRoleAuthRelathinship(map);
        return  ResultEntity.successWithoutData();
    }
}
