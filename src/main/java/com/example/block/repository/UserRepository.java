package com.example.block.repository;


import com.example.block.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    String getEmailById(Integer userId);
    @Query("SELECT u.email FROM User u WHERE u.id = :userId")
    String getEmailById(@Param("userId") Integer userId);


    @Modifying
    @Query("UPDATE User u SET u.point = u.point + :amount WHERE u.id = :userId")
    void calculateUserPoints(@Param("userId") Integer userId, @Param("amount") Long amount);

    @Modifying
    @Query("UPDATE User u SET u.imageUrl = :profileImageUrl WHERE u.id = :userId")
    void updateProfileImageUrl(@Param("userId") Integer userId, @Param("profileImageUrl") String profileImageUrl);

    Optional<User> findBySerialId(Long serialId);

    @Query("select u.id as id from User u where u.id = :id and u.isLogin = true")
    Optional<UserSecurityForm> findSecurityFormById(Integer id);

    //@Modifying은 데이터베이스를 수정하는 쿼리에 사용된다. 보통 update, delete 쿼리와 함께 사용된다.
    //ClearAutomatically 속성은 수정 작업 후에 영속성 컨텍스를 지우도록 한다. 이는 변경된 엔티티 상태와 데이터베이스 상태를 일치시키기 위함이다.
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.refreshToken = :refreshToken, u.isLogin = :isLogin where u.id = :id")
    void updateRefreshTokenAndLoginStatus(@Param("id") Integer id, @Param("refreshToken") String refreshToken,@Param("isLogin") Boolean isLogin);

    Optional<User> findByEmail(String email);

    //특정 필드를 반환하기 위해 정의된 인터페이스이다. Spring Data JPA는 이 인터페이스를 사용하여 결과를 매핑한다.
    //인터페이스 프로젝션을 통해서 필요한 필드만 조회할 수 있다.
    interface UserSecurityForm {
        Integer getId();
        String getPassword();
    }


    Optional<User> findById(Integer Id);
}




