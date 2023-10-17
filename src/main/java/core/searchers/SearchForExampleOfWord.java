package core.searchers;

import core.StringCorrection;
import core.parser.DictionaryRepository;
import core.parser.model.Example;
import core.parser.model.ExpressionDetails;

import java.util.List;

public class SearchForExampleOfWord {

    public Answer sendExample(DictionaryRepository dictionary, String spelling) {
        StringBuilder outputMessage = new StringBuilder();
        try {
            String[] spell = spelling.split("=");
            if (spell.length == 2) {
                spelling = spell[0];
            }
            outputMessage.append("<i>").append(spelling.substring(0, 1).toUpperCase()).append(spelling.substring(1)
                    .replaceAll("[i1lӏ|]", "I")).append("</i> ⤵️\n\n");
            List<ExpressionDetails> expressionDetails = dictionary.getDefinitions(spelling);
            StringCorrection correction = new StringCorrection();
            for (ExpressionDetails details : expressionDetails) {
                if (details.getExamples() != null) {
                    int count = 0;
                    for (Example examples : details.getExamples()) {
                        outputMessage.append(correction.lineEdit(examples.getRaw().replaceAll("ё", "е"))).append("\n");
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
        return new Answer(outputMessage.toString());
    }
}