package by.mluvse.javabot.model.repository;

import by.mluvse.javabot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByChatId(Long chatId);
}
