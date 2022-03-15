package com.mars.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mars.mall.enums.ResponseEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * @description: vo(value objet)值对象
 * vo对象一般是用于业务层之间的数据传递的，主要体现在视图的对象。
 * 对于一个WEB页面将整个页面的属性封装成一个对象，然后用一个VO对象在控制层与视图层进行传输交换。
 * @author: Mars
 * @create: 2021-09-27 13:05
 *
 * 这个 VO 是其他 VO 的基础
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //加上这个注解，NON_NULL表示值为null的属性不会被包含在json中
public class ResponseVo<T> {
//json格式传递数据
    private Integer status;//状态

    private String msg;//文字信息

    private T data;//数据

    public ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    //成功，携带文字信息
    public static <T> ResponseVo<T> successByMsg(String msg) {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),msg);
    }
    //成功，携带数据
    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),data);
    }
    //成功，使用成功状态的文字信息desc:SUCCESS(0,"成功")
    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getDesc());
    }
    //出错，错误文字信息取决于错误状态类型
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum) {
        return new ResponseVo<>(responseEnum.getCode(),responseEnum.getDesc());
    }
    //出错，携带文字信息
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum,String msg) {
        return new ResponseVo<>(responseEnum.getCode(),msg);
    }
    //出错，使用BindingResult绑定指定ResponseEnum的错误信息
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult) {
        return new ResponseVo<>(responseEnum.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getField()
                        + " " + bindingResult.getFieldError().getCode());
    }
}



