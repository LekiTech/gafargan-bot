package core;

import core.parser.DictionaryRepository;
import core.parser.model.DefinitionDetails;
import core.parser.model.Example;
import core.parser.model.ExpressionDetails;

import java.util.*;

public class Examples {

    public Map<String, Set<String>> getAll(List<DictionaryRepository> listOfDictionary) {
        Map<String, Set<String>> resultExamples = new HashMap<>();
        for (DictionaryRepository dictionaryRepository : listOfDictionary) {
            for (List<ExpressionDetails> expressionDetails : dictionaryRepository.getFullDictionary().values()) {
                for (ExpressionDetails details : expressionDetails) {
                    for (DefinitionDetails definitionDetails : details.getDefinitionDetails()) {
                        if (definitionDetails.getExamples() != null) {
                            writingExamplesToMap(definitionDetails.getExamples(), resultExamples);
                        }
                    }
                    if (details.getExamples() != null) {
                        writingExamplesToMap(details.getExamples(), resultExamples);
                    }
                }
            }
        }
        return resultExamples;
    }

    private static void writingExamplesToMap(List<Example> allExample, Map<String, Set<String>> resultExamples) {
        for (Example example : allExample) {
            String[] words = example.getRaw().split("[^а-яА-ЯёЁiI1lӏ|Ӏ]");
            for (String word : words) {
                if (!word.isEmpty()) {
                    String spell = word.replaceAll("ё", "е");
                    Set<String> temp = new HashSet<>();
                    temp.add(example.getRaw().replaceAll("ё", "е"));
                    if (resultExamples.containsKey(spell)) {
                        resultExamples.get(spell).addAll(temp);
                    } else {
                        resultExamples.put(spell, temp);
                    }
                }
            }
        }
    }
}