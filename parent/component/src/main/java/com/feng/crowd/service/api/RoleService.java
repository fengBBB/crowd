package com.feng.crowd.service.api;

import com.feng.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {

    public PageInfo<Role> getPageInfo(Integer pageNum,Integer pageSize,String keyword);

    public void saveRole(Role role);

    public void update(Role role);

    public void removeRole(List<Integer> list);

    public List<Role> getAssignedRole(Integer adminId);

    public List<Role> getUnAssignedRole(Integer adminId);

}
