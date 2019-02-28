package com.imooc.service;

import com.imooc.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {


    /**
     * @param categoryId
     * @return
     */
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryList);

    ProductCategory save(ProductCategory productCategory);

}
