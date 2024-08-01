package com.example.block.converter;

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
                        .build()
                ).collect(Collectors.toList());
    }
}
