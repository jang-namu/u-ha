package inu.helloforeigner.domain.feed.dto;

import inu.helloforeigner.domain.feed.domain.Feed;
import lombok.Getter;

@Getter
public class FeedUpdateResponse {

    private Long id;
    private String image;
    private String content;
    private Integer numOfLikes;
    private String updatedAt;

    private FeedUpdateResponse(Long id, String image, String content, Integer numOfLikes, String updatedAt) {
        this.id = id;
        this.image = image;
        this.content = content;
        this.numOfLikes = numOfLikes;
        this.updatedAt = updatedAt;
    }

    public static FeedUpdateResponse from(Feed feed) {
        return new FeedUpdateResponse(feed.getId(), feed.getImage(), feed.getContent(), feed.getNumOfLikes(), feed.getCreatedAt().toString());
    }
}
