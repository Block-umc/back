package com.example.block.domain.mapping;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Likes")
@Table(indexes = {
        @Index(name = "like_all_column_idx", columnList = "contest_id, user_liker_id, user_liked_id"),
        @Index(name = "user_liker_idx", columnList = "user_liker_id"),
        @Index(name = "user_liked_idx", columnList = "user_liked_id")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_liker_id")
    private User userLiker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_liked_id")
    private User userLiked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;
}
