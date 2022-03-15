package com.mars.mall.service;

import com.github.pagehelper.PageInfo;
import com.mars.mall.vo.ProductDetailVo;
import com.mars.mall.vo.ResponseVo;

/**
 * @description:
 * @author: Mars
 * @create: 2021-10-02 00:42
 **/
public interface IProductService {

    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
