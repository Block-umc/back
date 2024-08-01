package com.example.block.dto;

import lombok.Getter;

public class PointRequestDTO {
    @Getter
    public static class PointCharge{
        private Long point;
        private String reason;
    }

    @Getter
    public static class PointUse{
        private Integer userId;
        private Long point;
    }
}
