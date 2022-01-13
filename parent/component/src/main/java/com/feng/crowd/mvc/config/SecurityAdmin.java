package com.feng.crowd.mvc.config;

import com.feng.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class SecurityAdmin extends User {
    private static final long serialVersionUID=1l;


    public Admin getOriginalAdmin() {
        return originalAdmin;
    }

    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPswd(),authorities);
        this.originalAdmin=originalAdmin;
        //将原始admin中的密码设置为null
        //spring Security 有密码擦除功能，但是擦除的是父类User的password
        //原始admin密码设置为null 也可以进行密码校验，因为前面supper()中已经把密码传输了，父类进行校验
        originalAdmin.setUserPswd(null);
    }
}
