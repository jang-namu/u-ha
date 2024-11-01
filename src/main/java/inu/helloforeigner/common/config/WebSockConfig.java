package inu.helloforeigner.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${cors.dev.server.pattern}")
    private String CORS_DEV_SERVER_PATTERN;

    @Value("${cors.prod.server.pattern}")
    private String CORS_PROD_SERVER_PATTERN;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                //.setAllowedOrigins("*")
                .setAllowedOriginPatterns("http://localhost:[*]", CORS_DEV_SERVER_PATTERN, CORS_PROD_SERVER_PATTERN)  // 개발 환경
                .withSockJS();
    }
}

