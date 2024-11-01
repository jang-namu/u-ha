package inu.helloforeigner.domain.chat.dto;

import inu.helloforeigner.domain.chat.entity.Chat;
import inu.helloforeigner.domain.chat.entity.FilteringInfoMessage;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WebSocketChatPayload {
    private String senderName;
    private Long id;
    private String type;
    private String content;
    private Long senderId;
    private boolean isFiltered;
    private List<TranslateChatResponse> translations;
    private String createdAt;

    @Builder
    private WebSocketChatPayload(String senderName, Long id, String type, String content, Long senderId, boolean isFiltered,
                                 List<TranslateChatResponse> translations, String createdAt) {
        this.senderName = senderName;
        this.id = id;
        this.type = type;
        this.content = content;
        this.senderId = senderId;
        this.isFiltered = isFiltered;
        this.translations = translations;
        this.createdAt = createdAt;
    }

    public static WebSocketChatPayload of(Chat chat, List<TranslateChatResponse> translations) {
        return WebSocketChatPayload.builder()
                .senderName(chat.getSender().getName())
                .id(chat.getId())
                .type(chat.getType().getDescription())
                .content(chat.isFiltered() ? FilteringInfoMessage.getByLanguageCode(chat.getSender()
                        .getPreferredLanguage()) : chat.getContent())
                .senderId(chat.getSender().getId())
                .isFiltered(chat.isFiltered())
                .translations(translations)
                .createdAt(chat.getCreatedAt().toString())
                .build();
    }

}

