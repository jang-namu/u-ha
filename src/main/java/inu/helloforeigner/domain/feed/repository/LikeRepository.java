package inu.helloforeigner.domain.feed.repository;

import inu.helloforeigner.domain.feed.domain.Feed;
import inu.helloforeigner.domain.feed.domain.Like;
import inu.helloforeigner.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByFeedAndUser(Feed feed, User user);
}