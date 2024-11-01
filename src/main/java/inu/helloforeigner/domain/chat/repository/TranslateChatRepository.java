package inu.helloforeigner.domain.chat.repository;

import inu.helloforeigner.domain.chat.entity.TranslateChat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslateChatRepository extends JpaRepository<TranslateChat, Long> {
    List<TranslateChat> findAllByChatId(Long chatId);
}
