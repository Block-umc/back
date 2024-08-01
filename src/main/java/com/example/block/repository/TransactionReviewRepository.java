package com.example.block.repository;

import com.example.block.domain.User;
import com.example.block.domain.mapping.Review;
import com.example.block.domain.mapping.TransactionReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionReviewRepository extends JpaRepository<TransactionReview, Integer> {
    TransactionReview findAllByUserAndReview(User user, Review review);
}
