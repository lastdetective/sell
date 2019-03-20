package com.imooc.form;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;


@Data
public class OrderForm {

    /**
     * 用户姓名
     */
    @NotEmpty(message = "用户姓名不能为空")
    private String name;
    /**
     * 用户的手机号
     */
    @NotEmpty(message = "用户手机号不能为空")
    private String phone;
    /**
     * 用户的地址
     */
    @NotEmpty(message = "用户地址不能为空")
    private String address;
    /**
     * 用户微信的openid
     */
    @NotEmpty(message = "用户微信openid不能为空")
    private String openid;
    /**
     * 用户订单详情
     */
    @NotEmpty(message = "订单地址不能为空")
    private String items;

}
