package com.imooc.service;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductInfo findOne(String productId);

    /**
     * 查询所有在架商品
     */
    List<ProductInfo> findUpAll();

    Page findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    // 减库存
    void decreaseQuantity(List<CartDTO> cartDTOList);
    // 增加存

}
