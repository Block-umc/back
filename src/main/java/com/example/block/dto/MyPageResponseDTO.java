package com.example.block.dto;

import com.example.block.domain.enums.ApplyPart;
import com.example.block.domain.enums.ContestCategory;
import com.example.block.domain.enums.ContestType;
import com.example.block.domain.enums.LoginType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MyPageResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class likeListResultDTO {
        String name;
        String university;
        String major;
        ApplyPart applyPart;
        String contestTitle;
        Integer contestId;
        String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class profileImageDTO {
        String profileImageName;
    }

    // mypage 메인화면에서 활용
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myPageDTO {
        String name;
        String imageUrl;
        String university;
        String major;
        ContestCategory category; // 추후 추가
        LoginType loginType;
    }

    // mypage 프로필 수정에서 사용
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myPageEditDataDTO {
        String userId;
        String  passWord;
        String  birthDay;
        String  phoneNumber;
        String  address;
        String  university;
        String  univMajor;
        String  portfolio;
        ContestCategory  category; // 추후 추가
    }

    // 공모전 목록 조회에서 사용
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contestDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;
        String applyUrl;
        String imageUrl;
        ContestType status;
    }

    // 매칭된 공모전 조회에서 사용
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class matchContestDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;
        String imageUrl;
        String title;
        String startDate;
        String endDate;
        ContestType status;
        String host;
    }
}
