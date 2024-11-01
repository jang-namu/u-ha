package inu.helloforeigner.domain.chat.dto;

import inu.helloforeigner.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleUserResponse {
    private Long userId;
    private String name;
    private String profileImage;

    public static SimpleUserResponse from(User user) {
        return new SimpleUserResponse(user.getId(), user.getName(), user.getProfileImage());
    }
}
