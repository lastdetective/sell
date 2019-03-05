package com.imooc.repository;

import com.imooc.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void findByOrderId() {
        OrderDetail orderDetail = OrderDetail.builder()
                .detailId("1111")
                .orderId("1231")
                .productIcon("http://123")
                .productId("1dwss")
                .productName("油条")
                .productPrice(new BigDecimal(43.2))
                .productQuantity(98).build();

        OrderDetail result = orderDetailRepository.save(orderDetail);
        Assert.assertNotNull(result);

    }
}
