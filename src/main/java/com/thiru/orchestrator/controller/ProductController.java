package com.thiru.orchestrator.controller;

import com.thiru.orchestrator.dto.ProductDto;
import com.thiru.orchestrator.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @GetMapping("search")
    public List<ProductDto> getProducts(@RequestParam(defaultValue = "") String searchText) {
        if (searchText.length() <= 2) {
            throw new IllegalArgumentException("Search text must be at least 3 characters long");
        }
        return productService.findArticlesBySearchText(searchText);
    }

    @GetMapping("{id}")
    public ProductDto getProduct(@PathVariable String id) {
        return productService.getProductByIdOrSku(id);
    }
}
