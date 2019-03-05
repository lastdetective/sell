package com.imooc.service.impl;

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
import com.imooc.utils.IdGeneratorUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
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

}
