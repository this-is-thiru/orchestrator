package com.thiru.orchestrator.controller;

import com.thiru.orchestrator.entity.Product;
import com.thiru.orchestrator.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("search")
    public List<Product> getProducts(@RequestParam(defaultValue = "") String searchText) {
        return productService.findArticlesBySearchText(searchText);
    }

    @GetMapping("{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProductByIdOrSku(id);
    }
}
