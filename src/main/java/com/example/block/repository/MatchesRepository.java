package com.example.block.repository;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.mapping.Matches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchesRepository extends JpaRepository<Matches, Integer> {

    @Query(value = "SELECT CASE WHEN m.user_id_1 = :userId THEN m.user_id_2 ELSE m.user_id_1 END as matched_user_id " +
            "FROM matches m WHERE m.contest_id = :contestId AND (m.user_id_1 = :userId OR m.user_id_2 = :userId)",
            nativeQuery = true)
    List<Integer> findMatchedUsersByUserIdAndContestId(@Param("userId") Integer userId, @Param("contestId") Integer contestId);

    Boolean existsByUser1AndUser2AndContest(User user1, User user2, Contest contest);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Matches m WHERE m.contest.id = :contestId AND ((m.user1.id = :userId1 AND m.user2.id = :userId2) OR (m.user1.id = :userId2 AND m.user2.id = :userId1))")
    Boolean existsByContestIdAndUserIds(@Param("contestId") Integer contestId, @Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

    @Query(value = "SELECT m.contest_id FROM matches m WHERE (m.user_id_1 = :userId OR m.user_id_2 = :userId)",
            nativeQuery = true)
    List<Integer> findMatchedContestByUserId(@Param("userId") Integer userId);
}
