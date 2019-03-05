package com.imooc.enums;

import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
public enum PayStatusEnum {
    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功");
    private Integer payStatusCode;
    private String payStatusMsg;

    private PayStatusEnum(Integer payStatusCode, String payStatusMsg) {
        this.payStatusCode = payStatusCode;
        this.payStatusMsg = payStatusMsg;
    }

}
