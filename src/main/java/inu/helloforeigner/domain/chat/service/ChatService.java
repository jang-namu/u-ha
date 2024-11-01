package inu.helloforeigner.domain.chat.service;

import inu.helloforeigner.common.exception.ChatRoomNotFoundException;
import inu.helloforeigner.common.exception.UserNotFoundException;
import inu.helloforeigner.domain.chat.dto.ChatMessageRequest;
import inu.helloforeigner.domain.chat.dto.ChatRoomCreateResponse;
import inu.helloforeigner.domain.chat.dto.GroupChatCreateRequest;
import inu.helloforeigner.domain.chat.dto.SimpleUserResponse;
import inu.helloforeigner.domain.chat.dto.TranslateChatResponse;
import inu.helloforeigner.domain.chat.dto.WebSocketChatPayload;
import inu.helloforeigner.domain.chat.dto.WebSocketChatResponse;
import inu.helloforeigner.domain.chat.dto.chatroom.ChatRoomHistoryResponse;
import inu.helloforeigner.domain.chat.entity.Chat;
import inu.helloforeigner.domain.chat.entity.ChatRoom;
import inu.helloforeigner.domain.chat.entity.ChatType;
import inu.helloforeigner.domain.chat.entity.ChatUser;
import inu.helloforeigner.domain.chat.entity.FilteringInfoMessage;
import inu.helloforeigner.domain.chat.entity.TranslateChat;
import inu.helloforeigner.domain.chat.repository.ChatRepository;
import inu.helloforeigner.domain.chat.repository.ChatRoomRepository;
import inu.helloforeigner.domain.chat.repository.ChatUserRepository;
import inu.helloforeigner.domain.chat.repository.TranslateChatRepository;
import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final TranslationService translationService;
    private final TranslateChatRepository translateChatRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;

    private final RestClient restClient;

    public ChatRoomHistoryResponse getChatRoomMessages(Long roomId, Long lastMessageId, Integer size) {
        List<WebSocketChatPayload> webSocketChatPayloads = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, size);

        Page<Chat> chats;
        if (lastMessageId == null) {
            chats = chatRepository.findTopByRoomIdOrderByCreatedAtDesc(roomId, pageable);
        } else {
            chats = chatRepository.findByRoomIdAndIdLessThanOrderByCreatedAtDesc(roomId, lastMessageId, pageable);
        }
        for (Chat chat : chats) {
            List<TranslateChat> translateChats = translateChatRepository.findAllByChatId(chat.getId());
            List<TranslateChatResponse> translations = translateChats.stream().map(TranslateChatResponse::from)
                    .toList();
            webSocketChatPayloads.add(WebSocketChatPayload.of(chat, translations));
        }
        boolean hasNext = webSocketChatPayloads.size() == size;
        return ChatRoomHistoryResponse.from(webSocketChatPayloads, hasNext);
    }

    public Long getUserIdFromUserDetails(UserDetails userDetails) {
        return userRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException()).getId();
    }

    public WebSocketChatResponse saveAndTranslateMessage(ChatMessageRequest request, Long senderId) {

        // 채팅방 존재 확인
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new ChatRoomNotFoundException(request.getChatRoomId()));

        // 발신자 정보 확인
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException(senderId));

        // 수신자 정보 확인
        List<ChatUser> receiverChatUsers = chatUserRepository.findAllByChatRoom(chatRoom).stream().filter(
                chatUser -> !Objects.equals(chatUser.getUser().getId(), senderId)).toList();
        List<User> receivers = receiverChatUsers.stream().map(ChatUser::getUser).toList();

        // 채팅 메시지 저장
        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(request.getContent())
                .type(ChatType.valueOf(request.getType()))
                .build();

        TranslationResponse maybeFiltered = translationService.translate(
                request.getContent(),
                sender.getPreferredLanguage()
        );
        log.debug("maybeFiltered: {}  : {}", maybeFiltered.getOriginal(), maybeFiltered.isFlagged());
        if (maybeFiltered.isFlagged()) {
            chat.filtering();
        }
        chatRepository.save(chat);

        List<TranslateChat> translateChats = new ArrayList<>();
        for (User receiver : receivers) {
            if (Objects.equals(receiver.getPreferredLanguage(), sender.getPreferredLanguage())) {
                continue;
            }

            if (maybeFiltered.isFlagged()) {
                translateChats.add(TranslateChat.builder()
                        .chat(chat)
                        .content(FilteringInfoMessage.getByLanguageCode(receiver.getPreferredLanguage()))
                        .language(receiver.getPreferredLanguage())
                        .build());
                continue;
            }
            // 메시지 번역
            TranslationResponse translationResponse = translationService.translate(
                    request.getContent(),
                    receiver.getPreferredLanguage()
            );
            log.debug("translationResponse - original: {}  / translated: {} \n"
                            + "filtered : {} / message : {} / recieverLang : {}", translationResponse.getOriginal(),
                    translationResponse.getTranslated(), translationResponse.isFlagged(),
                    translationResponse.getMessage(), receiver.getPreferredLanguage());

            // 번역된 메시지 저장
            translateChats.add(TranslateChat.builder()
                    .chat(chat)
                    .content((translationResponse.getTranslated() == null || translationResponse.getTranslated()
                            .equals("None")) ? translationResponse.getOriginal()
                            : translationResponse.getTranslated())
                    .language(receiver.getPreferredLanguage())
                    .build());
        }
        // 채팅방 최종 메시지 시간 업데이트
        chatRoom.updateLastMessageAt();

        translateChatRepository.saveAll(translateChats);

        return WebSocketChatResponse.createMessage(
                WebSocketChatPayload.of(chat, translateChats.stream().map(TranslateChatResponse::from).toList()));
    }

    public ChatRoomCreateResponse createChatRoom(Long userId, UserDetails userDetails) {
        User targetUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

        ChatRoom chatRoom = ChatRoom.of();
        ChatUser chatUser1 = ChatUser.of(chatRoom, user);
        ChatUser chatUser2 = ChatUser.of(chatRoom, targetUser);

        chatRoom.addChatUser(chatUser1);
        chatRoom.addChatUser(chatUser2);
        chatRoomRepository.save(chatRoom);
        chatUserRepository.save(chatUser1);
        chatUserRepository.save(chatUser2);

        List<User> users = List.of(user, targetUser);

        return ChatRoomCreateResponse.of(chatRoom.getId(), users.stream().map(SimpleUserResponse::from).toList());
    }

    public ChatRoomCreateResponse createGroupChatRoom(GroupChatCreateRequest request, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        ChatRoom chatRoom = ChatRoom.of();
        chatRoomRepository.save(chatRoom);

        List<User> users = userRepository.findAllById(request.getParticipantIds());
        users.add(user);
        for (User participant : users) {
            ChatUser chatUser = ChatUser.of(chatRoom, participant);
            chatUserRepository.save(chatUser);
        }

        return ChatRoomCreateResponse.of(chatRoom.getId(), users.stream().map(SimpleUserResponse::from).toList());
    }
}