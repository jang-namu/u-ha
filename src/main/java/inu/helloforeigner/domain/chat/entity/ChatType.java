package inu.helloforeigner.domain.chat.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatType {
    TEXT("텍스트"),
    IMAGE("이미지"),
    SYSTEM("시스템");

    private final String description;
}
