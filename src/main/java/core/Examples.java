package core;

import core.parser.DictionaryRepository;
import core.parser.model.DefinitionDetails;
import core.parser.model.Example;
import core.parser.model.ExpressionDetails;

import java.util.ArrayList;
import java.util.List;

public class Examples {

    public static List<Example> getAll(List<DictionaryRepository> listOfDictionary) {
        List<Example> examples = new ArrayList<>();
        for (DictionaryRepository dictionaryRepository : listOfDictionary) {
            for (List<ExpressionDetails> expressionDetails : dictionaryRepository.getFullDictionary().values()) {
                for (ExpressionDetails details : expressionDetails) {
                    for (DefinitionDetails definitionDetails : details.getDefinitionDetails()) {
                        if (definitionDetails.getExamples() != null) {
                            examples.addAll(definitionDetails.getExamples());
                        }
                    }
                    if (details.getExamples() != null) {
                        examples.addAll(details.getExamples());
                    }
                }
            }
        }
        return examples;
    }
}