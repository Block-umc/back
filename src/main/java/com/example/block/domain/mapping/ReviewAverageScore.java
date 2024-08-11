package com.example.block.domain.mapping;


import com.example.block.domain.User;
import com.example.block.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "ReviewAverageScore")
@Table(indexes = {
        @Index(name = "user_id_idx", columnList = "user_id, id"),
        @Index(name = "review_user_idx", columnList = "review_id, user_id")})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewAverageScore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "score", nullable = false)
    private double score;
}
