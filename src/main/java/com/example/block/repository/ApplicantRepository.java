package com.example.block.repository;

import com.example.block.domain.User;
import com.example.block.domain.mapping.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {

    List<Applicant> findByContestId(Integer contestId);
    Applicant findByContestIdAndId(Integer contestId, Integer id);
    Optional<Applicant> findByContestIdAndUserId(Integer contestId, Integer userId);

//    User findUserByIdAndContestId(Integer applicantId, Integer contestId);

    @Query("SELECT a.user FROM Applicant a WHERE a.id = :applicantId AND a.contest.id = :contestId")
    User findUserByIdAndContestId(@Param("applicantId") Integer applicantId, @Param("contestId") Integer contestId);

}
