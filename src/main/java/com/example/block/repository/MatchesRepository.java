package com.example.block.repository;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Matches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchesRepository extends JpaRepository<Matches, Integer> {

    @Query(value = "SELECT CASE WHEN m.user_id1 = :userId THEN m.user_id2 ELSE m.user_id1 END as matched_user_id " +
            "FROM matches m WHERE m.contest_id = :contestId AND (m.user_id1 = :userId OR m.user_id2 = :userId)",
            nativeQuery = true)
    List<Integer> findMatchedUsersByUserIdAndContestId(@Param("userId") Integer userId, @Param("contestId") Integer contestId);

    Boolean existsByUser1AndUser2AndContest(User user1, User user2, Contest contest);
}
