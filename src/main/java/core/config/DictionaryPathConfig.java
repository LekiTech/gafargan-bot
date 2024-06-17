package core.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static core.commands.CommandsList.*;

@Getter
@Configuration
public class DictionaryPathConfig {

    @Value("${lez.rus}")
    private String lezRusDict;

    @Value("${rus.lez}")
    private String rusLezDict;

    @Value("${lez.eng}")
    private String lezEngDict;

    public String getFilePath(String dictionaryKey) {
        return switch (dictionaryKey) {
            case LEZ -> lezRusDict;
            case RUS -> rusLezDict;
            case ENG -> lezEngDict;
            default -> null;
        };
    }
}
