package com.example.block.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class KakaoPayRequestDTO {

    @Getter
    public static class KakaoPayReadyRequestDTO {
        @NotNull
        private Integer userId;//일단 body로 가져오기

        @NotNull
        private Integer reviewId;

        @NotNull(message = "상품명을 입력해주세요")
        private String itemName ;

        @NotNull(message = "결제할 금액을 입력해주세요")
        private Long amount;

        @NotNull(message = "사용할 포인트를 입력해주세요")
        private Long usingPoint;

        @NotNull(message = "주문번호")
        private String partner_order_id;

    }


}
