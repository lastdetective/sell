package com.imooc.service;

import com.imooc.dto.OrderDTO;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return
     */
    OrderDTO creteOrder(OrderDTO orderDTO);

    /**
     * 查询单个订单
     */
    OrderDTO findOne(String orderId);

    /**
     * 查询订单列表
     */
    Page<OrderDTO> findList(String buyerOpenId, Pageable pageable);

    /**
     * 取消订单
     */
    OrderDTO cancelOrder(OrderDTO orderDTO);


    /**
     * 完结订单
     */
    OrderDTO finishOrder(OrderDTO orderDTO);

    /**
     * 支付订单
     */
    OrderDTO paidOrder(OrderDTO orderDTO);

}
