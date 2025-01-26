package com.thiru.orchestrator.controller;

import com.thiru.orchestrator.dto.ProductDto;
import com.thiru.orchestrator.service.product.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;

    @AfterEach
    void tearDown() {
        productController = null;
        productService = null;
    }

    @Test
    void getProducts() {
        ProductDto productDto = new ProductDto();
        List<ProductDto> productDtos = List.of(productDto);
        Mockito.when(productService.findArticlesBySearchText(Mockito.anyString())).thenReturn(productDtos);
        List<ProductDto> products = productController.getProducts("test");
        Assertions.assertEquals(1, products.size());
        Mockito.verify(productService, Mockito.times(1)).findArticlesBySearchText(Mockito.anyString());


        Assertions.assertThrows(IllegalArgumentException.class, () -> productController.getProducts(""));
    }

    @Test
    void getProduct() {
        ProductDto productDto = new ProductDto();
        Mockito.when(productService.getProductByIdOrSku(Mockito.anyString())).thenReturn(productDto);
        ProductDto product = productController.getProduct("test");
        Assertions.assertEquals(productDto, product);
        Mockito.verify(productService, Mockito.times(1)).getProductByIdOrSku(Mockito.anyString());
    }
}