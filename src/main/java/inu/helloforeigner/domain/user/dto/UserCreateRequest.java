package inu.helloforeigner.domain.user.dto;

import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String email;
    private String password;
    private String name;
    // todo: File 업로드 처리
    private String profileImage;
    private String introduction;
    private String country;
    private String language;
    private String major;

    public UserCreateRequest(String email, String password, String name, String profileImage, String introduction, String country, String language, String major) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.country = country;
        this.language = language;
        this.major = major;
    }

    public void encodePassword(String encode) {
        this.password = encode;
    }
}
