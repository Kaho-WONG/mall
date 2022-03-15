package com.mars.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mars.mall.dao.ProductMapper;
import com.mars.mall.enums.ProductStatusEnum;
import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.pojo.Product;
import com.mars.mall.service.ICategoryService;
import com.mars.mall.service.IProductService;
import com.mars.mall.vo.ProductDetailVo;
import com.mars.mall.vo.ProductVo;
import com.mars.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 商品模块service层，提供了商品页面展示、商品详情页展示功能
 * @author: Mars
 * @create: 2021-10-02 00:45
 **/
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ICategoryService categoryService; //需要使用到类目service提供的功能

    @Autowired
    private ProductMapper productMapper;

    /**
     * 商品页面展示
     * @param categoryId 商品所属类目的id
     * @param pageNum 页码
     * @param pageSize 页面条目数量
     * @return
     */
    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null) {
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            categoryIdSet.add(categoryId);//上面方法调用只是把该类目的子类目加入集合，这里要把它自己本身也加入
        }

        PageHelper.startPage(pageNum,pageSize);//先设置好页面的分页

        //使用stream+lambda把类目id集合中的产品都查询出来，并封装到productVoList
        List<Product> productsList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVo> productVoList = productsList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                }).collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo(productsList); //设置分页页面信息
        pageInfo.setList(productVoList); //将商品信息展示在分好的页面上

        return ResponseVo.success(pageInfo);
    }

    /**
     * 商品详情页面展示
     * @param productId 商品id
     * @return
     */
    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //商品下架或者被删除，则抛出错误
        if (product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode())
                || product.getStatus().equals(ProductStatusEnum.DELETE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product,productDetailVo);
        //敏感数据处理
        productDetailVo.setStock(product.getStock() > 100 ? 100 :product.getStock());//隐瞒真实库存
        return ResponseVo.success(productDetailVo);
    }
}
