package com.mars.mall.service.impl;

import com.google.gson.Gson;
import com.mars.mall.dao.ProductMapper;
import com.mars.mall.enums.ProductStatusEnum;
import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.form.CartAddForm;
import com.mars.mall.form.CartUpdateForm;
import com.mars.mall.pojo.Cart;
import com.mars.mall.pojo.Product;
import com.mars.mall.service.ICartService;
import com.mars.mall.vo.CartProductVo;
import com.mars.mall.vo.CartVo;
import com.mars.mall.vo.ResponseVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 购物车模块service层
 * @author: Mars
 * @create: 2021-10-02 19:37
 **/
@Service
public class CartServiceImpl implements ICartService {

    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate; //提供了一系列操作Redis的方法

    private Gson gson = new Gson();//提供java对象序列化和反序列化功能

    /**
     * 向购物车里添加一件商品编号为 form中的属性id的商品
     * @param uid 存进redis中的键后缀编号("cart_%d"中的 %d)
     * @param form 包含添加一件商品进购物车的必要请求表单信息(productId、selected=true)
     * @return
     */
    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
        Integer quantity = 1;//默认每次添加进购物车一件商品

        Product product = productMapper.selectByPrimaryKey(form.getProductId());

        //商品是否存在
        if (product == null){
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        //商品是否正常在售
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //商品库存是否充足
        if (product.getStock() <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        //写入到redis
        //key:不同账户的购物车cart_1、cart_2... value:购物车中的map结构(key:商品id value:商品数据json)
        /**
         * 使用redis中的哈希表来存储购物车中的商品，使得查询存取购物车中某件商品更为迅速
         * HashOperations<String, String, String>:第一个String是redis中的键()，其对应的值是hash结构
         * 所以第二个String是hash结构中的键(SDS)，最后一个String是hash结构中的值(Json字符串)
         *
         * format(string,Object...args),这里可以实现uid代替CART_REDIS_KEY_TEMPLATE中的"%d"
         *    toJson  将一个Java对象转化为Json字符串
         */
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //设定购物车键的名字，redis中存储了很多个账户的购物车，所以会有多个购物车键(以uid结尾,形如cart_1)
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        Cart cart;//作为辅助的购物车内商品对象
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));
        if (StringUtils.isNullOrEmpty(value)){
            //没有该商品，新增
            cart = new Cart(product.getId(),quantity,form.getSelected());
        } else {
            //购物车中已经有该商品，数量+1
            cart = gson.fromJson(value,Cart.class);//将json字符串反序列化为cart对象
            cart.setQuantity(cart.getQuantity() + quantity);//将购物车中已有商品数量+1
        }
        //将指定商品(带商品id作为map键索引)存入指定的购物车(键)中
        opsForHash.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));

        return list(uid);
    }

    /**
     * 列表功能，将购物车信息以及其包含的商品指定信息罗列出来
     * @param uid 不同账户的购物车编号
     * @return
     */
    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);//购物车编号为"cart_uid"的购物车存放的商品map集合(键为商品id，值为购物车商品对象cart)

        CartVo cartVo = new CartVo();//购物车vo对象,包含下面四个属性
        boolean selectAll = true;//默认全选
        Integer cartTotalQuantity = 0;//购物车商品数量
        BigDecimal cartTotalPrice = BigDecimal.ZERO;//购物车所有商品总价
        List<CartProductVo> cartProductVoList = new ArrayList<>();//购物车vo对象中存放多个商品信息的列表属性

        for (Map.Entry<String,String> entry : entries.entrySet()){
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            //TODO 需要优化，使用mysql里的in
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(
                        productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                cartProductVoList.add(cartProductVo);

                if (!cart.getProductSelected()) {
                    selectAll = false;//但凡有一个商品没有选中，就不是全选
                }
                //计算购物车中所有选中的商品总价(累加)
                if (cart.getProductSelected()){
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
            }

            cartTotalQuantity += cart.getQuantity();//每种商品在购物车中的数量累加
        }
        cartVo.setCartProductVoList(cartProductVoList);//关联购物车和其中的列表属性

        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);

        return ResponseVo.success(cartVo);
    }

    /**
     * 更新购物车中商品条目的信息
     * @param uid 账户的购物车编号
     * @param productId 商品编号
     * @param form 附带有修改信息(产品数量和选中状态)的表单
     * @return
     */
    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isNullOrEmpty(value)){
            //没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //购物车中已经有该商品，修改内容
        Cart cart = gson.fromJson(value,Cart.class);//将json字符串反序列化为cart对象
        if (form.getQuantity() != null && form.getQuantity() >= 0) {
            cart.setQuantity(form.getQuantity());
        }
        if (form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }

        opsForHash.put(redisKey,String.valueOf(productId),gson.toJson(cart));

        return list(uid);
    }

    /**
     * 删除购物车里的某件商品
     * @param uid 购物车编号
     * @param productId 商品编号
     * @return
     */
    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isNullOrEmpty(value)){
            //没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        //删除redis中的某个hash键中的键，即某个购物车中的某个商品
        opsForHash.delete(redisKey,String.valueOf(productId));

        return list(uid);
    }

    /**
     * 全选中
     * @param uid 购物车编号
     * @return
     */
    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }

        return list(uid);
    }

    /**
     * 全不选中
     * @param uid 购物车编号
     * @return
     */
    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }

        return list(uid);
    }

    /**
     * 返回购物车全部商品数量总和 (小接口，只在页面返回Integer的data)
     * @param uid 购物车编号
     * @return
     */
    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        //利用lambda+stream将商品列表转换成流，从中读取每个商品的数量并累加起来返回
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);//reduce从0开始累加每个cart的quantity
        return ResponseVo.success(sum);
    }

    /**
     * 供全选和全不选方法使用的方法，用于查询出包含购物车全部商品的列表
     * 也供订单模块create方法使用
     * @param uid
     * @return
     */
    @Override
    public List<Cart> listForCart(Integer uid){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            cartList.add(gson.fromJson(entry.getValue(),Cart.class));
        }
        return cartList;
    }
}
