package inu.helloforeigner.domain.user.dto;

import lombok.Getter;

@Getter
public class InterestRequest {
    private String interest;
    private Integer score;

    public InterestRequest(String interest, Integer score) {
        this.interest = interest;
        this.score = score;
    }
}
