package com.example.block.repository;

import com.example.block.domain.mapping.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {

    List<Applicant> findByContestId(Integer contestId);
    Applicant findByContestIdAndId(Integer contestId, Integer id);
}
