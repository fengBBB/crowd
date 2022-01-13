package com.feng.crowd.service.api;

import com.feng.crowd.entity.po.MemberPo;
import com.feng.crowd.entity.vo.ProjectVO;

public interface MemberService {

    public MemberPo getMemberPoByLoginAcct(String Loginact);

    public void saveMember(MemberPo memberPo);


}
