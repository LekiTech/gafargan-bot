package core.dictionary.parser;

import core.dictionary.model.DialectDictionary;
import core.dictionary.model.ExpressionDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static core.commands.CommandsList.*;

@Component
public class JsonDictionary implements DictionaryRepository {

    @Autowired
    private DictionaryParser dictionaryParser;
    private final Map<String, Map<String, List<ExpressionDetails>>> dictionary = new HashMap<>();
    private final Map<String, List<DialectDictionary.Dialect>> dialectDictionary = new HashMap<>();

    @PostConstruct
    private void init() {
        dictionary.put(LEZ, dictionaryParser.parse(LEZ));
        dictionary.put(RUS, dictionaryParser.parse(RUS));
        dictionary.put(ENG, dictionaryParser.parse(ENG));
        dialectDictionary.putAll(dictionaryParser.parseDialectDict(DIALECT_DICT));
    }

    @Override
    public Map<String, List<DialectDictionary.Dialect>> getDialectDictionary() {
        return dialectDictionary;
    }

    @Override
    public void setDialectDictionary(Map<String, List<DialectDictionary.Dialect>> dict) {
        dialectDictionary.putAll(dict);
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
            combinedDictionary.putAll(dictionary.get(LEZ));
            combinedDictionary.putAll(dictionary.get(RUS));
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