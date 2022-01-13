package com.feng.crowd.mvc.config;

import com.feng.crowd.util.CrowdUtil;
import com.feng.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CrowdExceptionResolver  {

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = "system-error";
        return commonResolver(viewName, exception, request, response);
    }


    private ModelAndView commonResolver(
            //异常处理完后要去的页面
            String viewName,
            //实际捕获的异常
            Exception exception,
            //当前请求对象
            HttpServletRequest request,
            //当前相应对象
            HttpServletResponse response
    ) throws Exception {
        //判断当期请求是不是ajax请求
        boolean jedgeResult = CrowdUtil.judgeRequestType(request);
        //如果是ajax请求
        if (jedgeResult) {
            //创建resultEntity对象
            ResultEntity<Object> resultentity = ResultEntity.fialed(exception.getMessage());
            //创建gson对象，将resultentity转换为json数据发送给浏览器
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(resultentity));
            //response以返回数据就不需要返回modelAndView了
            return null;
        }
        //如果不是json请求,创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        //将Exception对象存入模型
        modelAndView.addObject("exception", exception);
        //设置对应视图名称,将要去的页面
        modelAndView.setViewName(viewName);
        //返回视图
        return modelAndView;
    }



}
