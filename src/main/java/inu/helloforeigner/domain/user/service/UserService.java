package inu.helloforeigner.domain.user.service;

import inu.helloforeigner.common.exception.InterestNotFoundException;
import inu.helloforeigner.common.exception.UserNotFoundException;
import inu.helloforeigner.domain.feed.dto.FeedCreateResponse;
import inu.helloforeigner.domain.interest.entity.Interest;
import inu.helloforeigner.domain.interest.repository.InterestRepository;
import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.domain.UserInterest;
import inu.helloforeigner.domain.user.dto.InterestResponse;
import inu.helloforeigner.domain.user.dto.InterestUpdateResponse;
import inu.helloforeigner.domain.user.dto.UserCreateRequest;
import inu.helloforeigner.domain.user.dto.UserInterestUpdateRequest;
import inu.helloforeigner.domain.user.dto.UserProfileResponse;
import inu.helloforeigner.domain.user.dto.UserResponse;
import inu.helloforeigner.domain.user.dto.UserUpdateRequest;
import inu.helloforeigner.domain.user.repository.UserInterestRepository;
import inu.helloforeigner.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Example service with caching
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInterestRepository userInterestRepository;

    public User createUser(UserCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)  // 해시된 패스워드 저장
                .name(request.getName())
                // ... 다른 필드들
                .build();

        return userRepository.save(user);
    }

    //    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public UserProfileResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        List<InterestResponse> interestResponses = user.getInterests().stream()
                .map(userInterest -> InterestResponse.from(userInterest, userInterest.getInterest()))
                .collect(Collectors.toList());
        List<FeedCreateResponse> feeds = user.getFeeds().stream().map(FeedCreateResponse::from)
                .collect(Collectors.toList());

        return UserProfileResponse.of(user, interestResponses, feeds);
    }

    //    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.delete();
        // Cascade delete related entities
    }

    //    @Caching(evict = {
//            @CacheEvict(value = "users", key = "#user.id"),
//            @CacheEvict(value = "usersByEmail", key = "#user.email")
//    })
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.update(request);
        // Update user logic
        return UserResponse.from(user);
    }

    @Transactional
    public InterestUpdateResponse updateInterest(Long id, UserInterestUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userInterestRepository.deleteAllByUserId(id);
        List<UserInterest> userInterests = new ArrayList<>();
        request.getInterests().forEach(interestRequest -> {
            Interest interest = interestRepository.findByContent(interestRequest.getInterest())
                    .orElseThrow(() -> new InterestNotFoundException(interestRequest.getInterest()));

            UserInterest userInterest = UserInterest.builder()
                    .user(user)
                    .interest(interest)
                    .score(interestRequest.getScore())
                    .build();

            userInterests.add(userInterest);
            userInterestRepository.save(userInterest);
        });

        user.updateInterests(userInterests);
        userRepository.save(user);
//        userInterestRepository.saveAllAndFlush(userInterests);

        return InterestUpdateResponse.from(user.getId(), userInterests.stream()
                .map(userInterest -> InterestResponse.from(userInterest,
                        userInterest.getInterest()))
                .toList());
    }
}
