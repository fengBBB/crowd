package com.feng.crowd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class testController {

    @RequestMapping("test/spring/getsession")
    public String test(HttpSession session){
       String value= (String) session.getAttribute("guo");
       return value;
    }
}
