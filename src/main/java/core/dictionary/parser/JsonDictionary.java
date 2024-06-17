package core.dictionary.parser;

import core.dictionary.model.ExpressionDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonDictionary implements DictionaryRepository {

    private final Map<String, Map<String, List<ExpressionDetails>>> dictionary = new HashMap<>();
    /* Temporary code due to the fact that the JSON format of the Lezgi-English dictionary is different from other dictionaries. */
    private final Map<String, List<String>> lezEngDictionary = new HashMap<>();

    @Override
    public Map<String, List<String>> getLezgiEngDict() {
        return lezEngDictionary;
    }

    @Override
    public void setLezEngDictionary(Map<String, List<String>> dict) {
        lezEngDictionary.putAll(dict);
    }

    @Override
    public List<String> getFromLezEngDictionary(String spelling) {
        if (spelling != null && !spelling.isEmpty()) {
            return lezEngDictionary.get(spelling.toLowerCase()) == null ? new ArrayList<>() : lezEngDictionary.get(spelling.toLowerCase());
        }
        return null;
    }

    @Override
    public void setDictionary(String langId, Map<String, List<ExpressionDetails>> parsedDictionary) throws Exception {
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