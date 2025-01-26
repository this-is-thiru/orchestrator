package com.thiru.orchestrator.dto;

import com.thiru.orchestrator.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductsDto {
    List<ProductDto> products;
    int total;
    int skip;
    int limit;
}
