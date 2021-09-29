package com.mars.mall.service.impl;

import com.mars.mall.dao.CategoryMapper;
import com.mars.mall.pojo.Category;
import com.mars.mall.service.ICategoryService;
import com.mars.mall.vo.CategoryVo;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mars.mall.consts.MallConst.ROOT_PARENT_ID;

/**
 * @description:
 * @author: Mars
 * @create: 2021-09-29 19:12
 **/
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();//获取所有的类目

        //查出parent_id=0的数据
        for (Category category : categories){
            //先提出根类目
            if (category.getParentId().equals(ROOT_PARENT_ID)){
                CategoryVo categoryVo = new CategoryVo();
                //Spring提供的BeanUtils类中copyProperties方法可以帮我们把一个对象中的字段复制到另一个对象的同名字段中
                BeanUtils.copyProperties(category,categoryVo);//第一个参数是源对象，第二个是目标对象
                categoryVoList.add(categoryVo);//先把根类目存进categoryVo列表
            }
        }
        return ResponseVo.success(categoryVoList);
    }
}
