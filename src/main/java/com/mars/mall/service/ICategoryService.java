package com.mars.mall.service;

import com.mars.mall.vo.CategoryVo;
import com.mars.mall.vo.ResponseVo;

import java.util.List;

/**
 * @description:
 * @author: Mars
 * @create: 2021-09-29 19:09
 **/
public interface ICategoryService {

    ResponseVo<List<CategoryVo>> selectAll();//查询所有的类目
}
