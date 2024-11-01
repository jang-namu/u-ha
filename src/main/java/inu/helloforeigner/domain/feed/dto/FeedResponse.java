package inu.helloforeigner.domain.feed.dto;

import inu.helloforeigner.domain.feed.domain.Feed;
import inu.helloforeigner.domain.user.domain.User;
import inu.helloforeigner.domain.user.dto.InterestResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class FeedResponse {

    private Long id;
    private String image;
    private String content;
    private Integer numOfLikes;
    private String createdAt;
    private UserDto user;

    private FeedResponse(Long id, String image, String content, Integer numOfLikes, String createdAt, UserDto userDto) {
        this.id = id;
        this.image = image;
        this.content = content;
        this.numOfLikes = numOfLikes;
        this.createdAt = createdAt;
        this.user = userDto;
    }

    public static FeedResponse of(Feed feed, User user, List<InterestResponse> interests) {
        return new FeedResponse(feed.getId(), feed.getImage(), feed.getContent(), feed.getNumOfLikes(), feed.getCreatedAt().toString(), UserDto.of(user, interests));
    }

    @Getter
    public static class UserDto {
        private Long userId;
        private String name;
        private String profileImage;
        private String country;
        private String major;
        private List<InterestResponse> interests;

        private UserDto(Long userId, String name, String profileImage, String country, String major, List<InterestResponse> interests) {
            this.userId = userId;
            this.name = name;
            this.profileImage = profileImage;
            this.country = country;
            this.major = major;
            this.interests = interests;
        }

        private static UserDto of(User user, List<InterestResponse> interests) {
            return new UserDto(user.getId(), user.getName(), user.getProfileImage(), user.getCountry(), user.getMajor(), interests);
        }
    }
}
