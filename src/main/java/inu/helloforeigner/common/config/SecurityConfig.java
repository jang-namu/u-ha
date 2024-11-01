package inu.helloforeigner.common.config;

import inu.helloforeigner.common.security.exception.CustomAccessDeniedHandler;
import inu.helloforeigner.common.security.exception.CustomAuthenticationEntryPoint;
import inu.helloforeigner.common.security.filter.ExceptionHandlerFilter;
import inu.helloforeigner.common.security.filter.JwtAuthenticationFilter;
import inu.helloforeigner.common.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    private final UserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(security -> {
                    security.configurationSource(corsConfigurationSource);
                })
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMIT_ALL).permitAll()
                        .requestMatchers(HttpMethod.GET, PERMIT_ALL_GET).permitAll()
                        .anyRequest().hasRole("USER") // todo: 테스트용으로 모든 요청 허용
                )

                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))

                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(exceptionHandlerFilter, LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // todo: 현재 시스템에서 패스워드 확인이 1초가 걸리도록 strength 파라미터 조정
        return new BCryptPasswordEncoder();
    }

    String[] PERMIT_ALL = {
            "/oauth2/**", //oauth2 로그인 서비스 접근
            "/login/**", //oauth2 로그인창
            "/swagger-ui/**", //스웨거 명세
            "/swagger-ui.html",
            "/v3/api-docs/**", //스웨거 명세
            "/api-docs/**",
            "/api/v1/auth/**",
            "/", "/index.html", "/public/**",
            "/css/**", "/js/**", "/images/**",
            "/favicon.ico", "/static/**",  // static 폴더 전체에 대한 접근 허용
            "/ws/chat/**",  // WebSocket 엔드포인트 추가
            "/ws/chat/info",  // SockJS가 사용하는 엔드포인트
            "/pub/**",
            "/sub/**"
    };

    String[] PERMIT_ALL_GET = {
            "/api/v1/feeds/**"
    };
}


