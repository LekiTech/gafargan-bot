package core.searchers;

import core.dictionary.parser.DictionaryRepository;
import core.dictionary.model.Example;
import core.dictionary.model.ExpressionDetails;

import java.util.List;

import static core.utils.MarkupLineEditor.convertMarkupToHTML;
import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class SearchByExamplesOfSpelling {

    public Response sendExample(DictionaryRepository dictionary, String spelling) {
        StringBuilder outputMessage = new StringBuilder();
        try {
            String[] spell = spelling.split("=");
            if (spell.length == 2) {
                spelling = spell[0];
            }
            outputMessage.append("<i>").append(capitalizeFirstLetter(spelling).replaceAll("[i1lӏ|]", "I"))
                    .append("</i> ⤵️\n\n");
            List<ExpressionDetails> expressionDetails = dictionary.getDefinitions(spelling);
            for (ExpressionDetails details : expressionDetails) {
                if (details.getExamples() != null) {
                    int count = 0;
                    for (Example examples : details.getExamples()) {
                        outputMessage.append(convertMarkupToHTML(examples.getRaw().replaceAll("ё", "е"))).append("\n");
                        if (count > 20) {
                            break;
                        }
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(outputMessage.toString());
    }
}