package core.dictionary.parser;

import core.dictionary.model.ExpressionDetails;
import javassist.NotFoundException;

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
    public List<ExpressionDetails> getDefinitions(String spelling) throws NotFoundException {
        if (spelling == null) {
            throw new IllegalArgumentException("spelling cannot be null");
        }
        String spellingLowered = spelling.toLowerCase();
        if (!this.dictionary.containsKey(spellingLowered)) {
            throw new NotFoundException("Spelling '" + spellingLowered + "' not found");
        }
        return dictionary.get(spellingLowered);
    }

    @Override
    public Map<String, List<ExpressionDetails>> getFullDictionary() {
        return dictionary;
    }
}