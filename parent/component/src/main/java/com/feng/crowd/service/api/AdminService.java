package com.feng.crowd.service.api;

import com.feng.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {
    public Admin getAdminbyLoginAcct(String loginAcct, String userPswd);
    public List<Admin> findall();
    public PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);
    public void remove(Integer id);
    public void saveAdmin(Admin admin);
    public void update(Admin admin);
    public Admin getAdminByid(Integer id);
    public void saveAdminRoleRelationship(Integer adminid,List<Integer> roleList);
    public Admin getAdminBylogginact(String username);
}
