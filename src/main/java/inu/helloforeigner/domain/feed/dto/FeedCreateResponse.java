package inu.helloforeigner.domain.feed.dto;

import inu.helloforeigner.domain.feed.domain.Feed;
import lombok.Getter;

@Getter
public class FeedCreateResponse {

    private Long id;
    private String image;
    private String content;
    private Integer numOfLikes;
    private String createdAt;

    private FeedCreateResponse(Long id, String image, String content, Integer numOfLikes, String createdAt) {
        this.id = id;
        this.image = image;
        this.content = content;
        this.numOfLikes = numOfLikes;
        this.createdAt = createdAt;
    }

    public static FeedCreateResponse from(Feed feed) {
        return new FeedCreateResponse(feed.getId(), feed.getImage(), feed.getContent(), feed.getNumOfLikes(), feed.getCreatedAt().toString());
    }
}
