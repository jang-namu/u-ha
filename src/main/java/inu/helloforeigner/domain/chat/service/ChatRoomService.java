package inu.helloforeigner.domain.chat.service;

import inu.helloforeigner.domain.chat.dto.chatroom.ChatRoomResponse;
import inu.helloforeigner.domain.chat.dto.chatroom.ChatRoomsResponse;
import inu.helloforeigner.domain.chat.entity.Chat;
import inu.helloforeigner.domain.chat.entity.ChatRoom;
import inu.helloforeigner.domain.chat.entity.ChatUser;
import inu.helloforeigner.domain.chat.repository.ChatRepository;
import inu.helloforeigner.domain.chat.repository.ChatUserRepository;
import inu.helloforeigner.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;

    public ChatRoomsResponse getChatRooms(Long userId) {
        List<ChatUser> chatUsers = chatUserRepository.findAllByUserId(userId);
        List<ChatRoom> chatRooms = chatUsers.stream().map(ChatUser::getChatRoom).toList();

        List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            List<User> participants = chatRoom.getChatUsers().stream().map(ChatUser::getUser).toList();
            Chat chat = chatRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId())
                    .orElse(null);
            chatRoomResponses.add(ChatRoomResponse.from(chatRoom, participants, chat));
        }

        return ChatRoomsResponse.from(chatRoomResponses);
    }

}
