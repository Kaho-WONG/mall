package com.mars.mall.service.impl;

import com.mars.mall.consts.MallConst;
import com.mars.mall.dao.CategoryMapper;
import com.mars.mall.pojo.Category;
import com.mars.mall.service.ICategoryService;
import com.mars.mall.vo.CategoryVo;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 类目模块Service层功能实现
 * @author: Mars
 * @create: 2021-09-29 19:12
 *
 * 耗时： http(请求微信api) > 磁盘 > 内存
 * mysql(内网+磁盘)
 **/
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询除所有的类目，包括子类目，封装成ResponseVo形式
     * @return
     */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
//        List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();//获取所有的类目数据

        //查出parent_id=0的数据
//        for (Category category : categories){
//            //先提出根类目
//            if (category.getParentId().equals(MallConst.ROOT_PARENT_ID)){
//                CategoryVo categoryVo = new CategoryVo();
//                //Spring提供的BeanUtils类中copyProperties方法可以帮我们把一个对象中的字段复制到另一个对象的同名字段中
//                BeanUtils.copyProperties(category,categoryVo);//第一个参数是源对象，第二个是目标对象
//                categoryVoList.add(categoryVo);//先把根类目存进categoryVo列表
//            }
//        }

        //下面代码是使用"Lambda表达式+stream"完成上面的功能 (不建议，可读性差)
        //在所有类目数据中筛选出parent_id=0的类目(根类目)，并按优先级sortOrder从大到小排序
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(MallConst.ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());

        //查询根类目下的所有子类目及子类目的子类目
        findSubCategory(categoryVoList,categories);

        return ResponseVo.success(categoryVoList);
    }

    /**
     * 查询子类目，这个方法可以逐层查询出所有类目和他们的子类目并将他们的父子关系相关联
     * @param categoryVoList 父类目列表
     * @param categories 数据源(包含所有的类目)
     */
    private void findSubCategory(List<CategoryVo> categoryVoList,List<Category> categories){

        for (CategoryVo categoryVo : categoryVoList){   //这层循环是父类目

            List<CategoryVo> subCategoryVoList = new ArrayList<>();//一个类目的子类目列表

            for (Category category : categories){   //这层循环是遍历所有类目，找出父类目的子类目
                //如果查到内容，设置subCategory。继续往下查
                if (categoryVo.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                //将子类目进行排序，优先级sortOrder从大到小
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);//将子类目列表关联到父类目的子类目属性

                findSubCategory(subCategoryVoList,categories);//递归调用，把每层子类目都查出来
            }
        }
    }

    /**
     * 将类目Category类对象转化为CategoryVo类对象，即将类目属性值迁移到categoryVo
     * @param category
     * @return
     */
    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);//第一个参数是源对象，第二个是目标对象
        return categoryVo;
    }

    /**
     * 查找指定id类目的所有子类目(包括子类目的子类目)的id
     * @param id 指定类目的id
     * @param resultSet 包含所有子类目id的集合
     */
    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();//递归不要包含这条，因为重复查数据库没意义
        findSubCategoryId(id,resultSet,categories);
    }
    //重载上面的方法，专门做递归使用
    private void findSubCategoryId(Integer id, Set<Integer> resultSet,List<Category> categories) {
        for (Category category : categories){
            if (category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);//递归，把所查询id的类目中的所有子类目存入set
            }
        }
    }
}
