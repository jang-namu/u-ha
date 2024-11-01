package inu.helloforeigner.domain.chat.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupChatCreateRequest {
    List<Long> participantIds;

    public GroupChatCreateRequest(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
}