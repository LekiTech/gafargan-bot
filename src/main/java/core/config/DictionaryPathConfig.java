package core.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DictionaryPathConfig {

    @Value("${lez_rus_dict}")
    private String lezRusDict;

    @Value("${rus_lez_dict}")
    private String rusLezDict;

    public String getFilePath(String dictionaryKey) {
        return switch (dictionaryKey) {
            case "lez" -> lezRusDict;
            case "rus" -> rusLezDict;
            default -> null;
        };
    }
}
