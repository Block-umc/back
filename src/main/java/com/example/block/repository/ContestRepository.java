package com.example.block.repository;

import com.example.block.domain.Contest;
import com.example.block.domain.enums.ContestCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Integer>{
    boolean existsByTitle(String title);
    Optional<Contest> findById(Integer contestId);
    List<Contest> findContestByHashTag(String category);
    List<Contest> findContestByHashTagIn(List<String> categorys);
    List<Contest> findAll();
}
