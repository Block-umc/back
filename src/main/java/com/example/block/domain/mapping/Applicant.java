package com.example.block.domain.mapping;

import com.example.block.domain.Contest;
import com.example.block.domain.User;
import com.example.block.domain.enums.ApplyPart;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Applicant")
@Table(indexes = {
        @Index(name = "contest_id_idx", columnList = "contest_id, id"),
        @Index(name = "contest_user_idx", columnList = "contest_id, user_id")})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplyPart applyPart;

    @Column(nullable = true)
    private String content;
}
