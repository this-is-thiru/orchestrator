package com.thiru.orchestrator.service.product;

import com.thiru.orchestrator.entity.Product;

import java.util.List;

public interface ProductService {
    void orchestrateProductFetchAndStore();
    List<Product> findArticlesBySearchText(String searchText);
    Product getProductByIdOrSku(String id);
}
