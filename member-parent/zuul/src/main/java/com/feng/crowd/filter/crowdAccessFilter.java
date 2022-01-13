package com.feng.crowd.filter;

import com.feng.crowd.constant.CrowdConstant;
import com.feng.crowd.util.AccessPassResources;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class crowdAccessFilter  extends ZuulFilter {
    public String filterType() {
        //这里返回pre 表示过滤在访问前执行
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }

    public boolean shouldFilter() {
        RequestContext requestContext= RequestContext.getCurrentContext();
        HttpServletRequest request= requestContext.getRequest();
        String servletPath= request.getServletPath();
        System.out.println(servletPath);
        boolean containResult= AccessPassResources.PASS_RES_SET.contains(servletPath);
        if(containResult==true){
            return false;
        }else {
            return !AccessPassResources.jedgeCurrentServletPathWetherStaticResource(servletPath);
        }
    }

    public Object run() throws ZuulException {
        //获取request
        RequestContext requestContext= RequestContext.getCurrentContext();
        HttpServletRequest request=requestContext.getRequest();
        //获取session
        HttpSession session= request.getSession();
        //查看session 有没有登录
        Object LoginMember= session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        System.out.println(LoginMember);
        if(LoginMember == null){
            HttpServletResponse response=requestContext.getResponse();
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
            try{
                response.sendRedirect("/auth/member/to/login/page");
            }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
            }
        }
        return null;
    }
}
