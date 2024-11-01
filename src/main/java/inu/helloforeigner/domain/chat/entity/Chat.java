package inu.helloforeigner.domain.chat.entity;

import inu.helloforeigner.common.BaseTimeEntity;
import inu.helloforeigner.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chats")
public class Chat extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType type;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    private boolean isFiltered;

    @OneToMany(mappedBy = "chat")
    private List<TranslateChat> translations = new ArrayList<>();

    @Builder
    public Chat(ChatRoom chatRoom, ChatType type, String content, User sender) {
        this.chatRoom = chatRoom;
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.isFiltered = false;
        chatRoom.updateLastMessageAt();
    }

    public void filtering() {
        this.isFiltered = true;
    }
}
