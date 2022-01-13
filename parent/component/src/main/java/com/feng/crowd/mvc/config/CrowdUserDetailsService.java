package com.feng.crowd.mvc.config;

import com.feng.crowd.entity.Admin;
import com.feng.crowd.entity.Auth;
import com.feng.crowd.entity.Role;
import com.feng.crowd.service.api.AdminService;
import com.feng.crowd.service.api.AuthService;
import com.feng.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            //根据账号查询admin对象
            Admin admin=adminService.getAdminBylogginact(username);
            //获得adminid
            Integer adminId=admin.getId();
            //根据adminid查询角色信息
            List<Role> roles=roleService.getAssignedRole(adminId);
            //根据adminid查询权限信息
            List<String> auths=authService.getAssignedAuthNameByAdminId(adminId);
            //创建集合对象用来存储GrantedAuthority
            List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
            //便利 角色和权限 存入GrantedAuthority
            for(Role role :roles){
                String roleName="ROLE_"+role.getName();
                SimpleGrantedAuthority simpleGrantedAuthority= new SimpleGrantedAuthority(roleName);
                grantedAuthorities.add(simpleGrantedAuthority);
            }

            for (String authname:auths){
                SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(authname);
                grantedAuthorities.add(simpleGrantedAuthority);
            }
            //封装SecurityAdmin
            SecurityAdmin securityAdmin= new SecurityAdmin(admin,grantedAuthorities);

        return securityAdmin;
    }
}
