package com.imooc.service.impl;

import com.imooc.converter.OrderMaster2OrderDTOConverter;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.repository.ProductInfoRepository;
import com.imooc.service.OrderService;
import com.imooc.service.ProductService;
import com.imooc.util.IdGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    /**
     * 创建订单
     *
     * @param orderDTO
     * @return
     */
    @Autowired
    private ProductInfoRepository productInfoRepository;


    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    @Override
    public OrderDTO creteOrder(OrderDTO orderDTO) {
        // 1. 计算总价
        BigDecimal productTotal = BigDecimal.ZERO;
        String orderId = IdGeneratorUtil.getUniqueId();
        for (OrderDetail orderDetail : orderDTO.getDetailList()) {
            ProductInfo productInfo = productInfoRepository.
                    findById(orderDetail.getProductId())
                    .orElse(null);
            if (productInfo == null) {
                throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
            }
            BigDecimal productPrice = productInfo.getProductPrice();
            int productQuantity = orderDetail.getProductQuantity();

            productTotal = productPrice.multiply(new BigDecimal(productQuantity)).add(productTotal);
            // 2 .订单详情入库(order_detail)
            orderDetail.setDetailId(IdGeneratorUtil.getUniqueId());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        // 2. 订单主表入库(order_master)
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(productTotal);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getStatusCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getPayStatusCode());
        orderMasterRepository.save(orderMaster);

        // 扣库存
        List<CartDTO> cartDTOList = orderDTO.getDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.decreaseQuantity(cartDTOList);
        return orderDTO;
    }

    /**
     * 查询单个订单
     *
     * @param orderId
     */
    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            // 订单不存在
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            // 订单详情为空
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setDetailList(orderDetailList);
        return orderDTO;
    }

    /**
     * 查询订单列表
     *
     * @param buyerOpenId
     * @param pageable
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> orderMasters =
                orderMasterRepository.findByBuyerOpenid(buyerOpenId, pageable);
        List<OrderDTO> orderDTOList =
                OrderMaster2OrderDTOConverter.convert2List(orderMasters.getContent());
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasters.getTotalElements());
    }

    /**
     * 取消订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO cancelOrder(OrderDTO orderDTO) {

        // 查看订单状态
        OrderMaster result = orderMasterRepository.findById(orderDTO.getOrderId()).orElse(null);
        if (result == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        Integer payStatus = result.getPayStatus();
        Integer orderStatus = result.getOrderStatus();

        if (!orderStatus.equals(OrderStatusEnum.NEW.getStatusCode())) {
            log.error("【取消订单】 订单状态不正确，roderId = {}, orderStatus = {}",
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 如果是新订单
        if (orderStatus == OrderStatusEnum.NEW.getStatusCode()) {
            // 取消订单
            OrderMaster orderMaster = new OrderMaster();
            orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getStatusCode());
            orderDTO.setPayStatus(PayStatusEnum.REFUNDS.getPayStatusCode());
            BeanUtils.copyProperties(orderDTO, orderMaster);
            orderMasterRepository.save(orderMaster);
            // 退库存
            List<OrderDetail> orderDetailList = orderDTO.getDetailList();
            if (CollectionUtils.isEmpty(orderDetailList)) {
                log.error("【去掉订单】，订单中无此商品的信息，orderId = {}", orderDTO.getOrderId());
            }
            List<CartDTO> cartDTOList =
                    orderDTO.getDetailList().stream()
                            .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                            .collect(Collectors.toList());
            productService.increaseQuantity(cartDTOList);

            // 如果已经支付，则退款
            if (payStatus == PayStatusEnum.SUCCESS.getPayStatusCode()) {
                // TODO
            }
            return orderDTO;
        }

        // 返回库存

        // 如果已经支付，则需要退款
        return null;
    }

    /**
     * 完结订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO finishOrder(OrderDTO orderDTO) {

        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getStatusCode())) {
            log.error("【完结订单】订单状态不正确 orderId = {}, orderStatus = {}",
                    orderDTO.getOrderId(),
                    orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getStatusCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null) {
            log.error("【完结订单】订单完结失败 orderId = {}, orderStatus = {}",
                    orderDTO.getOrderId(),
                    orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    /**
     * 支付订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO paidOrder(OrderDTO orderDTO) {
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getStatusCode())) {
            log.error("【支付订单】订单状态不正确 orderId = {}, orderStatus = {}",
                    orderDTO.getOrderId(),
                    orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getPayStatusCode())) {
            log.error("【支付订单】订单支付状态不正确 orderId = {}, payStatus = {}",
                    orderDTO.getOrderId(),
                    orderDTO.getPayStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getPayStatusCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null) {
            log.error("【支付订单】 订单支付失败，orderMaster = {}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

}
