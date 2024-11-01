package inu.helloforeigner.domain.chat.dto.chatroom;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomsResponse {
    private List<ChatRoomResponse> chatRooms;

    public static ChatRoomsResponse from(List<ChatRoomResponse> chatRooms) {
        return new ChatRoomsResponse(chatRooms);
    }
}
