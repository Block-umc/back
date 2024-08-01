package com.example.block.service;

import com.example.block.converter.PointConverter;
import com.example.block.converter.TransactionReviewConverter;
import com.example.block.domain.PointDetail;
import com.example.block.domain.User;
import com.example.block.domain.enums.PointType;
import com.example.block.domain.mapping.Review;
import com.example.block.domain.mapping.TransactionReview;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.GeneralException;
import com.example.block.repository.PointDetailRepository;
import com.example.block.repository.ReviewRepository;
import com.example.block.repository.TransactionReviewRepository;
import com.example.block.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final TransactionReviewRepository transactionReviewRepository;
    private final PointDetailRepository pointDetailRepository;

    @Transactional
    public PointDetail chargePoint(Integer userId, Long point) {
        //포인트 충전
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        //pointDetail 추가
        PointDetail pointDetail = PointConverter.toPointDetail(point,PointType.EARN,"리워드 지급");
        pointDetail.setUser(user);

        updateMyPoint(pointDetail);

        return pointDetail;
    }

    @Transactional
    public PointDetail usePoint(Integer userId, Long point) {
        //포인트 사용
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        //pointDetail 추가 -> 포인트 사용은 -로 표시
        PointDetail pointDetail = PointConverter.toPointDetail(-point,PointType.SPEND,"포인트 사용");
        pointDetail.setUser(user);

        updateMyPoint(pointDetail);

        return pointDetail;
    }

    @Transactional
    public Long getMyPoint(Integer userId) {
        //내 포인트 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        return user.getPoint();
    }


    public List<PointDetail> getMyPointDetail(Integer userId) {
        //내 포인트 상세 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, 5);

        return pointDetailRepository.findTopByUserIdOrderByCreatedAtDesc(userId,pageable);
    }


    @Transactional
    public void payForReview(Integer userId, Integer reviewId, Long point){
        //리뷰 정상 결제 시 transactionReview 추가 및 pointDetail 추가
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        Review review=reviewRepository.findById(reviewId).orElseThrow(
                () -> new GeneralException(ErrorStatus._REVIEW_NOT_FOUND));

        //transactionReview 추가
        TransactionReview transactionReview = TransactionReviewConverter.toTransactionReview(user,review);
        transactionReview.setUserReview(user,review);

        if(point==0){
            return;
        }

        //pointDetail 추가
        PointDetail pointDetail = PointConverter.toPointDetail(-point,PointType.KAKAOPAYSPEND,"공모전 후기 결제");
        pointDetail.setUser(user);

        updateMyPoint(pointDetail);

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
    public void updateMyPoint(PointDetail pointDetail) {
        userRepository.calculateUserPoints(pointDetail.getUser().getId(),pointDetail.getAmount());
    }

}
