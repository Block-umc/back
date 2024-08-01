package com.example.block.converter;

import com.example.block.domain.User;
import com.example.block.domain.mapping.Applicant;
import com.example.block.dto.TeamMatchResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TeamMatchConverter {

    public static List<TeamMatchResponseDTO.ChallengerListResultDTO> toChallengerListResultDTO(List<Applicant> applicantList){

        return applicantList.stream()
                .map(applicant -> TeamMatchResponseDTO.ChallengerListResultDTO.builder()
                        .id(applicant.getId())
                        .name(applicant.getUser().getName())
                        .university(applicant.getUser().getUniversity())
                        .applyPart(applicant.getApplyPart())
                        .build()).collect(Collectors.toList());
    }

    public static TeamMatchResponseDTO.ChallengerResultDTO toChallengerResultDTO(Applicant challenger, Boolean hasUserLiked){

        return TeamMatchResponseDTO.ChallengerResultDTO.builder()
                .name(challenger.getUser().getName())
                .university(challenger.getUser().getUniversity())
                .major(challenger.getUser().getUnivMajor())
                .portfolio(challenger.getUser().getPortfolio())
                .applyPart(challenger.getApplyPart())
                .content(challenger.getContent())
                .liked(hasUserLiked)
                .build();
    }

    public static List<TeamMatchResponseDTO.MemberResultDTO> toMemberResultDTO(List<User> memberList){
        return memberList.stream()
                .map(member -> TeamMatchResponseDTO.MemberResultDTO.builder()
                        .name(member.getName())
                        .university(member.getUniversity())
                        .build()).collect(Collectors.toList());
    }
}
