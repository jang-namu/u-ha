package inu.helloforeigner.domain.user.dto;

import inu.helloforeigner.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String country;
    private String profileImage;
    private String preferredLanguage;
    private String major;
    private String updatedAt;

    @Builder
    private UserResponse(Long id, String email, String name, String country, String profileImage, String preferredLanguage, String major, String updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.country = country;
        this.profileImage = profileImage;
        this.preferredLanguage = preferredLanguage;
        this.major = major;
        this.updatedAt = updatedAt;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .country(user.getCountry())
                .profileImage(user.getProfileImage())
                .preferredLanguage(user.getPreferredLanguage())
                .major(user.getMajor())
                .updatedAt(user.getUpdatedAt().toString())
                .build();
    }
}
