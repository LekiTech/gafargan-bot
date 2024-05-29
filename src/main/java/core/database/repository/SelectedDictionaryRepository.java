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
            SELECT sd FROM selected_dictionary sd WHERE sd.user_chat_id = :chatId
            """,
            nativeQuery = true)
    SelectedDictionary findSelectedDictionaryByUserChatId(@Param("chatId") Long chatId);
//
//    @Query("""
//            UPDATE selected_dictionary sd SET sd.dictionary = :dictionary WHERE sd.userChatId = :chatId""",
//            nativeQuery = true)
//    void updateSelectedDictionaryByUserChatId(SelectedDictionary dictionary, Long chatId);
}
