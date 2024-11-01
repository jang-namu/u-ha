package inu.helloforeigner.domain.chat.dto;

import inu.helloforeigner.domain.chat.entity.TranslateChat;
import lombok.Getter;

@Getter
public class TranslateChatResponse {
    private String language;
    private String content;

    private TranslateChatResponse(String language, String content) {
        this.language = language;
        this.content = content;
    }

    public static TranslateChatResponse from(TranslateChat translateChat) {
        return new TranslateChatResponse(translateChat.getLanguage(), translateChat.getContent());
    }
}
