package com.thiru.orchestrator.service.review;

import com.thiru.orchestrator.entity.Review;
import com.thiru.orchestrator.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public void saveReviews(List<Review> reviews) {
        reviewRepository.saveAllAndFlush(reviews);
    }
}
