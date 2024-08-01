package com.example.block.repository;

import com.example.block.domain.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Integer>{
    boolean existsByTitle(String title);
}
