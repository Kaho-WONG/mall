package com.mars.mall;

import com.mars.mall.consts.MallConst;
import com.mars.mall.exception.UserLoginException;
import com.mars.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 用户登录拦截器 ———— 用来在controller执行前统一判断登录状态
 * @author: Mars
 * @create: 2021-09-29 21:34
 **/
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     * preHandle指在controller执行之前进行拦截，true 表示继续流程，false 表示中断
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        //拦截请求，判断此时session中是否存在当前登录用户，若不存在，则是未登录
        User user = (User) request.getSession().getAttribute(MallConst.CURRENT_USER);
        if (user == null){
            log.info("user=null");
            //为了使拦截后前端不至于丢失后端controller返回的error，这里抛出异常，用异常类来返回json
            throw new UserLoginException();
            //return ResponseVo.error(ResponseEnum.NEED_LOGIN);//用户未登录
        }
        return true;
    }
}
