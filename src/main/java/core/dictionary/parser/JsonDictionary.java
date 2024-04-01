package core.dictionary.parser;

import core.dictionary.model.ExpressionDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonDictionary implements DictionaryRepository {

    private final Map<String, Map<String, List<ExpressionDetails>>> dictionary = new HashMap<>();

    @Override
    public void setDictionaryByLang(String langId, Map<String, List<ExpressionDetails>> parsedDictionary) throws Exception {
        if (langId == null || parsedDictionary == null) {
            throw new IllegalArgumentException("lang or parsedDictionary cannot be null");
        }
        if (dictionary.containsKey(langId)) {
            throw new Exception("Cannot set dictionary twice");
        }
        this.dictionary.put(langId, parsedDictionary);
    }

    @Override
    public Map<String, List<ExpressionDetails>> getAllDictionaries() {
        if (!dictionary.isEmpty()) {
            final Map<String, List<ExpressionDetails>> combinedDictionary = new HashMap<>();
            combinedDictionary.putAll(dictionary.get("lez"));
            combinedDictionary.putAll(dictionary.get("rus"));
            return combinedDictionary;
        }
        return null;
    }

    @Override
    public List<ExpressionDetails> getExpressionDetails(String lang, String spelling) {
        if (lang != null && spelling != null && dictionary.containsKey(lang)) {
            return dictionary.get(lang).get(spelling.toLowerCase());
        }
        return null;
    }

    @Override
    public Map<String, List<ExpressionDetails>> getDictionaryByLang(String lang) {
        if (lang != null) {
            return dictionary.get(lang);
        }
        return null;
    }
}