package inu.helloforeigner.domain.feed.domain;

import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "feeds")
public class Feed extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String image;

    private String s3Key;

    @Column(nullable = false)
    private String content;

    private int numOfLikes;

    @OneToMany(mappedBy = "feed")
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Feed(User user, String image, String s3Key, String content) {
        this.user = user;
        this.image = image;
        this.s3Key = s3Key;
        this.content = content;
        this.numOfLikes = 0;
    }

    public void incrementLikes() {
        this.numOfLikes = Math.min(Integer.MAX_VALUE, this.numOfLikes + 1);
    }

    public void decrementLikes() {
        this.numOfLikes = Math.max(0, this.numOfLikes - 1);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateImage(String image, String s3Key) {
        this.image = image;
        this.s3Key = s3Key;
    }
}
