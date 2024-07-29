package com.example.block.domain;

import com.example.block.domain.common.BaseEntity;
import com.example.block.domain.enums.LoginType;
import com.example.block.domain.mapping.Applicant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.Id;

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

    @Column(nullable = true, length = 50)
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private LoginType loginType;

    //    0 = FALSE, 1 = TRUE
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted;

    @Column(nullable = true)
    private LocalDateTime deleted_at;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long point;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

//    @OneToMany(mappedBy = "user")
//    private List<Applicant> applicantList = new ArrayList<Applicant>();
}
