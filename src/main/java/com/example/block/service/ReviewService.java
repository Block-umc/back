package com.example.block.service;


import com.example.block.converter.ReviewConverter;
import com.example.block.converter.TransactionReviewConverter;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Likes;
import com.example.block.domain.mapping.Review;
import com.example.block.domain.mapping.TransactionReview;
import com.example.block.dto.ReviewRequestDTO;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.GeneralException;
import com.example.block.global.apiPayload.exception.handler.LikeHandler;
import com.example.block.repository.ReviewRepository;
import com.example.block.repository.TransactionReviewRepository;
import com.example.block.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TransactionReviewRepository transactionReviewRepository;

    public Review getReviewDetail(Integer userId,Integer reviewId, Integer contestId){
        //리뷰 상세 내역 출력
        //리뷰ID가 현재 넘겨받은 공모전에 속한 리뷰인지 확인
        //해당 공모전에 속한 리뷰가 아닐 경우 리뷰를 찾을 수 없다는 에러를 반환
        if (!reviewRepository.existsByIdAndContestId(reviewId,contestId)){
            throw new GeneralException(ErrorStatus._REVIEW_NOT_FOUND);
        }
        //결제가 되어있지 않을 경우 결제가 필요하다는 에러를 반환
        if (!isAlreadyPaid(userId,reviewId)) {
            throw new GeneralException(ErrorStatus._NEED_PAY);
        }

        Review review=reviewRepository.findById(reviewId).orElseThrow(
                () -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));

        // 리뷰 반환
        return review;
    }

    @Transactional
    public boolean isAlreadyPaid(Integer userId, Integer reviewId) {
        //이미 결제한 리뷰인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        Review review=reviewRepository.findById(reviewId).orElseThrow(
                () -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));

        TransactionReview transactionReview = transactionReviewRepository.findAllByUserAndReview(user,review);

        return transactionReview != null;
    }

    @Transactional
    public void payReview(Integer userId, Integer reviewId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        //리뷰 결제
        Review review=reviewRepository.findById(reviewId).orElseThrow(
                () -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));
        //transactionReview 추가
        TransactionReview transactionReview = TransactionReviewConverter.toTransactionReview(user,review);
        transactionReview.setUserReview(user,review);
    }

    public Review addReview(ReviewRequestDTO.ReviewDTO request){
        Optional<Review> optionalReview = reviewRepository.findByUserIdAndContestId(request.getUser().getId(),request.getContest().getId());
        if(optionalReview.isPresent()){
            throw new GeneralException(ErrorStatus._REVIEW_ALREADY_EXIST);
        }
        else{
            Review newReview = ReviewConverter.toReview(request);
            return reviewRepository.save(newReview);
        }
    }
    public Review updateReview(Integer reviewId, ReviewRequestDTO.ReviewDTO request){
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));
        existingReview.setService(request.getService());
        existingReview.setContent(request.getContent());
        existingReview.setPrize(request.getPrize());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Integer reviewId){
        Optional<Review> optionalReview = reviewRepository.findByReivewId(reviewId);
        if(optionalReview.isPresent()){
            reviewRepository.delete(optionalReview.get());
        }
        else {
            throw new GeneralException(ErrorStatus._REVIEW_NOT_FOUND);
        }
    }
    public List<Review> viewReviewList(Integer contestId){
        return reviewRepository.findByContestId(contestId);
    }
}
