package inu.helloforeigner.domain.user.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequest {

    private String name;
    private String country;
    private String profileImage;
    private String preferredLanguage;
    private String major;

    public UserUpdateRequest(String name, String country, String profileImage, String preferredLanguage, String major) {
        this.name = name;
        this.country = country;
        this.profileImage = profileImage;
        this.preferredLanguage = preferredLanguage;
        this.major = major;
    }
}
