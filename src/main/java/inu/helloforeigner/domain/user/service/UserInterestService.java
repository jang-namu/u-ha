package inu.helloforeigner.domain.user.service;

import inu.helloforeigner.domain.user.domain.UserInterest;
import inu.helloforeigner.domain.user.repository.UserInterestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInterestService {

    private final UserInterestRepository userInterestRepository;

    @Transactional
    public void deleteAllByUserId(Long id) {
        List<UserInterest> userInterests = userInterestRepository.findAllByUserId(id);
        userInterests.forEach(UserInterest::delete);
    }
}
