package core.searchers;

import core.dictionary.model.Example;
import core.dictionary.model.ExpressionDetails;
import core.dictionary.parser.DictionaryRepository;
import core.utils.OutputLineEditor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static core.commands.CommandsList.LEZ;
import static core.commands.CommandsList.RUS;
import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class SearchByExample implements Searcher {

    @Override
    public Response searchResponse(String lang, DictionaryRepository dictionaries, String userMessage) {
        // TODO исправить методы capitalizeFirstLetter(WithNum); убрать замену "ё" в outputMsg в классе SearchBySpelling
        /* This condition will need to be removed when the JSON format of other dictionaries is updated to version two. */
        if (!lang.equals(LEZ) && !lang.equals(RUS)) {
            return null;
        }
        final Map<String, List<ExpressionDetails>> combinedDictionary = dictionaries.getAllDictionaries();
        List<ExpressionDetails> combinedList = combinedDictionary.values().stream()
                .flatMap(List::stream)
                .toList();
        List<String> foundExamples = new ArrayList<>();
        if (!combinedList.isEmpty()) {
            foundExamples = Stream.concat(
                    combinedList.stream()
                            .parallel()
                            .filter(expressionDetails -> expressionDetails.getExamples() != null)
                            .flatMap(expressionDetails -> expressionDetails.getExamples().stream())
                            .map(Example::getRaw)
                            .filter(raw -> cleanseText(raw).contains(userMessage)),
                    combinedList.stream()
                            .parallel()
                            .flatMap(expressionDetails -> expressionDetails.getDefinitionDetails().stream())
                            .filter(definitionDetails -> definitionDetails.getExamples() != null)
                            .flatMap(definitionDetails -> definitionDetails.getExamples().stream())
                            .map(Example::getRaw)
                            .filter(raw -> cleanseText(raw).contains(userMessage))
            ).toList();
        }
        if (!foundExamples.isEmpty()) {
            return new Response(
                    capitalizeFirstLetter(userMessage)
                    + foundExamples.stream()
                            .limit(10)
                            .map(OutputLineEditor::convertMarkupToHTML)
                            .map(row -> row.replaceAll(userMessage, "<u>" + userMessage + "</u>"))
                            .collect(Collectors.joining("\n"))
                    + "\n"
            );
        }
        return null;
    }

    private String cleanseText(String raw) {
        return raw
                .replaceAll("[,.?!]", "")
                .replaceAll("ё", "е");
    }
}