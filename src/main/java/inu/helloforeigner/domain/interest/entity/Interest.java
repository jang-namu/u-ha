package inu.helloforeigner.domain.interest.entity;

import inu.helloforeigner.common.BaseTimeEntity;
import inu.helloforeigner.domain.user.domain.UserInterest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "interests")
public class Interest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "interest")
    private List<UserInterest> userInterests = new ArrayList<>();

    @Builder
    public Interest(String content) {
        this.content = content;
    }
}
