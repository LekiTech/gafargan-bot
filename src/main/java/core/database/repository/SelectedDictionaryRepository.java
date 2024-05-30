package core.database.repository;

import core.database.entity.SelectedDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SelectedDictionaryRepository extends JpaRepository<SelectedDictionary, UUID> {

    @Query(value = """
            SELECT dictionary FROM selected_dictionary WHERE user_chat_id = :chatId
            """,
            nativeQuery = true)
    String findSelectedDictionaryByUserChatId(@Param("chatId") Long chatId);

    @Query(value = """
            UPDATE selected_dictionary SET dictionary = :dictionary, created_at = now()
            WHERE user_chat_id = :chatId
            """,
            nativeQuery = true)
    Boolean updateSelectedDictionaryByUserChatId(@Param("dictionary") String dictionary, @Param("chatId") Long chatId);
}
