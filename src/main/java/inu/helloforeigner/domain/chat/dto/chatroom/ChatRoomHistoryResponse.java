package inu.helloforeigner.domain.chat.dto.chatroom;

import inu.helloforeigner.domain.chat.dto.WebSocketChatPayload;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomHistoryResponse {

    private List<WebSocketChatPayload> messages;
    private boolean hasNext;

    public static ChatRoomHistoryResponse from(List<WebSocketChatPayload> messages, boolean hasNext) {
        return new ChatRoomHistoryResponse(messages, hasNext);
    }
}
