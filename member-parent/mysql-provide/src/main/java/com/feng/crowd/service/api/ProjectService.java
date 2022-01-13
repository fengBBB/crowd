package com.feng.crowd.service.api;

import com.feng.crowd.entity.vo.DetailProjectVO;
import com.feng.crowd.entity.vo.PortalTypeVO;
import com.feng.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    public void saveProjectVORemote(ProjectVO projectVO, Integer memberId);

    public List<PortalTypeVO> getPortalTypeVOList();

    public DetailProjectVO getDetailProjectVO(Integer projectId);
}
