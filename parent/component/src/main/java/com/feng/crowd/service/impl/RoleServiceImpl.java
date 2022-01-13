package com.feng.crowd.service.impl;

import com.feng.crowd.entity.Role;
import com.feng.crowd.entity.RoleExample;
import com.feng.crowd.mapper.RoleMapper;
import com.feng.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        //开启分页功能
        PageHelper.startPage(pageNum,pageSize);
        //执行查询
        List<Role> lists=roleMapper.selectRoleByKeyword(keyword);
        //查询结果封装到pageInfo中返回
        return new PageInfo<>(lists);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void update(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> list) {
        RoleExample roleExample= new RoleExample();
        RoleExample.Criteria criteria=roleExample.createCriteria();
        criteria.andIdIn(list);
        roleMapper.deleteByExample(roleExample);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {

        return roleMapper.selectAssignedRole(adminId);
    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selecrunAssignedRole(adminId);
    }


}
