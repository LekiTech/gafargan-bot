package core.searchers;

import core.dictionary.model.Example;
import core.dictionary.model.ExpressionDetails;
import core.dictionary.parser.DictionaryRepository;

import java.util.*;

import static core.utils.MarkupLineEditor.convertMarkupToHTML;
import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class SearchByExample {

    public Response findResponseByExamples(DictionaryRepository dictionaries, String userMessage) {
        final Map<String, List<ExpressionDetails>> combinedDictionary = dictionaries.getAllDictionaries();
        List<ExpressionDetails> combinedList = combinedDictionary.values().stream()
                .flatMap(List::stream)
                .toList();
        final List<String> foundExamples = new ArrayList<>();
        if (!combinedList.isEmpty()) {
            List<String> expressionExample = combinedList.stream()
                    .filter(expressionDetails -> expressionDetails.getExamples() != null)
                    .flatMap(expressionDetails -> expressionDetails.getExamples().stream())
                    .map(Example::getRaw)
                    .filter(raw -> cleanseText(raw).contains(userMessage))
                    .toList();
            if (!expressionExample.isEmpty()) {
                foundExamples.addAll(expressionExample);
            }
            List<String> definitionExample = combinedList.stream()
                    .flatMap(expressionDetails -> expressionDetails.getDefinitionDetails().stream())
                    .filter(definitionDetails -> definitionDetails.getExamples() != null)
                    .flatMap(definitionDetails -> definitionDetails.getExamples().stream())
                    .map(Example::getRaw)
                    .filter(raw -> cleanseText(raw).contains(userMessage))
                    .toList();
            if (!definitionExample.isEmpty()) {
                foundExamples.addAll(definitionExample);
            }
        }
        if (!foundExamples.isEmpty()) {
            StringBuilder outputMessage = new StringBuilder("<i>" + capitalizeFirstLetter(userMessage) + "</i> ⤵️️️\n\n");
            int numberOfExamples = 0;
            for (String example : foundExamples) {
                outputMessage.append(convertMarkupToHTML(example)
                        .replaceAll(userMessage, "<u>" + userMessage + "</u>")).append("\n");
                numberOfExamples++;
                if (numberOfExamples >= 10) {
                    return new Response(outputMessage.toString());
                }
            }
            return new Response(outputMessage.toString());
        }
        return null;
    }

    private String cleanseText(String raw) {
        return raw
                .replaceAll("[,.?!]", "")
                .replaceAll("ё", "е");
    }
}