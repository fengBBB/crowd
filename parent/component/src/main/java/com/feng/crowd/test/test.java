package com.feng.crowd.test;

import com.feng.crowd.util.CrowdUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    public void test(){
        String password="1221221";
        System.out.println(passwordEncoder.getClass());
    }



}
