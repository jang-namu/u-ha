package inu.helloforeigner.domain.user.repository;

import inu.helloforeigner.domain.interest.entity.Interest;
import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.domain.UserInterest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    List<UserInterest> findAllByUserId(Long id);

    void deleteAllByUserId(Long id);


    @Query("SELECT ui FROM UserInterest ui WHERE ui.interest=:interest AND ui.user!=:user")
    Page<UserInterest> findAllByInterest(Interest interest, Pageable pageable, User user);
}