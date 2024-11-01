package inu.helloforeigner.domain.feed.domain;

import inu.helloforeigner.common.BaseTimeEntity;
import inu.helloforeigner.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Like(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
        feed.incrementLikes();
    }

    public void delete() {
        super.delete();
        this.feed.decrementLikes();
    }

    public boolean toggleLike() {
        if (this.getDeletedAt() == null) {
            this.delete();
            return false;
        }
        this.restore();
        return true;
    }
}
