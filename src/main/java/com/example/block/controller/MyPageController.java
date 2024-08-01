package com.example.block.controller;

import com.example.block.ApiResponse;

import com.example.block.converter.MyPageConverter;
import com.example.block.converter.PointConverter;
import com.example.block.domain.PointDetail;
import com.example.block.domain.mapping.Applicant;
import com.example.block.dto.MyPageResponseDTO;
import com.example.block.dto.PointRequestDTO;
import com.example.block.dto.PointResponseDTO;
import com.example.block.service.MyPageService;
import com.example.block.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final PointService pointService;
    private final MyPageService myPageService;

    @GetMapping("/{userId}/point")
    @Operation(summary = "내 포인트 조회")
    public ApiResponse<PointResponseDTO.GetMyPointDTO> getPoint(@PathVariable(name="userId") Integer userId) {
        //내 포인트 조회
        return ApiResponse.onSuccess(PointConverter.toPointDTO(pointService.getMyPoint(userId)));
    }

    @GetMapping("/{userId}/pointDetail")
    @Operation(summary = "내 포인트 상세 내역 조회")
    public ApiResponse<PointResponseDTO.GetMyPointDetailListDTO> getPointDetail(@PathVariable(name="userId") Integer userId) {
        //내 포인트 상세 조회 : 최근 5개
        List<PointDetail> pointDetails=pointService.getMyPointDetail(userId);

        return ApiResponse.onSuccess(PointConverter.toPointDetailListDTO(pointDetails));
    }

    @PostMapping("/{userId}/charge")
    @Operation(summary = "포인트 지급")
    public ApiResponse<PointResponseDTO.GetMyPointDetailDTO> chargePoint(@PathVariable(name="userId") Integer userId,
                                                                         @RequestBody PointRequestDTO.PointCharge request) {
        //포인트 충전
        PointDetail point=pointService.chargePoint(userId,request.getPoint());
        return ApiResponse.onSuccess(PointConverter.toPointDetailDTO(point));
    }

    //  내가 좋아요 누른 사람 목록
    //  토큰 구현되면 userId를 토큰 정보로 대체
    @GetMapping("/like")
    public ApiResponse<List<MyPageResponseDTO.likeListResultDTO>> likeList(@RequestParam(name = "userId") Integer userId){

        List<Applicant> applicantList = myPageService.getLikeChallengerList(userId);
        return ApiResponse.onSuccess(MyPageConverter.toLikeListResultDTO(applicantList));
    }

    //  나에게 좋아요를 누른 사람 목록
    //  토큰 구현되면 userId를 토큰 정보로 대체
    @GetMapping("/likeBy")
    public ApiResponse<List<MyPageResponseDTO.likeListResultDTO>> likeByList(@RequestParam(name = "userId") Integer userId){

        List<Applicant> applicantList = myPageService.getLikeByChallengerList(userId);
        return ApiResponse.onSuccess(MyPageConverter.toLikeListResultDTO(applicantList));
    }
}
