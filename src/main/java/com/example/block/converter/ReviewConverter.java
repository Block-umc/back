package com.example.block.converter;


import com.example.block.domain.mapping.Review;
import com.example.block.dto.ReviewResponseDTO;

public class ReviewConverter {

    public static ReviewResponseDTO.GetReviewDetailDTO toReviewDetailDTO(Review review) {
        //리뷰 상세 내용 정보 변환
        return ReviewResponseDTO.GetReviewDetailDTO.builder()
                .score(review.getScore())
                .content(review.getContent())
                .createdAt(review.getCreated_at())
                .build();

    }
}
