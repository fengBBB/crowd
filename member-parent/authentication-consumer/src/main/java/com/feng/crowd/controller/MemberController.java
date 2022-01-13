package com.feng.crowd.controller;

import com.feng.crowd.api.MysqlRemoteService;
import com.feng.crowd.api.RedisRemoteService;
import com.feng.crowd.config.ShortMessageProperties;
import com.feng.crowd.constant.CrowdConstant;
import com.feng.crowd.entity.po.MemberPo;
import com.feng.crowd.entity.vo.MemberLoginVO;
import com.feng.crowd.entity.vo.MemberVo;
import com.feng.crowd.util.CrowdUtil;
import com.feng.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberController {
    @Autowired
    private ShortMessageProperties properties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    //获取验证码
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum){
        String key= CrowdConstant.REDIS_CODE_PREFIX+phoneNum;

        ResultEntity<String> sendMeaasgeResultEntity=CrowdUtil.sendCodeByShortMessage(properties.getHost(),properties.getPath(),properties.getMethod(),phoneNum,properties.getAppcode(),"1","1");

        String code= sendMeaasgeResultEntity.getData();

        if(ResultEntity.SUCCESS.equals(sendMeaasgeResultEntity.getResult())){
            System.out.println("发送成功了"+sendMeaasgeResultEntity.getResult());
            //如果发送成功 ，保存到redis中
            ResultEntity<String> saveCodeResultEntity=redisRemoteService.setRedisKeyValueRemoteWithTime(key,code,15, TimeUnit.MINUTES);

            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){


                System.out.println("保存成功了"+saveCodeResultEntity.getResult());
                return ResultEntity.successWithoutData();
            }else{
                return saveCodeResultEntity;
            }
        }else {
            return  sendMeaasgeResultEntity;
        }

    }

    //注册验证
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVo memberVo , ModelMap modelMap){
        // 1.获取用户输入的手机号
        String phone=memberVo.getPhoneNum();
        // 2 拼接redis中验证码的key
        String key =CrowdConstant.REDIS_CODE_PREFIX + phone;
        //从redis中获取code
        ResultEntity<String> value=redisRemoteService.getRedisStringVlaueByKey(key);
        //如果没有获取到 返回到注册界面
        if(ResultEntity.FAILED.equals(value.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,value.getMessage());
            return "member-reg";
        }

        String redisCode=value.getData();
        if(redisCode==null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        // 3 验证码比对
        String fromCode=memberVo.getCode();
        if(!fromCode.equals(redisCode)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        //redis中把code删除
        redisRemoteService.removeRedisKeyRemote(key);
        // 4 密码加密
        String beforepassword=memberVo.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String afterpassword=passwordEncoder.encode(beforepassword);
        memberVo.setUserpswd(afterpassword);
        // 5 存入数据库
        MemberPo memberPo= new MemberPo();
        //将memberVo中的属性复制到memberPo中
        BeanUtils.copyProperties(memberVo,memberPo);
        ResultEntity<String> save=mysqlRemoteService.saveMember(memberPo);
        if(ResultEntity.FAILED.equals(save.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,save.getMessage());
            return "member-reg";
        }
        // 6 返回登录界面
        return "redirect:http://localhost:80/auth/member/to/login/page";
    }

    //登录验证
    @RequestMapping("/auth/member/do/login")
    public String Login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd, ModelMap modelMap, HttpSession session){
        ResultEntity<MemberPo> LoginPo=mysqlRemoteService.getMemberPOByLogginAcctremote(loginacct);
        if(ResultEntity.FAILED.equals(LoginPo.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        MemberPo memberPo= LoginPo.getData();
        if(memberPo==null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return  "member-login";
        }
        //密码比较
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String loginpoPswd=LoginPo.getData().getUserpswd();
        boolean matchResult=passwordEncoder.matches(userpswd,loginpoPswd);
        if(!matchResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        //将登陆对象保存到session中
        MemberLoginVO memberLoginVO=new MemberLoginVO(memberPo.getUsername(),memberPo.getId(),memberPo.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
        return "redirect:http://localhost:80/auth/member/to/center/page";
    }

    //退出登录
    @RequestMapping("/auth/member/logout")
    public String Logout(HttpSession session){
        session.invalidate();
        return "redirect:http://localhost:80/";
    }
}
