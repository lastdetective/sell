package com.imooc.repository;

import com.imooc.dataobject.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductInfoRepositoryTest {
    @Autowired
    private ProductInfoRepository productInfoRepository;



       /* ProductInfo productInfo = new ProductInfo();
        productInfo.setCategoryType(2);
        productInfo.setProductDescription("好吃的sfsdfadf皮蛋粥");
        productInfo.setProductIcon("htsdftp：///");
       // productInfo.setProductId("dfg");
        productInfo.setProductName("好吃sdfsdf的皮蛋粥");
        productInfo.setProductPrice(new BigDecimal(5.1));
        productInfo.setProductStatus("1");
        productInfo.setProductStock(920);
        ProductInfo productInfo2 = productInfoRepository.save(productInfo);
        */


    @Test
    public void saveTest() {
        ProductInfo productInfo = new ProductInfo();
        // productInfo.setProductId("123456");
        productInfo.setProductId("777");
        productInfo.setProductName("皮皮虾");
        productInfo.setProductPrice(new BigDecimal(5.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("皮皮虾");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(1);
        productInfo.setCategoryType(3);
        ProductInfo result = productInfoRepository.save(productInfo);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByProductStatus() throws Exception {

        List<ProductInfo> productInfoList = productInfoRepository.findByProductStatus(0);
        Assert.assertNotEquals(0, productInfoList.size());
    }


    @Test
    public void findByProductStatusTest() {

    }
}
