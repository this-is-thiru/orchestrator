package com.thiru.orchestrator.service.product;

import com.thiru.orchestrator.dto.ProductDto;

import java.util.List;

public interface ProductService {
    void orchestrateProductFetchAndStore();
    List<ProductDto> findArticlesBySearchText(String searchText);
    ProductDto getProductByIdOrSku(String id);
}
