package com.example.block.repository;


import com.example.block.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying
    @Query("UPDATE User u SET u.point = u.point + :amount WHERE u.id = :userId")
    void calculateUserPoints(Integer userId, Long amount);


}
