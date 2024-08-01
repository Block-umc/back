package com.example.block.service;

import com.example.block.domain.mapping.Applicant;
import com.example.block.domain.mapping.Likes;
import com.example.block.repository.ApplicantRepository;
import com.example.block.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final LikesRepository likesRepository;
    private final ApplicantRepository applicantRepository;

    public Applicant getChallenger(Integer contestId, Integer userId){
        return applicantRepository.findByContestIdAndUserId(contestId, userId).get();
    }

    //  내가 좋아요 누른 사람 목록
    public List<Applicant> getLikeChallengerList(Integer userId){
        List<Likes> likeList = likesRepository.findByUserLikerId(userId);

        return likeList.stream()
                .map(like -> getChallenger(like.getContest().getId(), like.getUserLiked().getId()))
                .collect(Collectors.toList());
    }

    //  나에게 좋아요를 누른 사람 목록
    public List<Applicant> getLikeByChallengerList(Integer userId){
        List<Likes> likedList = likesRepository.findByUserLikedId(userId);

        return likedList.stream()
                .map(like -> getChallenger(like.getContest().getId(), like.getUserLiker().getId()))
                .collect(Collectors.toList());
    }
}
