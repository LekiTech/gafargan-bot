package core.dictionary.parser;

import core.dictionary.model.ExpressionDetails;
import javassist.NotFoundException;

import java.util.List;
import java.util.Map;

public interface DictionaryRepository {

    void setDictionary(Map<String, List<ExpressionDetails>> parsedDictionary) throws Exception;

    List<ExpressionDetails> getDefinitions(String spelling) throws NotFoundException;

    Map<String, List<ExpressionDetails>> getFullDictionary();
}