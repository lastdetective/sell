package com.imooc.service.impl;

import com.imooc.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    public void findOne() {
        ProductCategory productCategory = categoryServiceImpl.findOne(1);
        Assert.assertEquals("西红柿", productCategory.getCategoryName());
    }

    @Test
    public void findAll() {
        List<ProductCategory> productCategoryList = categoryServiceImpl.findAll();
        Assert.assertEquals(2, productCategoryList.size());
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> categoryIdList = Arrays.asList(11, 22);
        List<ProductCategory> productCategoryList = categoryServiceImpl.findByCategoryTypeIn(categoryIdList);
        Assert.assertEquals("西红柿", productCategoryList.get(0).getCategoryName());
    }

    @Test
    public void save() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryType(1321);
        productCategory.setCategoryName("火龙果");
        ProductCategory result = categoryServiceImpl.save(productCategory);
        Assert.assertNotNull(result);

    }
}