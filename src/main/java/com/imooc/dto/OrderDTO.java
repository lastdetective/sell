package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.imooc.dataobject.OrderDetail;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class OrderDTO {
    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;

    private BigDecimal orderAmount;

    private Integer orderStatus = OrderStatusEnum.NEW.getStatusCode();

    private Integer payStatus = PayStatusEnum.WAIT.getPayStatusCode();

    private Date createTime;

    private Date updateTime;

    private List<OrderDetail> detailList;
}
