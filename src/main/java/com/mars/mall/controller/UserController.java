package com.mars.mall.controller;

import com.mars.mall.form.UserForm;
import com.mars.mall.pojo.User;
import com.mars.mall.service.IUserService;
import com.mars.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @description:
 * @author: Mars
 * @create: 2021-09-27 12:38
 **/
@RestController
@RequestMapping("/user")
@Slf4j //在控制台打印日志
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 处理注册功能
     * @param userForm 传入用户必要信息(username,password,email)参数
     * @param bindingResult 用于接收前一个参数校验的错误信息, 前端将不再显示错误信息
     * @return
     */
    @PostMapping("/register")
    public ResponseVo register(@RequestBody UserForm userForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.error("注册提交的参数有误, {} {}",
                    Objects.requireNonNull(bindingResult.getFieldError()).getField(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        User user = new User();
        BeanUtils.copyProperties(userForm,user);

        return userService.register(user);
    }
}
