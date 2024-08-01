package com.example.block.dto;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeResposeDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeResultDTO{
        String user1Email;
        String user2Email;
        String message;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteLikeResultDTO{
        User userLiker;
        User userLiked;
        Contest contest;
        String message;
    }

}
