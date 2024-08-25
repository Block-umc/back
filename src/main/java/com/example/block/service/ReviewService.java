package com.example.block.service;


import com.example.block.converter.ReviewConverter;
import com.example.block.converter.TransactionReviewConverter;
import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Review;
import com.example.block.domain.mapping.ReviewAverageScore;
import com.example.block.domain.mapping.TransactionReview;
import com.example.block.dto.ReviewRequestDTO;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.GeneralException;
import com.example.block.global.apiPayload.exception.handler.RatingHandler;
import com.example.block.repository.*;
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
    private final ContestRepository contestRepository;
    private final TransactionReviewRepository transactionReviewRepository;
    private final ReviewAverageScoreRepository reviewAverageScoreRepository;

    public Review getReviewDetail(Integer userId,Integer reviewId, Integer contestId){
        //리뷰 상세 내역 출력
        //리뷰ID가 현재 넘겨받은 공모전에 속한 리뷰인지 확인
        //해당 공모전에 속한 리뷰가 아닐 경우 리뷰를 찾을 수 없다는 에러를 반환
        if (!reviewRepository.existsByIdAndContestId(reviewId,contestId)){
            throw new GeneralException(ErrorStatus._REVIEW_NOT_FOUND);
        }

        Review review=reviewRepository.findById(reviewId).orElseThrow(
                () -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));
        //자신이 작성한 리뷰인지 확인
        if(review.getUser().getId().equals(userId)){
            return review;
        }
        //결제가 되어있지 않을 경우 결제가 필요하다는 에러를 반환
        /*if (!isAlreadyPaid(userId,reviewId)) {
            throw new GeneralException(ErrorStatus._NEED_PAY);
        }*/
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
    @Transactional
    public Review addReview(Integer userId, Integer contestId, ReviewRequestDTO.ReviewDTO request){
        Optional<Review> optionalReview = reviewRepository.findByUserIdAndContestId(userId, contestId);
        if(optionalReview.isPresent()){
            throw new GeneralException(ErrorStatus._REVIEW_ALREADY_EXIST);
        }
        else{
            User user = userRepository.findById(userId).get();
            Contest contest = contestRepository.findById(contestId).get();
            Review newReview = ReviewConverter.toReview(request, user, contest);
            return reviewRepository.save(newReview);
        }
    }
    @Transactional
    public Review updateReview(Integer userId, Integer reviewId, ReviewRequestDTO.ReviewDTO request){
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));
        if(existingReview.getUser().getId() == userId){
            existingReview.setService(request.getService());
            existingReview.setContent(request.getContent());
            existingReview.setPrize(request.getPrize());
            return reviewRepository.save(existingReview);
        }
        else{
            throw new GeneralException(ErrorStatus.ACCESS_DENIED);
        }
    }
    @Transactional
    public void deleteReview(Integer userId, Integer reviewId){
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));
        if(existingReview.getUser().getId() == userId){
            reviewRepository.delete(existingReview);
        }
        else{
            throw new GeneralException(ErrorStatus.ACCESS_DENIED);
        }
    }
    public List<Review> viewReviewList(Integer contestId){
        return reviewRepository.findByContestId(contestId);
    }

    @Transactional
    public void rateReview(Integer userId, Integer reviewId, ReviewRequestDTO.RateReviewDTO request){
//        내가 평가한 리뷰인지 체크하는 값
        Optional<ReviewAverageScore> optionalReviewAverageScore = reviewAverageScoreRepository.findByUserIdAndReviewId(userId, reviewId);
        if(optionalReviewAverageScore.isPresent()){
            throw new RatingHandler(ErrorStatus.RATING_ALREADY_COMPLETED);
        }
        else{
            User user = userRepository.findById(userId).get();
            Review review = reviewRepository.findById(reviewId).get();
            ReviewAverageScore newReviewAverageScore = ReviewConverter.toReviewAverageScore(request, user, review);
            reviewAverageScoreRepository.save(newReviewAverageScore);
        }
    }
}
