package com.imooc.repository;

import com.imooc.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest() {
        ProductCategory productCategory =
                repository.findById(2).orElse(null);
        log.info(productCategory.toString());

    }

    @Test
    public void saveTest() {
        ProductCategory productCategory = repository.findById(2).orElse(null);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setCategoryType(221);
        productCategory2.setCategoryName("我的名字");
        repository.save(productCategory2);

    }

    @Test
    public void findByCategoryTypeInTest() {
        List<Integer> categoryTypeList = Arrays.asList(11, 52);
        List<ProductCategory> productCategoryList = repository.findByCategoryTypeIn(categoryTypeList);
        Assert.assertNotEquals(0,categoryTypeList.size());
    }

}