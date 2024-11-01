package inu.helloforeigner.domain.chat.dto;

import java.util.List;
import lombok.Data;

@Data
public class ChatRoomCreateResponse {

    private Long chatRoomId;

    private List<SimpleUserResponse> participants;

    private ChatRoomCreateResponse(Long chatRoomId, List<SimpleUserResponse> participants) {
        this.chatRoomId = chatRoomId;
        this.participants = participants;
    }

    public static ChatRoomCreateResponse of(Long chatRoomId, List<SimpleUserResponse> participants) {
        return new ChatRoomCreateResponse(chatRoomId, participants);
    }
}
