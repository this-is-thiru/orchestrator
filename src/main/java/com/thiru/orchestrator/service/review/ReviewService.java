package com.thiru.orchestrator.service.review;

import com.thiru.orchestrator.entity.Review;

import java.util.List;

public interface ReviewService {
    void saveReviews(List<Review> reviews);
}
