package inu.helloforeigner.domain.chat.controller;

import inu.helloforeigner.common.security.jwt.JwtTokenProvider;
import inu.helloforeigner.domain.chat.dto.ChatMessageRequest;
import inu.helloforeigner.domain.chat.dto.WebSocketChatResponse;
import inu.helloforeigner.domain.chat.service.ChatService;
import inu.helloforeigner.domain.user.repository.UserRepository;
import inu.helloforeigner.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    @MessageMapping("/chat/message")  // /pub/chat/message로 전송되는 메시지를 처리
    public void message(@Payload ChatMessageRequest request,
                        @Header("Authorization") String token,
                        SimpMessageHeaderAccessor headerAccessor) {
        // 토큰에서 사용자 정보 추출
        String userName = getUserNameFromToken(token);
        Long senderId = userRepository.findByEmail(userName)
                .orElseThrow().getId();

        // 메시지 저장 및 처리
        WebSocketChatResponse response = chatService.saveAndTranslateMessage(request, senderId);

        // 구독자들에게 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getChatRoomId(), response);
    }

    private String getUserNameFromToken(String token) {
        // Bearer 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return jwtTokenProvider.getUserNameFromJwtToken(token);
    }


//    // 채팅방 입장
//    @MessageMapping("/chat.enter/{roomId}")
//    public void enter(@DestinationVariable String roomId, ChatMessageRequest request) {
//        request.setMessage(request.getSender() + "님이 입장하셨습니다.");
//        request.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        messagingTemplate.convertAndSend("/topic/chat/" + roomId, request);
//    }
//
//    // 채팅 메시지 전송
//    @MessageMapping("/chat.send/{roomId}")
//    public void sendMessage(@DestinationVariable String roomId, ChatMessageRequest request) {
//        request.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        messagingTemplate.convertAndSend("/topic/chat/" + roomId, request);
//    }
//
//    // 채팅방 퇴장
//    @MessageMapping("/chat.leave/{roomId}")
//    public void leave(@DestinationVariable String roomId, ChatMessageRequest request) {
//        request.setMessage(request.getSender() + "님이 퇴장하셨습니다.");
//        request.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        messagingTemplate.convertAndSend("/topic/chat/" + roomId, request);
//    }
}