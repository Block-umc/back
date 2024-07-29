package com.example.block.converter;

import com.example.block.domain.PointDetail;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Review;
import com.example.block.domain.mapping.TransactionReview;
import com.example.block.service.DTO.PointResponseDTO;

public class TransactionReviewConverter {

    public static TransactionReview toTransactionReview(User user, Review review) {
        //TransactionReview Entity 생성 및 반환
        return TransactionReview.builder()
                .user(user)
                .review(review)
                .build();
    }


}
