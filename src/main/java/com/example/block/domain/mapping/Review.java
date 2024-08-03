package com.example.block.domain.mapping;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @Column(nullable = true, length = 25)
    private String content;

    @Column(nullable = true)
    private Float score;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<TransactionReview> transactionReviewList=new ArrayList<>();

}
