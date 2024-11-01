package inu.helloforeigner.common.websocket;

import inu.helloforeigner.common.security.jwt.JwtTokenProvider;
import inu.helloforeigner.common.security.jwt.JwtTokenProvider.TokenDto;
import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.dto.UserCreateRequest;
import inu.helloforeigner.domain.user.repository.UserRepository;
import inu.helloforeigner.domain.user.security.CustomUserDetails;
import inu.helloforeigner.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Getter
    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    public static class JwtResponse {
        private String token;
        private Long userId;
        private String email;
        private List<String> roles;

        @Builder
        public JwtResponse(String token, Long userId, String email, List<String> roles) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.roles = roles;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenDto tokenDto = jwtTokenProvider.createToken(authentication);

        UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles =
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Long id = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found")).getId();

        JwtResponse jwtResponse =
                JwtResponse.builder()
                        .token(tokenDto.getAccessToken())
                        .userId(id)
                        .email(userDetails.getUsername())
                        .roles(roles)
                        .build();

        response.addCookie(new Cookie("access_token", tokenDto.getAccessToken()));
        return ResponseEntity.ok(jwtResponse);
    }

    @Getter
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserCreateRequest request) {
        if (userRepository.existsByName(request.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        request.encodePassword(encoder.encode(request.getPassword()));
        User userEntity = User.from(request);
        userEntity = userRepository.save(userEntity);
        userRepository.save(userEntity);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(new MessageResponse(userDetails.getUsername() + "User registered successfully!"));
    }
}
