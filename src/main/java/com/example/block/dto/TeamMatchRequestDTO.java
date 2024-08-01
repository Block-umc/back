package com.example.block.dto;

import com.example.block.domain.enums.ApplyPart;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class TeamMatchRequestDTO {

    @Getter
    public static class ApplyDTO {
        @NotNull
        ApplyPart applyPart;
        String content;
    }
}
