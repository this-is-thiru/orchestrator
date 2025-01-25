package com.thiru.orchestrator.service.product;

import com.thiru.orchestrator.dto.ProductsDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductFetcherTest {

    @InjectMocks
     ProductFetcher productFetch;

    @Mock
    RestTemplate restTemplate;

    @AfterEach
    public void tearDown() {
        productFetch = null;
        restTemplate = null;
    }

    @Test
    public void testFetchPageAsync_Success() throws Exception {
        int skip = 0;
        int limit = 10;
        ProductsDto mockResponse = ProductsDto.builder().products(new ArrayList<>()).total(0).skip(0).limit(0).build();
        when(restTemplate.getForObject(anyString(), eq(ProductsDto.class))).thenThrow(new RuntimeException("Error")).thenReturn(mockResponse);

        CompletableFuture<ProductsDto> result = productFetch.fetchPageAsync(skip, limit);

        assertNotNull(result);
        assertFalse(result.isDone());

        productFetch.fetchPageAsync(skip, limit);
        verify(restTemplate, times(2)).getForObject(anyString(), eq(ProductsDto.class));
    }
}
