package core.searchers;

import core.dictionary.parser.DictionaryRepository;
import core.dictionary.model.Example;
import core.dictionary.model.ExpressionDetails;
import core.utils.OutputLineEditor;

import java.util.List;
import java.util.stream.Collectors;

import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class SearchForExampleExpression {

    public Response sendExampleExpression(String lang, DictionaryRepository dictionaries, String spelling) {
        String[] spell = spelling.split("=");
        if (spell.length == 2) {
            spelling = spell[0];
        }
        final List<ExpressionDetails> expressionDetails = dictionaries.getExpressionDetails(lang, spelling);
        if (!expressionDetails.isEmpty()) {
            List<String> expressionExample = expressionDetails.stream()
                    .filter(expDetails -> expDetails.getExamples() != null)
                    .flatMap(expDetails -> expDetails.getExamples().stream())
                    .map(Example::getRaw)
                    .toList();
            if (!expressionExample.isEmpty()) {
                return new Response(
                        capitalizeFirstLetter(spelling)
                                + expressionExample.stream()
                                .limit(20)
                                .map(OutputLineEditor::convertMarkupToHTML)
                                .collect(Collectors.joining("\n"))
                                + "\n"
                );
            }
        }
        return null;
    }
}