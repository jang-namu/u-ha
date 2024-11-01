package inu.helloforeigner.domain.feed.dto;

import lombok.Getter;

@Getter
public class FeedLikeResponse {

    private Long feedId;
    private Boolean liked;
    private Integer numOfLikes;

    private FeedLikeResponse(Long feedId, Boolean liked, Integer numOfLikes) {
        this.feedId = feedId;
        this.liked = liked;
        this.numOfLikes = numOfLikes;
    }

    public static FeedLikeResponse from(Long feedId, Boolean liked, Integer numOfLikes) {
        return new FeedLikeResponse(feedId, liked, numOfLikes);
    }
}
