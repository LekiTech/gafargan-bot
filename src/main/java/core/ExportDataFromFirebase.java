package core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.database.entity.SelectedDictionary;
import core.database.entity.UserChatId;
import core.database.service.SelectedDictionaryService;
import core.database.service.UserChatIdService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.sql.Timestamp;
import java.util.UUID;

@SpringBootApplication
public class ExportDataFromFirebase {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        ApplicationContext context = SpringApplication.run(ExportDataFromFirebase.class, args);
        UserChatIdService userChatIdService = context.getBean(UserChatIdService.class);
        SelectedDictionaryService selectedDictionaryService = context.getBean(SelectedDictionaryService.class);
        try {
            DictionaryEntry[] entries = mapper
                    .readValue(new File("src/main/resources/gafargan-bot-manual-export-timestamps.json"), DictionaryEntry[].class);
            for (DictionaryEntry entry : entries) {
                long userChatId = Long.parseLong(entry.getId());
                Timestamp timestamp = new Timestamp(entry.getTimestamp());

                userChatIdService.saveUser(new UserChatId(userChatId, timestamp));
                selectedDictionaryService.saveDictionary(
                        new SelectedDictionary(
                                UUID.randomUUID(),
                                entry.getDictionary().replaceAll("\"", ""),
                                new UserChatId(userChatId, timestamp),
                                timestamp)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DictionaryEntry {

        private String id;

        @JsonIgnore
        private String createdAt;

        @JsonProperty("dictionary")
        private String dictionary;

        @JsonProperty("timestamp")
        private long timestamp;
    }
}
