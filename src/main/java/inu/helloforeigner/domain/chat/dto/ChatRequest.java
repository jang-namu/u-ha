package inu.helloforeigner.domain.chat.dto;

import lombok.Getter;

@Getter
public class ChatRequest {

    private String type; //(TEXT/IMAGE)
    private String content;

    public ChatRequest(String type, String content) {
        this.type = type;
        this.content = content;
    }
}
