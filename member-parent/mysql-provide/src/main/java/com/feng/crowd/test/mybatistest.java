package com.feng.crowd.test;

import com.feng.crowd.entity.po.MemberPo;
import com.feng.crowd.entity.vo.PortalProjectVO;
import com.feng.crowd.entity.vo.PortalTypeVO;
import com.feng.crowd.mapper.MemberPoMapper;
import com.feng.crowd.mapper.ProjectPOMapper;
import com.feng.crowd.util.CrowdUtil;
import com.feng.crowd.util.ResultEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class mybatistest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    MemberPoMapper memberPoMapper;

    @Autowired
    ProjectPOMapper poMapper;


    @Test
    public void test() throws SQLException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123123";
        String encode = passwordEncoder.encode(source);
        MemberPo memberPO = new MemberPo(null, "jack", encode, " 杰 克 ", "jack@qq.com", 1, 1, "杰克", "123123", 2);
        memberPoMapper.insert(memberPO);

    }


    @Test
    public void test2(){
        String host="https://dfsns.market.alicloudapi.com";
        String path="/data/send_sms";
        String method="POST";
        String appCode="b6e295671719498fba1db2977e5532c1";
        String phone="16608661646";

        ResultEntity<String> a=CrowdUtil.sendCodeByShortMessage(host,path,method,phone,appCode,"1","1");
        System.out.println(a);
    }

    @Test
    public void test3(){
       List<PortalTypeVO> lists= poMapper.selectPortalTypeVOList();
       for (PortalTypeVO portal: lists){
           String name=portal.getName();
           String remake=portal.getRemark();
           System.out.println("name:"+name+"      remake:"+remake);
           List<PortalProjectVO> pors=portal.getPortalProjectVOList();
           for (PortalProjectVO projectVO:pors){
               if (projectVO ==null){
                   continue;
               }
               System.out.println(projectVO.toString());
           }
       }
    }
}
