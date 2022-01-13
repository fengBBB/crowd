package com.feng.crowd.service.impl;

import com.feng.crowd.entity.po.MemberPo;
import com.feng.crowd.entity.po.MemberPoExample;
import com.feng.crowd.entity.vo.ProjectVO;
import com.feng.crowd.mapper.MemberPoMapper;
import com.feng.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPoMapper memberPoMapper;

    public MemberPo getMemberPoByLoginAcct(String Loginact) {
        MemberPoExample memberPoExample= new MemberPoExample();
        MemberPoExample.Criteria criteria= memberPoExample.createCriteria();
        criteria.andLoginacctEqualTo(Loginact);
        List<MemberPo> lists=memberPoMapper.selectByExample(memberPoExample);
        return lists.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
                    rollbackFor = Exception.class,
                    readOnly = false)
    public void saveMember(MemberPo memberPo) {
            memberPoMapper.insert(memberPo);
    }

}
