package core.dictionary.parser;

import core.dictionary.model.DefinitionDetails;
import core.dictionary.model.Example;
import core.dictionary.model.ExpressionDetails;

import java.util.*;

public class ExamplesParsing {

    public Map<String, Set<String>> getAllExamples(List<DictionaryRepository> listOfDictionary) {
        Map<String, Set<String>> result = new HashMap<>();
        for (DictionaryRepository dictionaryRepository : listOfDictionary) {
            for (List<ExpressionDetails> expressionDetails : dictionaryRepository.getFullDictionary().values()) {
                for (ExpressionDetails details : expressionDetails) {
                    for (DefinitionDetails definitionDetails : details.getDefinitionDetails()) {
                        if (definitionDetails.getExamples() != null) {
                            writingExamplesToMap(definitionDetails.getExamples(), result);
                        }
                    }
                    if (details.getExamples() != null) {
                        writingExamplesToMap(details.getExamples(), result);
                    }
                }
            }
        }
        return result;
    }

    private static void writingExamplesToMap(List<Example> allExample, Map<String, Set<String>> result) {
        for (Example example : allExample) {
            String[] words = example.getRaw().split("[^а-яА-ЯёЁiI1lӏ|Ӏ]");
            for (String word : words) {
                if (!word.isEmpty()) {
                    String spell = word.replaceAll("ё", "е");
                    Set<String> temp = new HashSet<>();
                    temp.add(example.getRaw().replaceAll("ё", "е"));
                    if (result.containsKey(spell)) {
                        result.get(spell).addAll(temp);
                    } else {
                        result.put(spell, temp);
                    }
                }
            }
        }
    }
}