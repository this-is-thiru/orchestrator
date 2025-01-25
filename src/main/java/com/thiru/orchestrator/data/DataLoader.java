package com.thiru.orchestrator.data;

import com.thiru.orchestrator.service.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final ProductService productService;

    @Override
    public void run(String... args) {

        productService.orchestrateProductFetchAndStore();
        log.info("Products loaded from URL");
    }
}
