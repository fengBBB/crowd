package com.feng.crowd.mvc.controller;

import com.feng.crowd.entity.Admin;
import com.feng.crowd.service.api.AdminService;
import com.feng.crowd.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class testController {

    @Autowired
    AdminService adminService;

    @RequestMapping("test")
    public String test(ModelMap modelMap){
        System.out.println("hello");
        List<Admin> lists=adminService.findall();
        modelMap.addAttribute("adminlists",lists);
        return "targe";
    }
}
