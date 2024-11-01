package inu.helloforeigner.domain.chat.repository;

import inu.helloforeigner.domain.chat.entity.ChatRoom;
import inu.helloforeigner.domain.chat.entity.ChatUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    List<ChatUser> findAllByUserId(Long userId);

    List<ChatUser> findAllByChatRoom(ChatRoom chatRoom);
}
