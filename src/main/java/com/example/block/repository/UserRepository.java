package com.example.block.repository;


import com.example.block.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
//    String getEmailById(Integer userId);
    @Query("SELECT u.email FROM User u WHERE u.id = :userId")
    String getEmailById(@Param("userId") Integer userId);
    @Modifying
    @Query("UPDATE User u SET u.point = u.point + :amount WHERE u.id = :userId")
    void calculateUserPoints(Integer userId, Long amount);

}
