package com.example.block.domain.mapping;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Matches")
//@Table(indexes = {
//        @Index(name = "user1_contest_idx", columnList = "user_id1, contest_id"),
//        @Index(name = "user2_contest_idx", columnList = "user_id2, contest_id")
//})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Matches extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id_1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2")
    private User user2;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;
}
