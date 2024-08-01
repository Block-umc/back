package com.example.block.controller;


import com.example.block.ApiResponse;
import com.example.block.converter.ReviewConverter;
import com.example.block.domain.mapping.Review;
import com.example.block.dto.ReviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.block.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
//@Tag(name = "개발 전용", description = "test API")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}/{reviewId}")
    @Parameters({
            @Parameter(name="userId",description = "사용자의 id, path variable 입니다"),
            @Parameter(name="reviewId",description = "리뷰의 id, path variable 입니다")
    })
    @Operation(summary = "리뷰 상세 조회")
    public ApiResponse<ReviewResponseDTO.GetReviewDetailDTO> getReviewDetail(@PathVariable(name="userId") Integer userId,
                                                                             @PathVariable(name="reviewId") Integer reviewId)
    {
        //리뷰 상세 조회
        Review review = reviewService.getReviewDetail(userId,reviewId);
        return ApiResponse.onSuccess(ReviewConverter.toReviewDetailDTO(review));
    }
}
