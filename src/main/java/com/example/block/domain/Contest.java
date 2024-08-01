package com.example.block.domain;

import com.example.block.domain.common.BaseEntity;
import com.example.block.domain.enums.ContestType;
import com.example.block.domain.mapping.Applicant;
import com.example.block.domain.mapping.Likes;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(nullable = true)
    private String hashTag;

    @Column(nullable = false, length = 10)
    private String startDate;

    @Column(nullable = false, length = 10)
    private String endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ACTIVE'")
    private ContestType contestType;

    @Column(nullable = false)
    private String host;

    @OneToMany(mappedBy = "contest")
    private List<Likes> likesList = new ArrayList<>();
}
