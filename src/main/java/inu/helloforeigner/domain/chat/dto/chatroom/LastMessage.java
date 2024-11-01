package inu.helloforeigner.domain.chat.dto.chatroom;

import inu.helloforeigner.domain.chat.entity.Chat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LastMessage {
    private String content;
    private String type;
    private Long senderId;
    private String createdAt;

    public static LastMessage from(Chat chat) {
        if (chat == null) {
            return null;
        }
        return new LastMessage(chat.getContent(), chat.getType().name(), chat.getSender().getId(),
                chat.getCreatedAt().toString());
    }
}
