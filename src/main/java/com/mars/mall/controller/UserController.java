package com.mars.mall.controller;

import com.mars.mall.consts.MallConst;
import com.mars.mall.form.UserLoginForm;
import com.mars.mall.form.UserRegisterForm;
import com.mars.mall.pojo.User;
import com.mars.mall.service.IUserService;
import com.mars.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @description: 用户模块Controller层
 * @author: Mars
 * @create: 2021-09-27 12:38
 **/
@RestController //@RestController注解相当于@ResponseBody + @Controller合在一起的作用
@Slf4j //在控制台打印日志
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 处理注册功能
     * @param userRegisterForm 传入用户必要信息(username,password,email)参数
     * BindingResult 用于接收前一个参数校验的错误信息, 前端将不再显示错误信息(本项目后续废弃)
     * @return
     * @RequestBody注解： 主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
     * 而最常用的使用请求体传参的无疑是POST请求了，所以使用@RequestBody接收数据时，一般都用POST方式进行提交。
     *
     *  * @Valid介绍及相关注解  主要用于表单验证，减轻了代码量
     *  * 在Springboot启动器的web包下包含了javax.validation.Valid，所以无需添加多余的依赖
     *  * 使用方式：
     *  * 1.在相关的实体类(form)的相关字段添加用于充当验证条件的注解
     *  * 2.在controller层的方法的要校验的参数上添加@Valid注解
     *  * 3.编写全局异常捕捉类
     */
    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userRegisterForm){

//        if (bindingResult.hasErrors()) {
//            log.error("注册提交的参数有误, {} {}",
//                    Objects.requireNonNull(bindingResult.getFieldError()).getField(),
//                    bindingResult.getFieldError().getDefaultMessage());
//            return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult);
//        }

        User user = new User();
        //Spring提供的BeanUtils类中copyProperties方法可以帮我们把一个对象中的字段复制到另一个对象的同名字段中
        BeanUtils.copyProperties(userRegisterForm,user);
        //当项目体量大一些后，就不直接用user实体类进行封装登记，而是再抽取出一层dto对象
        return userService.register(user);
    }

    /**
     * 处理登录功能
     * @param userLoginForm 传入用户必要信息(username,password)参数
     * @param session 利用session来保存当前登录用户的数据信息 data
     * @return
     */
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session){

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        //设置Session(服务端)，前端使用cookie：该Session键为常量，值为当前登录用户的对象实例user
        session.setAttribute(MallConst.CURRENT_USER,userResponseVo.getData());
        log.info("/login sessionId={}",session.getId());

        return userResponseVo;
    }

    //TODO 保存用户登录信息更建议使用token权限验证+Redis保存用户
    /**
     * 获取当前登录的用户信息
     * @param session 保存用户数据的session
     * @return
     * session保存在内存里，服务器重启或session过期都会使其失效，所以也可以使用token+redis来保存当前登录用户信息：分布式session
     */
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId={}",session.getId());
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success(user);
    }

    //TODO 判断登录状态，拦截器
    /**
     * 注销当前用户，只需将session域中保存的当前用户信息移除即可
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        log.info("/user sessionId={}",session.getId());
        session.removeAttribute(MallConst.CURRENT_USER);//移除session
        return ResponseVo.success();
    }
}
