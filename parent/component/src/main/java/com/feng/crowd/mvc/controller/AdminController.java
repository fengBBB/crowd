package com.feng.crowd.mvc.controller;

import com.feng.crowd.constant.CrowdConstant;
import com.feng.crowd.entity.Admin;
import com.feng.crowd.service.api.AdminService;
import com.feng.crowd.service.impl.AdminServiceImpl;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String LoginAcct,
                          @RequestParam("userPswd") String password,
                          HttpSession session) {
        //调用service 检查登录
        // 如果能返回admin对象 就登录成功 如果返回null 代表登录失败
        Admin admin= adminService.getAdminbyLoginAcct(LoginAcct,password);
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        return "redirect:/admin/to/main/page.html";
    }


    @RequestMapping("/admin/do/logout.html")
    public String dpLogout(HttpSession session){
        //让session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/get/page.html")
    public String getpageInfo(@RequestParam(value = "keyword",defaultValue = "") String keyword,
                              @RequestParam(value = "pageNum" ,defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                              ModelMap modelMap){
       PageInfo<Admin> pageInfo= adminService.getPageInfo(keyword,pageNum,pageSize);
       modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
       return  "admin-page";
    }


    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminID,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword){
            adminService.remove(adminID);
            return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }


    @PreAuthorize("hasAuthority('user:add')")
    @RequestMapping("/admin/save.html")
    public String add(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    /*
    * 跳转修改页面并从数据库取出需要被修改的对象发送回页面
    * */
    @RequestMapping("admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,
                             ModelMap modelMap){
        Admin admin=adminService.getAdminByid(adminId);
        modelMap.addAttribute("admin",admin);
        return "admin-edit";
    }

    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
}
