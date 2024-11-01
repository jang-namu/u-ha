package inu.helloforeigner.domain.matching.dto;

import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.dto.InterestResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class MatchingResponse {

    private Long userId;
    private String name;
    private String profileImage;
    private String country;
    private String major;
    private String matchingPurpose;
    private List<InterestResponse> interests;

    private MatchingResponse(Long userId, String name, String profileImage, String country, String major, String matchingPurpose, List<InterestResponse> interests) {
        this.userId = userId;
        this.name = name;
        this.profileImage = profileImage;
        this.country = country;
        this.major = major;
        this.matchingPurpose = matchingPurpose;
        this.interests = interests;
    }

    public static MatchingResponse from(User user, String matchingPurpose, List<InterestResponse> interests) {
        return new MatchingResponse(user.getId(), user.getName(), user.getProfileImage(), user.getCountry(), user.getMajor(), matchingPurpose, interests);
    }
}
