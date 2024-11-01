package inu.helloforeigner.domain.user.security;

import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.domain.UserStatus;
import inu.helloforeigner.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// domain/user/security/CustomUserDetailsService.java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new DisabledException("User is not active");
        }

        return CustomUserDetails.from(user);
    }

    // JWT 에서 사용할 id로 사용자를 조회하는 메서드
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new DisabledException("User is not active");
        }

        return CustomUserDetails.from(user);
    }
}