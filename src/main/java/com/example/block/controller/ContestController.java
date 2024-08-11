package com.example.block.controller;

import com.example.block.ApiResponse;
import com.example.block.domain.Contest;
import com.example.block.domain.MyContest;
import com.example.block.dto.ContestResponseDTO;
import com.example.block.repository.ContestRepository;
import com.example.block.repository.MyContestRepository;
import com.example.block.service.ContestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contest")
public class ContestController {
    private final ContestService contestService;

    // 공모전 상세 페이지
    @GetMapping("{contestId}")
    @Operation(summary = "공모전 상세 페이지")
    public ApiResponse<ContestResponseDTO.ContestInfoDTO> getContestInfo(@PathVariable Integer contestId) {
        return ApiResponse.onSuccess(contestService.getContestInfo(contestId));
    }


    // 공모전 저장
    @PostMapping("{contestId}/save")
    @Operation(summary = "공모전 저장")
    public ApiResponse<String> saveContest(@PathVariable Integer contestId) {
            return ApiResponse.onSuccess(contestService.saveMyContest(contestId));
    }

    // 공모전 삭제
    @DeleteMapping("{contestId}/delete")
    @Operation(summary = "공모전 삭제")
    public ApiResponse<String> deleteContest(@PathVariable Integer contestId) {
        contestService.deleteMyContest(contestId);

        return ApiResponse.onSuccess("공모전이 삭제되었습니다.");
    }
}
