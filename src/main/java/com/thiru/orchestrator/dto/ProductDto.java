package com.thiru.orchestrator.dto;

import com.thiru.orchestrator.entity.Review;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ProductDto {

    private Long id;

    private String title;

    private String description;

    private String category;

    private Double price;

    private Double discountPercentage;

    private Double rating;

    private Integer stock;

    private String brand;

    private String sku;

    private Double weight;

    private String warrantyInformation;

    private String shippingInformation;

    private String availabilityStatus;

    private String returnPolicy;

    private Integer minimumOrderQuantity;

    private Dimensions dimensions;

    private List<String> tags;

    private Map<String, String> meta;

    private List<String> images;

    private List<Review> reviews = new ArrayList<>();

    private String thumbnail;

    @Data
    public static class Dimensions {
        private double width;
        private double height;
        private double depth;
    }
}