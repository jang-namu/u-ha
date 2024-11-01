package inu.helloforeigner.domain.matching.service;

import inu.helloforeigner.common.PagingResponse;
import inu.helloforeigner.common.exception.InterestNotFoundException;
import inu.helloforeigner.common.exception.UserNotFoundException;
import inu.helloforeigner.domain.interest.entity.Interest;
import inu.helloforeigner.domain.interest.repository.InterestRepository;
import inu.helloforeigner.domain.matching.dto.MatchingResponse;
import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.domain.UserInterest;
import inu.helloforeigner.domain.user.dto.InterestResponse;
import inu.helloforeigner.domain.user.repository.UserInterestRepository;
import inu.helloforeigner.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final InterestRepository interestRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PagingResponse<MatchingResponse> findMatchingUsers(String purpose, Pageable pageable, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        Interest interest = interestRepository.findByContent(purpose)
                .orElseThrow(InterestNotFoundException::new);

        List<MatchingResponse> matchingResponses = new ArrayList<>();
        Page<UserInterest> matchingUsersInterests = userInterestRepository.findAllByInterest(interest,
                pageable, user);
        for (UserInterest userInterest : matchingUsersInterests) {
//            if (userInterest.getUser().getId().equals(user.getId())) {
//                continue;
//            }
            User targetUser = userInterest.getUser();
            List<UserInterest> matchingUserInterests = targetUser.getInterests();

            List<InterestResponse> interestResponses = new ArrayList<>();
            for (UserInterest matchingUserInterest : matchingUserInterests) {
                interestResponses.add(InterestResponse.from(matchingUserInterest, matchingUserInterest.getInterest()));
            }
            matchingResponses.add(MatchingResponse.from(targetUser, purpose, interestResponses));
        }

        return PagingResponse.of(matchingResponses, matchingUsersInterests.getPageable(),
                matchingUsersInterests.getTotalElements());
    }
}
