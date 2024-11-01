package inu.helloforeigner.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import inu.helloforeigner.common.security.exception.ErrorResponse;
import inu.helloforeigner.common.security.exception.JwtException;
import inu.helloforeigner.common.security.exception.SecurityExceptionCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// common/security/filter/ExceptionHandlerFilter.java
@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("JWT Token Exception: ", e);
            setErrorResponse(response, SecurityExceptionCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("Exception in ExceptionHandlerFilter: ", e);
            setErrorResponse(response, SecurityExceptionCode.INVALID_TOKEN);
        }
    }

    private void setErrorResponse(HttpServletResponse response, SecurityExceptionCode exceptionCode) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(exceptionCode.name())
                .code(exceptionCode.getCode())
                .message(exceptionCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("Error writing error response", e);
        }
    }
}
