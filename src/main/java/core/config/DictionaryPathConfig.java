package core.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static core.commands.CommandsList.*;

@Getter
@Component
public class DictionaryPathConfig {

    @Value("${lez.rus}")
    private String lezRusDict;

    @Value("${rus.lez}")
    private String rusLezDict;

    @Value("${lez.eng}")
    private String lezEngDict;

    @Value("${dialect.dict}")
    private String dialectDict;

    public String getFilePath(String dictionaryKey) {
        return switch (dictionaryKey) {
            case LEZGI_RUS -> lezRusDict;
            case RUS_LEZGI -> rusLezDict;
            case LEZGI_ENG -> lezEngDict;
            case LEZGI_DIALECT_DICT -> dialectDict;
            default -> null;
        };
    }
}
