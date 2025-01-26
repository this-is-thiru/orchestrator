package com.thiru.orchestrator.service.review;

import com.thiru.orchestrator.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @InjectMocks
    ReviewServiceImpl reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Test
    void saveReviews() {
        Mockito.when(reviewRepository.saveAllAndFlush(Mockito.anyList())).thenReturn(new ArrayList<>());
        reviewService.saveReviews(new ArrayList<>());
        Mockito.verify(reviewRepository, Mockito.times(1)).saveAllAndFlush(Mockito.anyList());
    }
}