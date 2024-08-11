package com.example.block.repository;

import com.example.block.domain.MyContest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyContestRepository extends JpaRepository<MyContest, Integer> {
    List<MyContest> findByUserId(Integer userId);
    MyContest findByUserIdAndContestId(Integer userId, Integer contestId);
}
