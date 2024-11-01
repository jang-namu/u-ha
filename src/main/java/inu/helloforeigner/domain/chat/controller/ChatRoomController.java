package inu.helloforeigner.domain.chat.controller;

import inu.helloforeigner.domain.chat.dto.ChatRoomCreateResponse;
import inu.helloforeigner.domain.chat.dto.GroupChatCreateRequest;
import inu.helloforeigner.domain.chat.dto.chatroom.ChatRoomHistoryResponse;
import inu.helloforeigner.domain.chat.dto.chatroom.ChatRoomsResponse;
import inu.helloforeigner.domain.chat.service.ChatRoomService;
import inu.helloforeigner.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<ChatRoomsResponse> getChatRooms(@PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.getChatRooms(userId));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ChatRoomHistoryResponse> getChatHistory(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "20") int size) {
        ChatRoomHistoryResponse response = chatService.getChatRoomMessages(roomId, lastMessageId, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ChatRoomCreateResponse> createChatroom(@PathVariable Long userId,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        ChatRoomCreateResponse response = chatService.createChatRoom(userId, userDetails);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/groups")
    public ResponseEntity<ChatRoomCreateResponse> createGroupChatroom(@RequestBody GroupChatCreateRequest request,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        ChatRoomCreateResponse response = chatService.createGroupChatRoom(request, userDetails);
        return ResponseEntity.ok(response);
    }
}
