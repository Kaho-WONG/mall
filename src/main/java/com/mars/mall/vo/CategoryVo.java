package com.mars.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Mars
 * @create: 2021-09-29 17:57
 **/
@Data
public class CategoryVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategories;//子类目

}
