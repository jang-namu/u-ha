package inu.helloforeigner.domain.chat.dto;

import lombok.Getter;

@Getter
public class WebSocketChatResponse {
    private String type = "MESSAGE";
    private WebSocketChatPayload payload;

    public WebSocketChatResponse(String type, WebSocketChatPayload payload) {
        this.type = type;
        this.payload = payload;
    }

    public static WebSocketChatResponse createMessage(WebSocketChatPayload payload) {
        return new WebSocketChatResponse("MESSAGE", payload);
    }
}

