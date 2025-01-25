package com.thiru.orchestrator.service.product;

import com.thiru.orchestrator.dto.ProductsDto;
import com.thiru.orchestrator.entity.Product;
import com.thiru.orchestrator.repository.ProductRepository;
import com.thiru.orchestrator.service.review.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ReviewService reviewService;

    @Mock
    ProductFetcher productFetcher;

    @AfterEach
    public void tearDown() {
        productService = null;
        productRepository = null;
        reviewService = null;
        productFetcher = null;
    }

    @Test
    void orchestrateProductFetchAndStore() {
        ProductsDto productsDtoTotal = ProductsDto.builder().products(new ArrayList<>()).total(194).skip(0).limit(0).build();
        ProductsDto productsDto = ProductsDto.builder().products(new ArrayList<>()).total(194).skip(0).limit(0).build();
        Product product = Mockito.mock(Product.class);
        ProductsDto productsDto1 = ProductsDto.builder().products(List.of(product)).total(194).skip(0).limit(0).build();

        Mockito.when(productFetcher.fetchPageAsync(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(CompletableFuture.completedFuture(productsDtoTotal))
                .thenReturn(CompletableFuture.completedFuture(productsDto))
                .thenReturn(CompletableFuture.completedFuture(productsDto))
                .thenReturn(CompletableFuture.completedFuture(productsDto))
                .thenReturn(CompletableFuture.completedFuture(productsDto1));

        productService.orchestrateProductFetchAndStore();
        Mockito.verify(productFetcher, Mockito.times(5)).fetchPageAsync(Mockito.anyInt(), Mockito.anyInt());


        Mockito.when(productFetcher.fetchPageAsync(Mockito.anyInt(), Mockito.anyInt()))
                .thenAnswer(invocation -> CompletableFuture.failedStage(new RuntimeException("Error")));
        productService.orchestrateProductFetchAndStore();
        Mockito.verify(productFetcher, Mockito.times(6)).fetchPageAsync(Mockito.anyInt(), Mockito.anyInt());

        Mockito.when(productFetcher.fetchPageAsync(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(CompletableFuture.completedFuture(productsDtoTotal))
                .thenAnswer(invocation -> CompletableFuture.failedStage(new RuntimeException("Error")));
        productService.orchestrateProductFetchAndStore();
        Mockito.verify(productFetcher, Mockito.times(11)).fetchPageAsync(Mockito.anyInt(), Mockito.anyInt());

    }

    @Test
    void findArticlesBySearchText() {
        Mockito.when(productRepository.findArticlesBySearchText(Mockito.anyString())).thenReturn(new ArrayList<>());
        List<Product> products = productService.findArticlesBySearchText("test");
        Assertions.assertEquals(0, products.size());
        Mockito.verify(productRepository, Mockito.times(1)).findArticlesBySearchText(Mockito.anyString());
    }

    @Test
    void getProductByIdOrSku() {
        Product mockProduct = Mockito.mock(Product.class);
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockProduct));
        Product product = productService.getProductByIdOrSku("1");
        Assertions.assertNotNull(product);
        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.when(productRepository.findBySku(Mockito.anyString())).thenReturn(Optional.of(mockProduct));
        product = productService.getProductByIdOrSku("1dsf");
        Assertions.assertNotNull(product);
        Mockito.verify(productRepository, Mockito.times(1)).findBySku(Mockito.anyString());
    }
}