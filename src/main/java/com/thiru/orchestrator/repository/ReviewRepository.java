package com.thiru.orchestrator.repository;

import com.thiru.orchestrator.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
