package com.imooc.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {


    UP(0, "销售中"), DOWN(1, "已经下架");
    private int code;
    private String message;

    private ProductStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    }
