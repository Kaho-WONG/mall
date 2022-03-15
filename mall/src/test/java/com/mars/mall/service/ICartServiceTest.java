package com.mars.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mars.mall.MallApplicationTests;
import com.mars.mall.form.CartAddForm;
import com.mars.mall.form.CartUpdateForm;
import com.mars.mall.vo.CartVo;
import com.mars.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();//用于在控制台打印出格式化的json

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(29);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1, form);
        log.info("list={}",gson.toJson(responseVo));
    }

    @Test
    public void list(){
        ResponseVo<CartVo> list = cartService.list(1);
        log.info("list={}",gson.toJson(list));
    }

    @Test
    public void update(){
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(5);
        form.setSelected(false);
        ResponseVo<CartVo> responseVo = cartService.update(1, 26, form);
        log.info("result={}",gson.toJson(responseVo));
    }

    @Test
    public void delete(){
        ResponseVo<CartVo> responseVo = cartService.delete(1, 26);
        log.info("result={}",gson.toJson(responseVo));
    }

    @Test
    public void selectAll(){
        ResponseVo<CartVo> responseVo = cartService.selectAll(1);
        log.info("result={}",gson.toJson(responseVo));
    }

    @Test
    public void unSelectAll(){
        ResponseVo<CartVo> responseVo = cartService.unSelectAll(1);
        log.info("result={}",gson.toJson(responseVo));
    }

    @Test
    public void sum(){
        ResponseVo<Integer> responseVo = cartService.sum(1);
        log.info("result={}",gson.toJson(responseVo));
    }
}