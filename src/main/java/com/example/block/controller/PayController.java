//package com.example.block.controller;
//
//
//import com.example.block.global.apiPayload.code.status.ErrorStatus;
//import com.example.block.global.apiPayload.exception.GeneralException;
//import com.example.block.dto.KakaoPayRequestDTO;
//import com.example.block.dto.KakaoPayResponseDTO;
//import com.example.block.service.PointService;
//import com.example.block.service.kakao.KakaoPayService;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/pay")
////@Tag(name = "개발 전용", description = "test API")
//public class PayController {
//    private final PointService pointService;
//    private final KakaoPayService kakaoPayService;
//
//    @PostMapping("/ready")
//    @Operation(summary = "리뷰 결제 요청")
//    public ResponseEntity getPoint(@RequestBody @Valid KakaoPayRequestDTO.KakaoPayReadyRequestDTO request){
//        //리뷰 결제 요청 처리
//        //성공 시 결제한 리뷰 ID와 결제 날짜를 반환한다
//        KakaoPayResponseDTO.KakaoPayReadyResponseDTO kakaoReady=kakaoPayService.kakaoPayReady(request);
//        return new ResponseEntity<>(kakaoReady, HttpStatus.OK);
//    }
//
//    /**
//     * 결제 성공
//     */
//    @GetMapping("/success")
//    @Operation(summary = "결제 성공")
//    public ResponseEntity afterPayRequest(@RequestParam("pg_token") String pgToken) {
//        KakaoPayResponseDTO.KakaoPayApproveResponseDTO kakaoApprove = kakaoPayService.kakaoPayApprove(pgToken);
//
//        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
//    }
//
//    /**
//     * 결제 진행 중 취소
//     */
//    @GetMapping("/cancel")
//    @Operation(summary = "결제 취소")
//    public void cancel() {
//
//        throw new GeneralException(ErrorStatus._PAY_CANCEL);
//    }
//
//    /**
//     * 결제 실패
//     */
//    @GetMapping("/fail")
//    @Operation(summary = "결제 실패")
//    public void fail() {
//
//        throw new GeneralException(ErrorStatus._PAY_FAIL);
//    }
//
//
//}
