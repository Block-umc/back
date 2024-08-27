package com.example.block.dto;

import com.example.block.domain.Contest;
import com.example.block.domain.enums.ContestCategory;
import lombok.*;

import java.util.List;

public class HomeRequestDTO {

    @Builder
    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HomePageRequestDTO {
        private String userName;
        private String userImageUrl;
        private List<HomeContestDTO> contestList;
    }

    @Builder
    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HomeContestDTO {
        private Integer contestId;
        private String contestName;
        private String contestHost;
        private String contestCategory;
        private String contestImageUrl;
        private String applyUrl;
    }
}
