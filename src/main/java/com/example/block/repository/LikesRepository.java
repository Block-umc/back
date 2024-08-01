package com.example.block.repository;

import com.example.block.domain.mapping.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
    Optional<Likes> findByUserLikerIdAndUserLikedIdAndContestId(Integer userLikerId, Integer userLikedId, Integer contestId);
    List<Likes> findByUserLikerId(Integer userLikerId);
    List<Likes> findByUserLikedId(Integer userLikedId);
    Boolean existsByUserLikerIdAndUserLikedIdAndContestId(Integer userId2,Integer userId1,Integer contestId);


}
