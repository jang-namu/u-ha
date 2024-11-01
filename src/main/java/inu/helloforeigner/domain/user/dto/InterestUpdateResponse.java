package inu.helloforeigner.domain.user.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class InterestUpdateResponse {

    private Long userId;
    private List<InterestResponse> interests;

    public InterestUpdateResponse(Long userId, List<InterestResponse> interests) {
        this.userId = userId;
        this.interests = interests;
    }

    public static InterestUpdateResponse from(Long userId, List<InterestResponse> interests) {
        return new InterestUpdateResponse(userId, interests);
    }
}
