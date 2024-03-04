package core.searchers;

import core.dictionary.parser.DictionaryRepository;
import core.dictionary.model.Example;
import core.dictionary.model.ExpressionDetails;

import java.util.List;

import static core.utils.MarkupLineEditor.convertMarkupToHTML;
import static core.utils.SearchStringNormalizer.normalizeString;
import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class SearchForExampleExpression {

    public Response sendExampleExpression(String lang, DictionaryRepository dictionaries, String spelling) {
        String[] spell = spelling.split("=");
        if (spell.length == 2) {
            spelling = spell[0];
        }
        List<ExpressionDetails> expressionDetails = dictionaries.getExpressionDetails(lang, spelling);
        List<String> expressionExample = expressionDetails.stream()
                .filter(expDetails -> expDetails.getExamples() != null)
                .flatMap(expDetails -> expDetails.getExamples().stream())
                .map(Example::getRaw)
                .toList();
        if (!expressionExample.isEmpty()) {
            StringBuilder outputMessage = new StringBuilder().append(capitalizeFirstLetter(normalizeString(spelling)));
            int count = 0;
            for (String example : expressionExample) {
                outputMessage.append(convertMarkupToHTML(example)).append("\n");
                if (count >= 20) {
                    break;
                }
                count++;
            }
            return new Response(outputMessage.toString());
        }
        return null;
    }
}