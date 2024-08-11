package com.example.block.domain;

import com.example.block.domain.common.BaseEntity;
import com.example.block.domain.enums.ContestCategory;
import com.example.block.domain.enums.LoginType;
import com.example.block.domain.mapping.Likes;
import com.example.block.domain.mapping.ReviewAverageScore;
import com.example.block.domain.mapping.TransactionReview;


import com.example.block.dto.SignUpRequest;
import com.example.block.global.constants.Constants;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Entity(name = "User")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "serial_id", nullable = false, unique = true)
    private Long serialId;

    @Column(nullable = true, length = 50)
    private String userId;

    @Column(name = "platform")
    private String platform;

    @Column(name = "password", length = 256)
    private String passWord;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email",unique = true, length = 50)
    private String email;

    @Column(nullable = true, length = 1023)
    private String portfolio;

    @Column(nullable = true, length = 1023)
    private String imageUrl;

    @Column(nullable = true, length = 25)
    private String birthDay;

    @Column(nullable = true, length = 10)
    private String name;

    @Column(nullable = true, length = 30)
    private String address;

    @Column(nullable = true, length = 25)
    private String phoneNumber;

    @Column(nullable = true, length = 25)
    private String university;

    @Column(nullable = true, length = 25)
    private String univMajor;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_login", columnDefinition = "TINYINT(1)")
    private Boolean isLogin;

    @Column(name = "is_new_user")
    private Boolean isNewUser;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'kakao'")
    private LoginType loginType;

    //    0 = FALSE, 1 = TRUE
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted;

    @Column(nullable = true)
    private LocalDateTime deleted_at;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column
    private ContestCategory InterestCategory;

    @Setter
    private double score;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<TransactionReview> transactionReviewList=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewAverageScore> reviewAverageScoresList=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PointDetail> pointDetailList=new ArrayList<>();

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MyContest> myContestList = new ArrayList<>();
    @Builder
    public User(Long serialId) {
        this.serialId = serialId;
        this.isLogin = true;
        this.isNewUser = true;
        this.nickname = Constants.USER_NICKNAME_PREFIX + serialId;
    }

    public static User signUp(Long serialId){
        return User.builder()
                .serialId(serialId)
                .build();
    }

    public static User signUpByRequest(SignUpRequest request) {
        return User.builder()
                .serialId(request.getProviderId())
                .email(request.getEmail())
                .passWord(request.getPassword())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .university(request.getUniversity())
                .birthDay(request.getBirthDay())
                .univMajor(request.getUnivMajor())
                .portfolio(request.getPortfolio())
                .point(0L)
                .isLogin(true)
                .build();
    }

    @OneToMany(mappedBy = "userLiker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Likes> likerList = new ArrayList<>();

    @OneToMany(mappedBy = "userLiked", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Likes> likedList = new ArrayList<>();


}
