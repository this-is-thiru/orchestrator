package com.thiru.orchestrator.service.product;

import com.thiru.orchestrator.dto.ProductDto;
import com.thiru.orchestrator.dto.ProductsDto;
import com.thiru.orchestrator.entity.Product;
import com.thiru.orchestrator.entity.Review;
import com.thiru.orchestrator.exception.NotFoundException;
import com.thiru.orchestrator.repository.ProductRepository;
import com.thiru.orchestrator.service.review.ReviewService;
import com.thiru.orchestrator.utils.TObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                            List<ProductDto> productsDto = response.getProducts();
                            List<Product> products = TObjectMapper.copyCollection(productsDto, Product.class);
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
    public List<ProductDto> findArticlesBySearchText(String searchText) {
        List<Product> products = productRepository.findArticlesBySearchText(searchText);
        return TObjectMapper.copyCollection(products, ProductDto.class);
    }

    @Override
    public ProductDto getProductByIdOrSku(String id) {
        Optional<Product> optionalProduct;
        if (id.matches("\\d+")) {
            optionalProduct = productRepository.findById(Long.parseLong(id));
        } else {
            optionalProduct = productRepository.findBySku(id);
        }

        Product  product = optionalProduct.orElseThrow(() -> new NotFoundException("Product not found"));
        return TObjectMapper.copy(product, ProductDto.class);
    }

    private void saveProduct(Product product) {

        List<Review> productReviews = product.getReviews();
        product.setReviews(new ArrayList<>());
        Product savedAndFlush = productRepository.saveAndFlush(product);

        productReviews.forEach(review -> review.setProduct(savedAndFlush));
        reviewService.saveReviews(productReviews);
    }
}
