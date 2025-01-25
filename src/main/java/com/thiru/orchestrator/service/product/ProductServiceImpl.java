package com.thiru.orchestrator.service.product;

import com.thiru.orchestrator.dto.ProductsDto;
import com.thiru.orchestrator.entity.Product;
import com.thiru.orchestrator.entity.Review;
import com.thiru.orchestrator.exception.NotFoundException;
import com.thiru.orchestrator.repository.ProductRepository;
import com.thiru.orchestrator.service.review.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final int PRODUCTS_COUNT = 50;
    private final ProductRepository productRepository;
    private final ReviewService reviewService;
    private final ProductFetcher productFetcher;

    @Override
    public void orchestrateProductFetchAndStore() {
        CompletableFuture<ProductsDto> firstPageFuture = productFetcher.fetchPageAsync(0, PRODUCTS_COUNT);

        firstPageFuture.thenAccept(firstPageResponse -> {
            int totalRecords = firstPageResponse.getTotal();
            int totalPages = (int) Math.ceil((double) totalRecords / PRODUCTS_COUNT);

            IntFunction<CompletableFuture<Void>> fetchAndStorePage = page -> {
                int skip = page * PRODUCTS_COUNT;
                return productFetcher.fetchPageAsync(skip, PRODUCTS_COUNT)
                        .thenAccept(response -> {
                            List<Product> products = response.getProducts();
                            products.forEach(this::saveProduct);
                        })
                        .exceptionally(ex -> {
                            log.error("Error fetching page {}", page, ex);
                            return null;
                        });
            };

            List<CompletableFuture<Void>> futures = IntStream.range(0, totalPages)
                    .mapToObj(fetchAndStorePage)
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenRun(() -> log.info("All products have been fetched and stored successfully."))
                    .exceptionally(ex -> {
                        log.error("Error completing fetch and store process", ex);
                        return null;
                    });
        }).exceptionally(ex -> {
            log.error("Error fetching first page", ex);
            return null;
        });
    }


    @Override
    public List<Product> findArticlesBySearchText(String searchText) {
        return productRepository.findArticlesBySearchText(searchText);
    }

    @Override
    public Product getProductByIdOrSku(String id) {
        if (id.matches("\\d+")) {
            return productRepository.findById(Long.parseLong(id)).orElseThrow(() -> new NotFoundException("Product not found"));
        } else {
            return productRepository.findBySku(id).orElseThrow(() -> new NotFoundException("Product not found"));
        }
    }

    private void saveProduct(Product product) {

        List<Review> productReviews = product.getReviews();
        product.setReviews(new ArrayList<>());
        Product savedAndFlush = productRepository.saveAndFlush(product);

        productReviews.forEach(review -> review.setProduct(savedAndFlush));
        reviewService.saveReviews(productReviews);
    }
}
