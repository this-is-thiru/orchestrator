package com.thiru.orchestrator.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Product {

    @Id
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

    @Embedded
    private Dimensions dimensions;

    @ElementCollection
    private List<String> tags;

    @ElementCollection
    private Map<String, String> meta;

    @ElementCollection
    private List<String> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    private String thumbnail;

    @Data
    @Embeddable
    public static class Dimensions {
        private double width;
        private double height;
        private double depth;
    }
}