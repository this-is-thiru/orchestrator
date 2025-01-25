package com.thiru.orchestrator.repository;

import com.thiru.orchestrator.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT a FROM Product a WHERE " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :searchText, '%')) ")
    List<Product> findArticlesBySearchText(@Param("searchText") String searchText);

    Optional<Product> findBySku(String sku);
}
