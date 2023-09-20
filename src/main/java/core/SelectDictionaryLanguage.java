package core;

import java.util.HashMap;
import java.util.Map;

public class SelectDictionaryLanguage {

    private Map<Long, String> dictionaryLanguage = new HashMap<>();

    public String getDictionaryLanguage(long chatId) {
        return dictionaryLanguage.get(chatId);
    }

    public void putDictionaryLanguage(long chatId, String language) {
        dictionaryLanguage.put(chatId, language);
    }
}