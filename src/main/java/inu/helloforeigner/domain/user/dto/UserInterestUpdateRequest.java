package inu.helloforeigner.domain.user.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInterestUpdateRequest {

    private List<InterestRequest> interests;

    public UserInterestUpdateRequest(List<InterestRequest> interests) {
        this.interests = interests;
    }
}
