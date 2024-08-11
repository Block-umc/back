package com.example.block.domain;

import com.example.block.domain.common.BaseEntity;
import com.example.block.domain.enums.ContestType;
import com.example.block.domain.mapping.Applicant;
import com.example.block.domain.mapping.Likes;
import com.example.block.domain.mapping.Review;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

@Entity(name = "Contest")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class Contest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = true)
    private String applyUrl;

    @Column(nullable = false)
    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "contest_hashtags", joinColumns = @JoinColumn(name = "contest_id"))
    @Column(name = "hashtag")
    private List<String> hashTag = new ArrayList<>();

    @Column(nullable = false, length = 10)
    private String startDate;

    @Column(nullable = false, length = 10)
    private String endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ACTIVE'")
    private ContestType contestType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Setter
    private ContestType contestCategory;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private String hostUrl;

    @OneToMany(mappedBy = "contest")
    @Builder.Default
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Applicant> applicants = new ArrayList<>();

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MyContest> myContests = new ArrayList<>();

}
