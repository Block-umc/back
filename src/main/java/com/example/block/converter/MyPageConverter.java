package com.example.block.converter;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Applicant;
import com.example.block.dto.MyPageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MyPageConverter {

    public static List<MyPageResponseDTO.likeListResultDTO> toLikeListResultDTO(List<Applicant> applicantList){
        return applicantList.stream()
                .map(applicant -> MyPageResponseDTO.likeListResultDTO.builder()
                        .name(applicant.getUser().getName())
                        .university(applicant.getUser().getUniversity())
                        .major(applicant.getUser().getUnivMajor())
                        .applyPart(applicant.getApplyPart())
                        .contestTitle(applicant.getContest().getTitle())
                        .contestId(applicant.getContest().getId())
                        .profileImage(applicant.getUser().getImageUrl())
                        .build()
                ).collect(Collectors.toList());
    }

    public static MyPageResponseDTO.profileImageDTO toChangeProfileImageDTO(String fileName) {
        return MyPageResponseDTO.profileImageDTO.builder()
                .profileImageName(fileName)
                .build();
    }

    public static MyPageResponseDTO.myPageDTO toMyPageDTO(User user) {
        return MyPageResponseDTO.myPageDTO.builder()
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .university(user.getUniversity())
                .major(user.getUnivMajor())
                .category(user.getInterestCategory())
                .loginType(user.getLoginType())
                .build();
    }

    public static MyPageResponseDTO.myPageEditDataDTO toMyPageEditDataDTO(User user){
        return MyPageResponseDTO.myPageEditDataDTO.builder()
                .userId(user.getUserId())
                .passWord(user.getPassWord())
                .birthDay(user.getBirthDay())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .university(user.getUniversity())
                .univMajor(user.getUnivMajor())
                .portfolio(user.getPortfolio())
                .category(user.getInterestCategory())
                .build();
    }

    public static MyPageResponseDTO.contestDTO toMyContestDTO(Contest contest){
        return MyPageResponseDTO.contestDTO.builder()
                .id(contest.getId())
                .imageUrl(contest.getImageUrl())
                .applyUrl(contest.getApplyUrl())
                .status(contest.getContestType())
                .build();
    }

    public static MyPageResponseDTO.matchContestDTO toMatchContest(Contest contest) {
        return MyPageResponseDTO.matchContestDTO.builder()
                .id(contest.getId())
                .imageUrl(contest.getImageUrl())
                .applyUrl(contest.getApplyUrl())
                .status(contest.getContestType())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .title(contest.getTitle())
                .host(contest.getHost())
                .build();
    }
}
