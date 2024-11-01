package inu.helloforeigner.domain.chat.dto.chatroom;

import inu.helloforeigner.domain.chat.dto.SimpleUserResponse;
import inu.helloforeigner.domain.chat.entity.Chat;
import inu.helloforeigner.domain.chat.entity.ChatRoom;
import inu.helloforeigner.domain.user.domain.User;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomResponse {
    private Long id;
    private List<SimpleUserResponse> participants;
    private LastMessage lastMessage;

    public static ChatRoomResponse from(ChatRoom chatRoom, List<User> participants, Chat chat) {
        return new ChatRoomResponse(chatRoom.getId(),
                participants.stream().map(SimpleUserResponse::from).toList(),
                LastMessage.from(chat));
    }
}


