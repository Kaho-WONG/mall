package com.mars.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mars.mall.dao.OrderItemMapper;
import com.mars.mall.dao.OrderMapper;
import com.mars.mall.dao.ProductMapper;
import com.mars.mall.dao.ShippingMapper;
import com.mars.mall.enums.OrderStatusEnum;
import com.mars.mall.enums.PaymentTypeEnum;
import com.mars.mall.enums.ProductStatusEnum;
import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.pojo.*;
import com.mars.mall.service.ICartService;
import com.mars.mall.service.IOrderService;
import com.mars.mall.vo.OrderItemVo;
import com.mars.mall.vo.OrderVo;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 订单模块Service层
 * @author: Mars
 * @create: 2021-10-05 12:01
 **/
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;//提供收货地址模块持久层服务

    @Autowired
    private ICartService cartService;//提供购物车模块相关的服务

    @Autowired
    private ProductMapper productMapper;//提供商品模块持久层服务

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 创建订单
     * @param uid 用户id
     * @param shippingId 收货地址id
     * @return
     */
    @Override
    @Transactional //添加事务，方法内任何一个数据库操作失败或运行时异常都会造成整体回滚
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //校验收货地址是否存在
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null){
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        //获取购物车中被选中的商品列表，校验 (是否有商品、库存)
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList)){
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);//未选中任何商品
        }

        /*
        获取cartList里的所有商品的productId(放在list里)，然后根据所得到的productId获取商品product
        因为上面的cartList的cart只有几个属性，我们需要用到product中的其他属性，所以需要查出完整的product
         */
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));//用一个map来存放商品id和商品关联的键值对

        List<OrderItem> orderItemList = new ArrayList<>();//存放多个订单条目的list
        Long orderNo = generateOrderNo();//生成唯一订单号
        for (Cart cart : cartList) {
            //根据productId查数据库,这步放在循环外(上面),减少查询数据库次数
            Product product = map.get(cart.getProductId());
            //判断是否有该商品
            if (product == null){
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,
                        "商品不存在. productId = " + cart.getProductId());
            }
            //商品状态是会变的 上架/下架/删除
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())){
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE,
                        "商品不是在售状态. " + product.getName());
            }
            //库存是否充足
            if (product.getStock() < cart.getQuantity()){
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR,
                        "库存不正确. " + product.getName());
            }

            //构造订单条目，一个满足上面三个判断的商品就会有一个订单条目
            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0){
                return ResponseVo.error(ResponseEnum.ERROR);
            }

        }

        //计算总价，只计算被选中的商品
        //生成订单，入库: order和order_item, 添加事务:使用@Transactional注解
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);//构造订单

        int rowForOrder = orderMapper.insertSelective(order);//将订单插入数据库订单表
        if (rowForOrder <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);//将订单条目插入数据库订单条目表
        if (rowForOrderItem <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //更新购物车(选中的商品)
        //操作Redis，Redis的事务是多条命令的打包，每个命令操作都是原子性的，不存在回滚(不要在上面的for循环内进行)
        for (Cart cart : cartList) {
            cartService.delete(uid,cart.getProductId());
        }

        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    /**
     * 将指定用户的所有订单罗列成订单列表
     * @param uid 用户id
     * @param pageNum 页码
     * @param pageSize 页内容纳条目数量
     * @return
     */
    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);//构造页面
        List<Order> orderList = orderMapper.selectByUid(uid);//通过传入的用户id查找其所有的订单

        //在该用户所有的订单中提取出所有的订单号，并将这些订单号组成一个集合
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        //根据订单号集合查询这些订单号对应订单中的所有订单条目orderItem
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        //把订单号和其对应的所有订单条目以map映射形式存储
        Map<Long,List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        //在该用户所有的订单中提取出所有的收货地址编号，并将这些收货地址编号组成一个集合
        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        //根据收货地址编号集合查询这些收货地址编号对应所有收货地址shipping
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        //把地址编号和其对应的一个地址以map映射形式存储
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        //构造出该用户所有的orderVo
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo pageInfo = new PageInfo<>(orderList);//设置分页页面信息
        pageInfo.setList(orderVoList);

        return ResponseVo.success(pageInfo);
    }

    /**
     * 查询指定用户指定订单号的订单详情
     * @param uid 用户
     * @param orderNo 订单号
     * @return
     */
    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);//用订单号在数据库中查询该订单
        if (order == null || !order.getUserId().equals(uid)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }

        //查询该订单号对应订单的订单条目
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        //查询该订单的地址信息，通过订单中的地址编号
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());

        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    /**
     * 取消指定用户指定订单号的订单
     * @param uid 用户id
     * @param orderNo 订单号
     * @return
     */
    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);//用订单号在数据库中查询该订单
        if (order == null || !order.getUserId().equals(uid)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //只有[未付款]的订单才可以取消，取决于商城业务
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }
        //设定取消后订单的某些属性:订单状态，关闭时间
        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        //更新数据库中订单状态
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    /**
     * 从MQ种接收到支付成功的消息后，根据消息中的订单号，修改订单状态为已付款
     * @param orderNo
     */
    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单id:" + orderNo);
        }
        //只有[未付款]订单可以变成[已付款]
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc() + "订单id:" + orderNo);
        }

        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            throw new RuntimeException("将订单更新为已支付状态失败，订单id:" + orderNo);
        }
    }

    /**
     * 用于构造orderVo的方法
     * @param order 订单order对象
     * @param orderItemList 订单条目list
     * @param shipping 收货地址
     * @return
     */
    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(orderItemVoList);

        if (shipping != null){
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);//没有专门定义一个ShippingVo类,直接用shipping
        }

        return orderVo;
    }

    /**
     * 生成唯一的订单号:时间戳+随机值
     * 企业级：分布式唯一id/主键
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    /**
     * 构造订单条目对象，一个订单条目对象中只有一种商品
     * @param uid 用户id
     * @param orderNo 订单号
     * @param quantity 商品数量
     * @param product 商品对象，包含该商品的所有信息
     * @return
     */
    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    /**
     * 构造订单对象
     * @param uid 用户id
     * @param orderNo 订单号
     * @param shippingId 收货地址id
     * @param orderItemList 包含多个订单条目对象的列表
     * @return
     */
    private Order buildOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);//计算整个订单的商品总价格

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());//支付方式:在线支付
        order.setPostage(0);//运费，默认0
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());//订单状态:未支付
        return order;
    }
}
