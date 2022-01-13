package com.feng.crowd.controller;

import com.feng.crowd.api.MysqlRemoteService;
import com.feng.crowd.config.OSSProperties;
import com.feng.crowd.constant.CrowdConstant;
import com.feng.crowd.entity.vo.*;
import com.feng.crowd.util.CrowdUtil;
import com.feng.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectConsumerController {

    @Autowired
    private OSSProperties ossproperties;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer id, Model model){
        ResultEntity<DetailProjectVO> result=mysqlRemoteService.getDetailProjectVO(id);
        if(ResultEntity.SUCCESS.equals(result.getResult())){
            DetailProjectVO detailProjectVO=result.getData();
            model.addAttribute("detailProjectVO",detailProjectVO);
        }
        return "project-show-detail";
    }

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO,
                                       MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList,
                                       HttpSession session,
                                       ModelMap modelMap) throws IOException {
        //一 :保存头图到oss中 把访问地址保存到projectVO中
        //判断头图是否为空
        if(headerPicture.isEmpty()){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        //头图不为空 保存到oss中
       ResultEntity<String> uploadHeaderPicResultEntity=  CrowdUtil.uploadFileToOss(ossproperties.getEndPoint(),
                                    ossproperties.getAccessKeyId(),
                                    ossproperties.getAccessKeySecret(),
                                    headerPicture.getInputStream(),
                                    ossproperties.getBucketName(),
                                    ossproperties.getBucketDomain(),
                                    headerPicture.getOriginalFilename());
        String result=uploadHeaderPicResultEntity.getResult();
        //如果保存失败 返回表单页 如果上次成功 文件名保存到projectvo中
        if(ResultEntity.SUCCESS.equals(result)){
            String headPicturePath=uploadHeaderPicResultEntity.getData();
            projectVO.setHeaderPicturePath(headPicturePath);
        }else{
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return  "project-launch";
        }
        //二 ;将剩下的图片保存到oss中， 把访问地址保存到projectVO中
        List<String> detailPicturePathList= new ArrayList<String>();
        //检查detailPictureList是否有效
        if(detailPictureList==null || detailPictureList.size()==0){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return  "project-launch";
        }

        //如果有效 便利集合 把 图片保存到 oss中
        for (MultipartFile detailPicture: detailPictureList ){
            //检查单个图片是否为空
            if(detailPicture.isEmpty()){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            //执行保存
            ResultEntity<String> detailUploadResultEntity=CrowdUtil.uploadFileToOss(ossproperties.getEndPoint(),
                    ossproperties.getAccessKeyId(),
                    ossproperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossproperties.getBucketName(),
                    ossproperties.getBucketDomain(),
                    detailPicture.getOriginalFilename());
            //检查保存结果
            String detailUploadResult= detailUploadResultEntity.getResult();
            if(ResultEntity.SUCCESS.equals(detailUploadResult)){
                detailPicturePathList.add(detailUploadResultEntity.getData());
            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
                return  "project-launch";
            }
        }
        projectVO.setDetailPicturePathList(detailPicturePathList);
        //三 把projectVo 保存到session中
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
        return "redirect:http://localhost:80/project/return/info/page";
    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public  ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile multipartFile) throws IOException {
            //执行上传
        ResultEntity<String> uploadReturnPicResultEntity=CrowdUtil.uploadFileToOss(ossproperties.getEndPoint(),
                ossproperties.getAccessKeyId(),
                ossproperties.getAccessKeySecret(),
                multipartFile.getInputStream(),
                ossproperties.getBucketName(),
                ossproperties.getBucketDomain(),
                multipartFile.getOriginalFilename());
        System.out.println("图片上传成功");
        return  uploadReturnPicResultEntity;
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO , HttpSession session){
            //从session中获取之前保存的projectvo
                try {
                    ProjectVO projectVO=(ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
                    if (projectVO ==null){
                        return  ResultEntity.fialed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
                    }
                    //从projectvo中获取returnvoList
                    List<ReturnVO> returnVOS=projectVO.getReturnVOList();
                    if(returnVOS ==null){
                        returnVOS = new ArrayList<ReturnVO>();
                        projectVO.setReturnVOList(returnVOS);
                    }
                    //将returnvo保存到projectvo中
                    returnVOS.add(returnVO);
                    //将projectVO重新保存回session中
                    session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
                    System.out.println("session保存成功");
                    return  ResultEntity.successWithoutData();
                }catch (Exception e){
                    e.printStackTrace();
                    return ResultEntity.fialed(e.getMessage());
                }
    }

    @RequestMapping("/create/confirm")
    public String saveConfirm(ModelMap modelMap, HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO){
            // 从session中获取projectVo
            ProjectVO projectVO=(ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            //判断projectvo是否为空
            if(projectVO == null){
                throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }
            //不为空 将memberCOnfirmInfoVO保存到projectVO中
            projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
            //从session中读取当前用户
            MemberLoginVO memberLoginVO=(MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
            Integer memberId=memberLoginVO.getId();
            //将projectVo保存到mysql中
            ResultEntity<String>  saveResultEntity= mysqlRemoteService.saveProjectVORemote(projectVO,memberId);
            //判断是否保存成功
            if(ResultEntity.FAILED.equals(saveResultEntity.getResult())){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
                return "project-confirm";
            }
            //保存成功 把session中的projectvo 移除
            session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            //跳转最终页面
            return "redirect:http://localhost:80/project/create/success";
    }


}
