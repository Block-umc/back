package com.example.block.service.kakao;


import com.example.block.converter.PointConverter;
import com.example.block.domain.enums.PointType;
import com.example.block.domain.mapping.HashTag;
import com.example.block.dto.PointRequestDTO;
import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.GeneralException;
import com.example.block.repository.UserRepository;
import com.example.block.dto.KakaoPayRequestDTO;
import com.example.block.dto.KakaoPayResponseDTO;
import com.example.block.service.AuthService;
import com.example.block.service.PointService;
import com.example.block.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoPayServiceImpl implements KakaoPayService{
    private final UserRepository userRepository;
    private final String cid="TC0ONETIME"; //테스트 cid : TC0ONETIME
    private KakaoPayResponseDTO.KakaoPayReadyResponseDTO kakaoReady;
    private KakaoPayRequestDTO.KakaoPayReadyRequestDTO ready; //정상 결제 시 DB 업데이트를 위해 필요한 정보
    private final AuthService authService;
    private final PointService pointService;
    private final ReviewService reviewService;

    @Value("${kakao-admin-key}")
    private String kakao_admin_key; //어플리케이션 어드민 키

    @Override
    public KakaoPayResponseDTO.KakaoPayReadyResponseDTO kakaoPayReady(KakaoPayRequestDTO.KakaoPayReadyRequestDTO readyDTO) {
        //카카오페이 API에 결제 요청 날리는 과정
        ready=readyDTO;
        //결제 요청에 필요한 정보를 준비한다.
        //최종 결제 금액 계산 : 상품 총액 - 사용한 포인트
        String totalAmount = Long.toString(ready.getAmount() - ready.getUsingPoint());
        //포인트 확인 -> 포인트가 부족하면 예외처리
        if (ready.getUsingPoint() > userRepository.findById(authService.getUserIdFromSecurity()).get().getPoint()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        //이미 결제했던 리뷰인지 확인
        if (reviewService.isAlreadyPaid(authService.getUserIdFromSecurity(),ready.getReviewId())) {
            throw new GeneralException(ErrorStatus._ALREADY_PAID);
        }

        //RequestBody만들기
        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);//가맹점 코드
        params.put("partner_order_id", ready.getPartner_order_id());//가맹점 주문번호
        params.put("partner_user_id", authService.getUserIdFromSecurity().toString());//가맹점 회원 id
        params.put("item_name", ready.getItemName());//상품명
        params.put("quantity", "1");//상품 수량
        params.put("total_amount", totalAmount);//상품 총액
        params.put("tax_free_amount", "0");//상품 비과세 금액
        params.put("approval_url", "http://13.209.114.87:8080/pay/success");//결제 성공시 redirect url
        params.put("cancel_url", "http://13.209.114.87:8080/pay/cancel");//결제 취소시 redirect url
        params.put("fail_url", "http://13.209.114.87:8080/pay/fail");//결제 실패시 redirect url

        //헤더와 바디
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());

        //RestTemplate을 사용하여 카카오페이 API에 요청을 보내고 응답을 받는다.
        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

        kakaoReady = template.postForObject(
                url,
                requestEntity,
                KakaoPayResponseDTO.KakaoPayReadyResponseDTO.class);

        return kakaoReady;

    }

    @Override
    @Transactional
    public KakaoPayResponseDTO.KakaoPayApproveResponseDTO kakaoPayApprove(String pgToken) {
        //카카오페이 API에 결제 승인 요청을 보내는 과정
        //Request Body 만들기
        Map<String,String> params= new HashMap<>();
        params.put("cid", cid);
        params.put("tid", kakaoReady.getTid());
        params.put("partner_order_id", ready.getPartner_order_id());
        params.put("partner_user_id", authService.getUserIdFromSecurity().toString());
        params.put("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoPayResponseDTO.KakaoPayApproveResponseDTO approveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoPayResponseDTO.KakaoPayApproveResponseDTO.class);

        //결제 성공 시 포인트 차감
        //pointService.usePoint(ready.getUserId(), );//서비스 계층에서 다른 서비스를 호출해도 될까?
        //리뷰ID와 유저ID로 transactionReview에 추가
        reviewService.payReview(authService.getUserIdFromSecurity(), ready.getReviewId());
        //포인트 차감
        if(ready.getUsingPoint()!=0)
            pointService.usePoint(authService.getUserIdFromSecurity(), PointConverter.toPointRequestUseDTO(ready), PointType.KAKAOPAYSPEND);

        return approveResponse;
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY "+kakao_admin_key);
        headers.add("Content-Type","application/json");
        return headers;
    }

}
