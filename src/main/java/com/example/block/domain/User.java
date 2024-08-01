package com.example.block.domain;

import com.example.block.domain.common.BaseEntity;
import com.example.block.domain.enums.LoginType;
import com.example.block.domain.mapping.Applicant;
import com.example.block.domain.mapping.Likes;
import com.example.block.domain.mapping.TransactionReview;


import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "User")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true, length = 50)
    private String userId;

    @Column(nullable = true,length = 50)
    private String passWord;

    @Column(name = "email",nullable = true, length = 50)
    private String email;

    @Column(nullable = true, length = 1023)
    private String portfolio;

    @Column(nullable = true, length = 1023)
    private String imageUrl;

    @Column(nullable = false, length = 8)
    private String birthDay;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30)
    private String address;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = true, length = 25)
    private String university;

    @Column(nullable = true, length = 25)
    private String univMajor;

//    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "VARCHAR(10) DEFAULT kakao ")
//    private LoginType loginType;

    //    0 = FALSE, 1 = TRUE
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted;

    @Column(nullable = true)
    private LocalDateTime deleted_at;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long point;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TransactionReview> transactionReviewList=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PointDetail> pointDetailList=new ArrayList<>();


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    @OneToMany(mappedBy = "userLiker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Likes> likerList = new ArrayList<>();

    @OneToMany(mappedBy = "userLiked", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Likes> likedList = new ArrayList<>();


}
