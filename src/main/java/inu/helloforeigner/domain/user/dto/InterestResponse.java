package inu.helloforeigner.domain.user.dto;

import inu.helloforeigner.domain.interest.entity.Interest;
import inu.helloforeigner.domain.user.domain.UserInterest;
import lombok.Getter;

@Getter
public class InterestResponse {
    private Long id;
    private String content;
    private Integer score;

    public InterestResponse(Long id, String content, Integer score) {
        this.id = id;
        this.content = content;
        this.score = score;
    }

    public static InterestResponse from(UserInterest userInterest, Interest interest) {
        return new InterestResponse(userInterest.getId(), interest.getContent(), userInterest.getScore());
    }
}
