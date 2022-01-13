package com.feng.crowd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class testController {

    @RequestMapping("/test/spring/session")
    public  String testSession(HttpSession session){
            session.setAttribute("guo","feng");
            return "数据存入session域中";
    }
}
