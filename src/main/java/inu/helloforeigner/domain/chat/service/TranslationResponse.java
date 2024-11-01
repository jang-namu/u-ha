package inu.helloforeigner.domain.chat.service;

import lombok.Data;

// 응답을 받을 클래스
@Data
public class TranslationResponse {
    private boolean flagged;
    private String original;
    private String translated;
    private String message;
}
