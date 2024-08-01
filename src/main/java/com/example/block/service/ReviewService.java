package com.example.block.service;


import com.example.block.domain.mapping.Review;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.GeneralException;
import com.example.block.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PointService pointService;

    public Review getReviewDetail(Integer userId,Integer reviewId){
        //리뷰 결제 여부 확인
        if (!pointService.isAlreadyPaid(userId,reviewId)) {
            throw new GeneralException(ErrorStatus._NEED_PAY);
        }

        Review review=reviewRepository.findById(reviewId).orElseThrow(
                () -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));

        // 리뷰 반환
        return review;
    }
}
