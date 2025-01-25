package com.thiru.orchestrator.service.product;

import com.thiru.orchestrator.dto.ProductsDto;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class ProductFetcher {

    final RestTemplate restTemplate;

    @Async
    @Retry(name = "productFetcherRetry")
    public CompletableFuture<ProductsDto> fetchPageAsync(int skip, int limit) {
        return CompletableFuture.supplyAsync(() -> fetchPage(skip, limit));
    }

    public ProductsDto fetchPage(int skip, int limit) {
        String url = "https://dummyjson.com/products?skip=" + skip + "&limit=" + limit;
        return restTemplate.getForObject(url, ProductsDto.class);
    }
}
