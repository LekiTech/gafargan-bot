package core.dictionary.parser;

import core.dictionary.model.ExpressionDetails;

import java.util.List;
import java.util.Map;

public class JsonDictionary implements DictionaryRepository {
    private Map<String, List<ExpressionDetails>> dictionary;

    @Override
    public void setDictionary(Map<String, List<ExpressionDetails>> parsedDictionary) throws Exception {
        if (parsedDictionary == null) {
            throw new IllegalArgumentException("parsedDictionary cannot be null");
        }
        if (this.dictionary != null) {
            throw new Exception("Cannot set dictionary twice");
        }
        this.dictionary = parsedDictionary;
    }

    @Override
    public List<ExpressionDetails> getDefinitions(String spelling) {
        if (spelling != null) {
            String spellingLowered = spelling.toLowerCase();
            return dictionary.get(spellingLowered);
        }
        return null;
    }

    @Override
    public Map<String, List<ExpressionDetails>> getFullDictionary() {
        return dictionary;
    }
}