package core.dictionary.parser;

import core.dictionary.model.ExpressionDetails;

import java.util.List;
import java.util.Map;

public interface DictionaryRepository {

    void setDictionary(String langId, Map<String, List<ExpressionDetails>> parsedDictionary) throws Exception;

    Map<String, List<ExpressionDetails>> getAllDictionaries();

    List<ExpressionDetails> getExpressionDetails(String lang, String spelling);

    Map<String, List<ExpressionDetails>> getDictionaryByLang(String lang);
}