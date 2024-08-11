package com.example.block.dto;

import com.example.block.domain.enums.ContestType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;
import java.util.Locale;

public class ContestResponseDTO {
    @Builder
    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class ContestInfoDTO {
        private Integer contestId;
        private String contestName;
        private String contestImage;
        private ContestType contestCategory;
        private String startDate;
        private String endDate;
        private String HashTag;
        private String hostUrl;
    }
}
