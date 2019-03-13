package com.imooc.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;

import java.util.ArrayList;
import java.util.List;

public class OrderForm2OrderDTOConverter {
    public static OrderDTO convert(OrderForm orderForm) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        try {

            // 这种写法 orderDetailList 的元素类型是 JSONObject 而不是 OrderDetail

            /*List<OrderDetail> orderDetailList =
                    JSON.parseObject(orderForm.getItems(),
                            new ArrayList<OrderDetail>().getClass());*/
            // 这种写法将 String 直接映射为元素类型为 OrderDetail 的 List

            List<OrderDetail> orderDetailList =
                    JSON.parseObject(orderForm.getItems(), new TypeReference<ArrayList<OrderDetail>>() {
                    });
            System.out.println(orderDetailList.get(0).getClass().toString());
            orderDTO.setDetailList(orderDetailList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SellException(ResultEnum.PARAM_ERROR);

        }
        return orderDTO;
    }

    public static void main(String[] args) {
        String s = "{name: \"张三\",\n" +
                "phone: \"18868822111\",\n" +
                "address: \"慕课网总部\",\n" +
                "openid: \"ew3euwhd7sjw9diwkq\", \n" +
                "items: [{\n" +
                "    productId: \"1423113435324\",\n" +
                "    productQuantity: 2\n" +
                "}]}";

        JSONObject object = JSON.parseObject(s);
        OrderForm orderForm = new OrderForm();
        orderForm.setAddress(object.getString("address"));
        orderForm.setItems(object.getString("items"));
        orderForm.setOpenid(object.getString("openid"));
        orderForm.setPhone(object.getString("phone"));
        orderForm.setName(object.getString("name"));

        convert(orderForm);

    }
}
