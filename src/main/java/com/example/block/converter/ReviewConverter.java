package com.example.block.converter;


import com.example.block.domain.mapping.Review;
import com.example.block.dto.ReviewRequestDTO;
import com.example.block.dto.ReviewResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {

//    dto - > entity
    public static Review toReview(ReviewRequestDTO.ReviewDTO request){
        return Review.builder()
                .contest(request.getContest())
                .user(request.getUser())
                .content(request.getContent())
                .service(request.getService())
                .prize(request.getPrize())
                .build();
    }
    public static ReviewResponseDTO.ReviewResultDTO toAddReviewResultDTO(Review review){
        return ReviewResponseDTO.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .message("update!")
                .build();
    }
    public static ReviewResponseDTO.ReviewResultDTO toUpdateReviewResultDTO(Review review){
        return ReviewResponseDTO.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .message("update!")
                .build();
    }
    public static ReviewResponseDTO.ViewReviewResultDTO toViewReviewResultDTO(Review review) {
        return ReviewResponseDTO.ViewReviewResultDTO.builder()
                .reviewId(review.getId())
                .userName(review.getUser().getName())
                .prize(review.getPrize())
//                .score(score)
                .build();
    }
    public static ReviewResponseDTO.ViewReviewResultListDTO toViewReviewResultListDTO(List<Review> reviews) {
        List<ReviewResponseDTO.ViewReviewResultDTO> viewReviewResultDTOList = reviews.stream()
                .map(ReviewConverter::toViewReviewResultDTO).collect(Collectors.toList());
        return ReviewResponseDTO.ViewReviewResultListDTO.builder()
                .reviewList(viewReviewResultDTOList)
                .message("view!")
                .build();
    }

    public static ReviewResponseDTO.GetReviewDetailDTO toReviewDetailDTO(Review review) {
        //리뷰 상세 내용 정보 변환
        return ReviewResponseDTO.GetReviewDetailDTO.builder()
                .writer(review.getUser().getName())
                .content(review.getContent())
                .service(review.getService())
                .prize(review.getPrize())
                .createdAt(review.getCreated_at())
                //.score(score)
                .build();

    }
}
