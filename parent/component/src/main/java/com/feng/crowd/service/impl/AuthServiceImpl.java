package com.feng.crowd.service.impl;

import com.feng.crowd.entity.Auth;
import com.feng.crowd.entity.AuthExample;
import com.feng.crowd.mapper.AuthMapper;
import com.feng.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthMapper authMapper;

    @Override
    public List<Auth> getAuthAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {
        //获取roleID的值
        List<Integer> rolelist=map.get("roleId");
        Integer roleId=rolelist.get(0);
        //删除roleid关联的所有数据
        authMapper.deletebyRoleId(roleId);
        //取出authIdList
        List<Integer> authIdList=map.get("authIdArray");
        if(authIdList != null && authIdList.size() > 0){
            System.out.println("hello ++++++++++++++");
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        List<String> auth=authMapper.selectAssignedAuthNameByAdminId(adminId);
        return auth;
    }
}
