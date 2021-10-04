package com.mars.mall.exception;

import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.vo.ResponseVo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * @description: 基于AOP的对Controller层的异常处理类，用于处理突发的非客户人为的异常
 * @author: Mars
 * @create: 2021-09-27 23:30
 *
 * @ExceptionHandler：统一处理某一类异常，从而能够减少代码重复率和复杂度
 * @ControllerAdvice：异常集中处理，更好的使业务逻辑与异常处理剥离开；其是对Controller层进行拦截
 * @ResponseStatus：可以将某种异常映射为HTTP状态码
 **/

@ControllerAdvice//全局异常处理
public class RuntimeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)//统一捕获处理运行时异常
    @ResponseBody //将java对象转为json格式
//    @ResponseStatus(HttpStatus.FORBIDDEN) //强制返回的http状态码为403FORBIDDEN
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ResponseEnum.ERROR,e.getMessage());
    }

    @ExceptionHandler(UserLoginException.class)//捕获用户未登录异常
    @ResponseBody
    public ResponseVo userLoginHandle() {
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);//返回用户未登录的error对象
    }

    /**
     * 统一处理用户传入参数错误
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,
                bindingResult.getFieldError().getField() + " "
                        + bindingResult.getFieldError().getDefaultMessage());
    }
}
