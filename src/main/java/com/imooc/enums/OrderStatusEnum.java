package com.imooc.enums;

import lombok.Getter;
import org.hibernate.criterion.Order;


@Getter
public enum OrderStatusEnum {
    NEW(0, "新订单"),
    FINISHED(1, "完结"),
    CANCEL(2, "已取消");
    private Integer statusCode;
    private String statusMsg;

    private OrderStatusEnum(Integer statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
}
