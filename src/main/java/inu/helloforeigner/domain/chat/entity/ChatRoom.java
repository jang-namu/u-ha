package inu.helloforeigner.domain.chat.entity;

import inu.helloforeigner.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "chat_rooms")
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lastMessageAt;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatUser> chatUsers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chats = new ArrayList<>();

    @Builder
    protected ChatRoom() {
        updateLastMessageAt();
    }

    public static ChatRoom of() {
        return new ChatRoom();
    }

    public void addChatUser(ChatUser chatUser) {
        this.chatUsers.add(chatUser);
    }

    public void updateLastMessageAt() {
        this.lastMessageAt = LocalDateTime.now();
    }
}
