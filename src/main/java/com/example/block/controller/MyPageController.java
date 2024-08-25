package com.example.block.controller;

import com.example.block.ApiResponse;

import com.example.block.converter.MyPageConverter;
import com.example.block.converter.PointConverter;
import com.example.block.domain.MyContest;
import com.example.block.domain.PointDetail;
import com.example.block.domain.User;
import com.example.block.domain.enums.LoginType;
import com.example.block.domain.mapping.Applicant;
import com.example.block.dto.MyPageResponseDTO;
import com.example.block.dto.PointRequestDTO;
import com.example.block.dto.PointResponseDTO;
//import com.example.block.service.ImageService;
import com.example.block.service.AuthService;
import com.example.block.service.ImageService;
import com.example.block.service.MyPageService;
import com.example.block.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final PointService pointService;
    private final MyPageService myPageService;
    private final ImageService imageService;
    private final AuthService authService;


    @PostMapping(value="/myProfileChange",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "내 프로필 이미지 등록,변경,삭제",
            description = "선택파일이 없으면 기존 프로필 이미지를 삭제합니다. 선택파일이 없을 경우 Send empty value 체크해제하고 테스트해주세요. swagger 기본스펙이라 수정이 안됨..")
    public ApiResponse<MyPageResponseDTO.profileImageDTO> changeProfileImage(@RequestPart(value = "file", required = false) MultipartFile image)
    {
        if (image == null || image.isEmpty()) {
            // 내 프로필 이미지 삭제
            return ApiResponse.onSuccess(MyPageConverter.toChangeProfileImageDTO(
                    imageService.deleteImageFromS3(authService.getUserIdFromSecurity())));
        }
        // 내 프로필 이미지 등록, 변경 -> 새로 들어온 이미지로 변경
        return ApiResponse.onSuccess(MyPageConverter.toChangeProfileImageDTO(
                imageService.uploadImageToS3(authService.getUserIdFromSecurity(), image)));
    }


    @GetMapping("/point")
    @Operation(summary = "내 포인트 조회")
    public ApiResponse<PointResponseDTO.GetMyPointDTO> getPoint() {
        //내 포인트 조회
        return ApiResponse.onSuccess(PointConverter.toPointDTO(pointService.getMyPoint(authService.getUserIdFromSecurity())));
    }

    @GetMapping("/pointDetail")
    @Operation(summary = "내 포인트 상세 내역 조회")
    public ApiResponse<PointResponseDTO.GetMyPointDetailListDTO> getPointDetail() {
        //내 포인트 상세 조회 : 최근 5개
        return ApiResponse.onSuccess(PointConverter.toPointDetailListDTO(pointService.getMyPointDetail(authService.getUserIdFromSecurity())));
    }

    @PostMapping("/charge")
    @Operation(summary = "포인트 지급/포인트 상세내역 확인용")
    public ApiResponse<PointResponseDTO.GetMyPointDetailDTO> chargePoint(@RequestBody PointRequestDTO.PointCharge request) {
        //포인트 충전
        PointDetail point=pointService.chargePoint(authService.getUserIdFromSecurity(), request);
        return ApiResponse.onSuccess(PointConverter.toPointDetailDTO(point));
    }

    //  내가 좋아요 누른 사람 목록
    @GetMapping("/like")
    public ApiResponse<List<MyPageResponseDTO.likeListResultDTO>> likeList() {

        Integer userId = authService.getUserIdFromSecurity();
        List<Applicant> applicantList = myPageService.getLikeChallengerList(userId);
        return ApiResponse.onSuccess(MyPageConverter.toLikeListResultDTO(applicantList));
    }

    //  나에게 좋아요를 누른 사람 목록
    @GetMapping("/likeBy")
    public ApiResponse<List<MyPageResponseDTO.likeListResultDTO>> likeByList() {

        Integer userId = authService.getUserIdFromSecurity();
        List<Applicant> applicantList = myPageService.getLikeByChallengerList(userId);
        return ApiResponse.onSuccess(MyPageConverter.toLikeListResultDTO(applicantList));
    }

    // 마이 페이지 메인 화면
    @GetMapping()
    @Operation(summary = "마이 페이지 메인 화면")
    public ApiResponse<MyPageResponseDTO.myPageDTO> getMyPageMain() {
        // 마이 페이지 메인 화면 데이터 조회
        MyPageResponseDTO.myPageDTO user = myPageService.getMyPageUser();

        return ApiResponse.onSuccess(user);
    }

    // 마이 페이지_내 정보 수정
    @GetMapping("/edit")
    @Operation(summary = "마이 페이지_내 정보 수정")
    public ApiResponse<MyPageResponseDTO.myPageDTO> editMyPage() {
        // 마이 페이지 내 정보 수정
        // 기본 정보는 메인 화면과 똑같이 보여주면서 수정 가능한 리스트의 뷰를 보여주므로 같은 메소드 사용
        MyPageResponseDTO.myPageDTO user = myPageService.getMyPageUser();
        return ApiResponse.onSuccess(user);
    }

    // 마이 페이지_내 정보 수정 완료시
    @PostMapping("/edit")
    @Operation(summary = "마이 페이지_내 정보 수정 완료")
    // 수정된 정보를 확인하기 위해 User를 반환함
    public void editMyPageComplete(@RequestBody MyPageResponseDTO.myPageEditDataDTO updatedUser) {
        // 수정된 정보를 저장하고
        myPageService.updateUser(updatedUser);
    }

    // 마이페이지 옵션 내 저장한 공모전, 후기 조회
    @GetMapping("myContestInfo")
    @Operation(summary = "마이페이지 옵션 내 저장한 공모전, 후기 조회")
    public ApiResponse<MyPageResponseDTO.myPageDTO> getMyContest() {
        // 해당 버튼을 가지고 있는 뷰를 보여주면서 메인페이지도 보여줘야하기 때문에 같은 메소드 사용
        MyPageResponseDTO.myPageDTO user = myPageService.getMyPageUser();
        return ApiResponse.onSuccess(user);
    }

    // 마이페이지 옵션 내 저장한 공모전 조회
    @GetMapping("myContests")
    @Operation(summary = "마이페이지 옵션 내 저장한 공모전 조회")
    public ApiResponse<List<MyPageResponseDTO.contestDTO>> getMyContests() {
        // 내가 저장한 공모전 목록 조회
        return ApiResponse.onSuccess(myPageService.getMyContestList());
    }

    @GetMapping("matchedContests")
    @Operation(summary = "마이페이지 옵션 내 매칭된 공모전 조회")
    public ApiResponse<List<MyPageResponseDTO.matchContestDTO>> getMatchedContests() {
        // 내가 매칭된 공모전 목록 조회
        return ApiResponse.onSuccess(myPageService.getMatchedContestList());
    }

}
