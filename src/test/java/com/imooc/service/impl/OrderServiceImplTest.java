package com.imooc.service.impl;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * name: "张三"
 * phone: "18868822111"
 * address: "慕课网总部"
 * openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
 * items: [{
 * productId: "1423113435324",
 * productQuantity: 2 //购买数量
 * }]
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class OrderServiceImplTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void createOrderTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerAddress("小寨西路");
        orderDTO.setBuyerOpenid("s8df89df9");
        orderDTO.setBuyerName("张力");
        orderDTO.setBuyerPhone("18919991111");

        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("1231");
        o1.setProductQuantity(4);
        orderDetailList.add(o1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("777");
        o2.setProductQuantity(2);
        orderDetailList.add(o2);

        orderDTO.setDetailList(orderDetailList);

        OrderDTO result = orderService.creteOrder(orderDTO);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindOneTest() throws Exception {
        String orderId = "1551793579391700744";
        OrderDTO result = orderService.findOne(orderId);
        log.info(result.toString());
        Assert.assertNotNull(result);
    }

    @Test
    public void findListTest() {
        PageRequest request = new PageRequest(0, 5);
        Page<OrderDTO> orderDTOPage = orderService.findList("s8df89df9", request);
        log.info(orderDTOPage.toString());
    }

    @Test
    public void cancelTest() {
        OrderDTO orderDTO = orderService.findOne("1552357856296415754");
        OrderDTO result = orderService.cancelOrder(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getStatusCode(), result.getOrderStatus());
        Assert.assertEquals(PayStatusEnum.REFUNDS.getPayStatusCode(), result.getPayStatus());
    }

    @Test
    public void finishTest() {
        OrderDTO orderDTO = orderService.findOne("1552357856296415754");
        OrderDTO result = orderService.finishOrder(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getStatusCode(), result.getOrderStatus());
    }

    @Test
    public void paidTest() {
        OrderDTO orderDTO = orderService.findOne("1552357856296415754");
        OrderDTO result = orderService.paidOrder(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getPayStatusCode(), orderDTO.getPayStatus());
    }
}
