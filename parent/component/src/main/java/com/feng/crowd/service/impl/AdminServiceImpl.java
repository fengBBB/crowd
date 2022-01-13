package com.feng.crowd.service.impl;

import com.feng.crowd.constant.CrowdConstant;
import com.feng.crowd.entity.Admin;
import com.feng.crowd.entity.AdminExample;
import com.feng.crowd.exception.LogginAcctAlreadyInUseException;
import com.feng.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.feng.crowd.exception.LoginFailedException;
import com.feng.crowd.mapper.AdminMapper;
import com.feng.crowd.service.api.AdminService;
import com.feng.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
   private AdminMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Admin getAdminbyLoginAcct(String loginAcct, String userPswd) {
        //通过LoginAcct 取出admin
        AdminExample adminExample= new AdminExample();
        AdminExample.Criteria criteria=adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> admins= mapper.selectByExample(adminExample);

        //如果admins为空，表示没有这个用户
        if(admins==null || admins.size()==0){

            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //如果admins有一个以上 表示同一个账户名在数据库内有两个，这是不合规的
        if(admins.size()>1){
            throw  new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin=admins.get(0);
        if(admin == null){
            throw  new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //将密码从admin中取出
        String passwordDB=admin.getUserPswd();
        //将提交表单的明文密码进行加密
        String userpasswordMD5= CrowdUtil.md5(userPswd);
        //将加密密码和数据库密码进行比对
        if(!Objects.equals(passwordDB,userpasswordMD5)){
            //如果比较不一致 抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        return  admin;
    }

    public List<Admin> findall(){
       return  mapper.selectByExample(new AdminExample());
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        //调用pageHelp开启分页功能
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> admins=mapper.selectAdminbyKeyword(keyword);
        return new PageInfo<>(admins);
    }

    @Override
    public void remove(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }


    public void saveAdmin(Admin admin){
        //密码加密
        String userPwd= admin.getUserPswd();
        userPwd=passwordEncoder.encode(userPwd);
        admin.setUserPswd(userPwd);
        //生成创建时间
        Date date= new Date();
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createtime=format.format(date);
        admin.setCreateTime(createtime);
        //执行保存
        try {
            mapper.insert(admin);
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                throw  new LogginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void update(Admin admin) {
        try {
            mapper.updateByPrimaryKeySelective(admin);
        }catch (Exception e){
            if (e instanceof  DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public Admin getAdminByid(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminid, List<Integer> roleList) {
        //删除admin id下所有数据
        mapper.deleteOLdRelationship(adminid);
        //将roleList集合里所有的数据和adminid 绑定存入数据库
        if(roleList !=null&&roleList.size()>0) {
            mapper.insertNewRelationship(adminid, roleList);
        }
    }

    @Override
    public Admin getAdminBylogginact(String username) {
        AdminExample example= new AdminExample();
        AdminExample.Criteria criteria= example.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        List<Admin> admins=mapper.selectByExample(example);
        return admins.get(0);
    }
}
