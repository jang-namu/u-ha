package inu.helloforeigner.domain.user.dto;

import inu.helloforeigner.domain.feed.dto.FeedCreateResponse;
import inu.helloforeigner.domain.user.domain.User;
import java.util.List;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    private Long userId;
    private String name;
    private String profileImage;
    private String country;
    private String major;
    private String preferredLanguage;
    private List<InterestResponse> interests;
    private List<FeedCreateResponse> feeds;

    private UserProfileResponse(Long userId, String name, String profileImage, String country, String major,
                               String preferredLanguage, List<InterestResponse> interests,
                               List<FeedCreateResponse> feeds) {
        this.userId = userId;
        this.name = name;
        this.profileImage = profileImage;
        this.country = country;
        this.major = major;
        this.preferredLanguage = preferredLanguage;
        this.interests = interests;
        this.feeds = feeds;
    }

    public static UserProfileResponse of(User user, List<InterestResponse> interests, List<FeedCreateResponse> feeds) {
        return new UserProfileResponse(user.getId(), user.getName(), user.getProfileImage(), user.getCountry(), user.getMajor(), user.getPreferredLanguage(), interests, feeds);
    }
}
