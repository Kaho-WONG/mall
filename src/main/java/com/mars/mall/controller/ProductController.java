package com.mars.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mars.mall.service.IProductService;
import com.mars.mall.vo.ProductDetailVo;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 商品列表模块controller层
 * @author: Mars
 * @create: 2021-10-02 13:49
 **/
@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

    //默认展示第1页的10条数据
    @GetMapping("/products")
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return productService.list(categoryId,pageNum,pageSize);
    }

    //通过 @PathVariable 可以将URL中占位符参数{xxx}绑定到处理器类的方法形参中@PathVariable(“xxx“)
    @GetMapping("/products/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable Integer productId){
        return productService.detail(productId);
    }
}
