package com.mars.mall.exception;

import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 基于AOP的对Controller层的异常处理类，用于处理突发的非客户人为的异常
 * @author: Mars
 * @create: 2021-09-27 23:30
 **/

@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody //传给前端json格式
//    @ResponseStatus(HttpStatus.FORBIDDEN) //强制返回的http状态码为403FORBIDDEN
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ResponseEnum.ERROR,e.getMessage());
    }
}
