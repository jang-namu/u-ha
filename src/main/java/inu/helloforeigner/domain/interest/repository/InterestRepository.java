package inu.helloforeigner.domain.interest.repository;

import inu.helloforeigner.domain.interest.entity.Interest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findByContent(String purpose);
}
