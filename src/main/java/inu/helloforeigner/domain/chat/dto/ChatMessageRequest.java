package inu.helloforeigner.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 채팅 메시지 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Long chatRoomId;
    private String type;
    private String content;
}
