package com.feng.crowd.service.impl;

import com.feng.crowd.entity.po.MemberConfirmInfoPO;
import com.feng.crowd.entity.po.MemberLaunchInfoPO;
import com.feng.crowd.entity.po.ProjectPO;
import com.feng.crowd.entity.po.ReturnPO;
import com.feng.crowd.entity.vo.*;
import com.feng.crowd.mapper.*;
import com.feng.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void saveProjectVORemote(ProjectVO projectVO, Integer memberId) {
        // 一、保存ProjectPO对象
        ProjectPO projectPO= new ProjectPO();
        BeanUtils.copyProperties(projectVO,projectPO);
        projectPO.setMemberid(memberId);
        //生成创建时间
        String creatData=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(creatData);
        projectPO.setStatus(0);
        projectPOMapper.insert(projectPO);
        //获得projectID
        Integer projectId=projectPO.getId();
        //二、保存项目、分类的关联关系信息
        List<Integer> typeList=projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeList,projectId);
        // 三、保存项目、标签的关联关系信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList, projectId);
        // 四、保存项目中详情图片路径信息
        List<String> detailPicturePathList=projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(detailPicturePathList,projectId);
        // 五、保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO=projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLauchInfoPO= new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO,memberLauchInfoPO);
        memberLauchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLauchInfoPO);
        // 六、保存项目回报信息
        List<ReturnVO> returnVOS= projectVO.getReturnVOList();
        List<ReturnPO> returnPOS=new ArrayList<ReturnPO>();
        for (ReturnVO returnVO: returnVOS){
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO,returnPO);
            returnPOS.add(returnPO);
        }
        returnPOMapper.insertReturnPOBatch(returnPOS,projectId);
        // 七、保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO= new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    public List<PortalTypeVO> getPortalTypeVOList() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        DetailProjectVO detailProjectVO=projectPOMapper.selectDetailProjectVO(projectId);
        Integer status=detailProjectVO.getStatus();
        switch (status){
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
        }
        // 3.根据deployeDate计算lastDay
        String deploydate=detailProjectVO.getDeployDate();
        //获取当前日期
        Date date=new Date();
        //把众筹日期转换为date类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay=format.parse(deploydate);
            //获取当前日期时间戳
            Long currentTimeStamp=date.getTime();
            //获取众筹创建时间戳
            Long deployTimeStamp=deployDay.getTime();
            // 两个时间戳相减计算当前已经过去的时间
            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;
            //获取众筹总时间
            Integer totalDays=detailProjectVO.getDay();
            // 使用总的众筹天数减去已经过去的天数得到剩余天数
            Integer lastDay = (int) (totalDays - pastDays);
            detailProjectVO.setLastDay(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }
}
