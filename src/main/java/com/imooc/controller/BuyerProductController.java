package com.imooc.controller;


import com.imooc.VO.ProductInfoVO;
import com.imooc.VO.ProductVO;
import com.imooc.VO.ResultVO;
import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
@Slf4j
public class BuyerProductController {
    @Autowired
    private ProductService productService;


    @Autowired
    private CategoryService categoryService;


    @GetMapping("/list")
    public ResultVO getProductList() {
        //1. 查询所有上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //2. 查询所有类目（一次性查询）
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(productInfo -> productInfo.getCategoryType())
                .collect(Collectors.toList());

        List<ProductCategory> productCategoryList =
                categoryService.findByCategoryTypeIn(categoryTypeList);

        log.info("这个没有斜杠");
        ResultVO resultVO = new ResultVO();

        ProductVO productVO = new ProductVO();
        productVO.setCategoryName("销售榜中榜");
        productVO.setCategoryType(ProductStatusEnum.UP.getCode());

        ProductInfoVO productInfoVO = new ProductInfoVO();
        productInfoVO.setProductId("123456");
        productInfoVO.setProductName("好吃的焖饭");
        productInfoVO.setProductPrice(new BigDecimal(3.5));
        productInfoVO.setProductIcon("http://2342.sdfa");
        productInfoVO.setProductDescription("这是很好吃的焖饭");

        productVO.setProductInfoVOList(Arrays.asList(productInfoVO));
        resultVO.setData(Arrays.asList(productVO));
        resultVO.setCode(ProductStatusEnum.UP.getCode());
        resultVO.setMsg("查找成功");

        return resultVO;

    }
}
