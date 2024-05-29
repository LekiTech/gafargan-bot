package core.database.repository;

import core.database.entity.UserChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatIdRepository extends JpaRepository<UserChatId, Long> {
}