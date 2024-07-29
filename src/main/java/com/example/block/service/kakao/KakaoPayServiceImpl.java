package com.example.block.service.kakao;


import com.example.block.global.apiPayload.code.status.ErrorStatus;
import com.example.block.global.apiPayload.exception.GeneralException;
import com.example.block.repository.UserRepository;
import com.example.block.service.DTO.KakaoPayRequestDTO;
import com.example.block.service.DTO.KakaoPayResponseDTO;
import com.example.block.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoPayServiceImpl implements KakaoPayService{
    private final UserRepository userRepository;
    private final String cid="TC0ONETIME"; //테스트 cid : TC0ONETIME
    private KakaoPayResponseDTO.KakaoPayReadyResponseDTO kakaoReady;
    private KakaoPayRequestDTO.KakaoPayReadyRequestDTO ready; //정상 결제 시 DB 업데이트를 위해 필요한 정보
    private final PointService pointService;

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
        if (ready.getUsingPoint() > userRepository.findById(ready.getUserId()).get().getPoint()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        //이미 결제했던 리뷰인지 확인
        if (pointService.isAlreadyPaid(ready.getUserId(),ready.getReviewId())) {
            throw new GeneralException(ErrorStatus._ALREADY_PAID);
        }


        //RequestBody만들기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);//가맹점 코드
        params.add("partner_order_id", ready.getPartner_order_id());//가맹점 주문번호
        params.add("partner_user_id", ready.getUserId().toString());//가맹점 회원 id
        params.add("item_name", ready.getItemName());//상품명
        params.add("quantity", "1");//상품 수량
        params.add("total_amount", totalAmount);//상품 총액
        params.add("tax_free_amount", "0");//상품 비과세 금액
        params.add("approval_url", "http://localhost:8080/pay/success");//결제 성공시 redirect url
        params.add("cancel_url", "http://localhost:8080/pay/cancel");//결제 취소시 redirect url
        params.add("fail_url", "http://localhost:8080/pay/fail");//결제 실패시 redirect url

        //헤더와 바디
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());

        //RestTemplate을 사용하여 카카오페이 API에 요청을 보내고 응답을 받는다.
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";

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
        MultiValueMap<String,String> params= new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("tid", kakaoReady.getTid());
        params.add("partner_order_id", ready.getPartner_order_id());
        params.add("partner_user_id", ready.getUserId().toString());
        params.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoPayResponseDTO.KakaoPayApproveResponseDTO approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoPayResponseDTO.KakaoPayApproveResponseDTO.class);

        //결제 성공 시 포인트 차감
        //pointService.usePoint(ready.getUserId(), );//서비스 계층에서 다른 서비스를 호출해도 될까?
        //리뷰ID와 유저ID로 transactionReview에 추가
        pointService.payForReview(ready.getUserId(),ready.getReviewId(),ready.getUsingPoint());

        return approveResponse;
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakao_admin_key);
        headers.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

}
