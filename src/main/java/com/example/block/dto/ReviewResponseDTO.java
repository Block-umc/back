package com.example.block.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReviewResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewDetailDTO{
        //리뷰 상세 조회 응답 정보
        private String content;
        private Float score;
        private LocalDateTime createdAt;

    }
}
