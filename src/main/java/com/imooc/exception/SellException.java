package com.imooc.exception;

import com.imooc.enums.ResultEnum;

public class SellException extends RuntimeException {
    private Integer errorCode;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.errorCode = resultEnum.getResultCode();
    }

    public SellException(Integer errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

}
