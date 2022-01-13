package com.feng.crowd.service.api;

import com.feng.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

public interface AuthService {

    public List<Auth> getAuthAll();

    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    public void saveRoleAuthRelathinship(Map<String,List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
