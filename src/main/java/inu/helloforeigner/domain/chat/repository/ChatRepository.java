package inu.helloforeigner.domain.chat.repository;

import inu.helloforeigner.domain.chat.entity.Chat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    // lastMessageId가 null일 때 최신 메시지부터 size만큼 조회
    @Query("SELECT c FROM Chat c " +
            "WHERE c.chatRoom.id = :roomId " +
            "ORDER BY c.createdAt DESC")
    Page<Chat> findTopByRoomIdOrderByCreatedAtDesc(
            @Param("roomId") Long roomId,
            Pageable pageable
    );

    @Query("SELECT c FROM Chat c " +
            "WHERE c.chatRoom.id = :roomId " +
            "AND c.id < :lastMessageId " +
            "ORDER BY c.createdAt DESC")
    Page<Chat> findByRoomIdAndIdLessThanOrderByCreatedAtDesc(
            @Param("roomId") Long roomId,
            @Param("lastMessageId") Long lastMessageId,
            Pageable pageable
    );

}
