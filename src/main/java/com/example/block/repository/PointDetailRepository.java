package com.example.block.repository;

import com.example.block.domain.PointDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointDetailRepository extends JpaRepository<PointDetail, Integer> {
    @Query("SELECT p FROM PointDetail p WHERE p.user.id = :userId ORDER BY p.created_at DESC")
    List<PointDetail> findTopByUserIdOrderByCreatedAtDesc(@Param("userId") Integer userId, Pageable pageable);

}
