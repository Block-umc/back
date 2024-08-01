package com.example.block.service;

import com.example.block.domain.mapping.Likes;

public interface LikeService {
    Integer findUser(Integer applicantId, Integer contestId);
    Boolean hasMatched(Integer userId1, Integer userId2, Integer contestId);
    Boolean hasLiked(Integer userId1, Integer userId2, Integer contestId);
    String findEmail(Integer userId);
    void likeUser(Integer userId, Integer applicantId, Integer contestId);
    void match(Integer userId, Integer applicantId, Integer contestId);
    void deleteLike(Integer userId, Integer applicantId, Integer contestId);

}
