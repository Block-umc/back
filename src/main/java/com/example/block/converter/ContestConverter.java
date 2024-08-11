package com.example.block.converter;

import com.example.block.domain.Contest;
import com.example.block.domain.MyContest;
import com.example.block.domain.User;
import com.example.block.dto.ContestResponseDTO;
import com.example.block.repository.ContestRepository;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;

public class ContestConverter {

    public static ContestResponseDTO.ContestInfoDTO toContestInfoDTO(Contest contest) {
        return ContestResponseDTO.ContestInfoDTO.builder()
                .contestId(contest.getId())
                .contestName(contest.getTitle())
                .contestImage(contest.getImageUrl())
                .contestCategory(contest.getContestCategory())
                .startDate(contest.getStartDate().toString())
                .endDate(contest.getEndDate().toString())
                .HashTag(contest.getHashTag())
                .build();
    }

    public static MyContest toMyContest(Contest contest, User user) {
        return MyContest.builder()
                .contest(contest)
                .user(user)
                .build();
    }
}
