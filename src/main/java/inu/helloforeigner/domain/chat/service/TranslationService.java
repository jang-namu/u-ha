package inu.helloforeigner.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TranslationService {
    private final RestClient restClient;

    public TranslationResponse translate(String text, String targetLanguage) {
        return restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/moderate_and_translate")
                        .queryParam("chat", text)
                        .queryParam("target_language", targetLanguage)
                        .build())
                .header("accept", "application/json")
                .body("")  // empty body
                .retrieve()
                .body(TranslationResponse.class);
    }
}

