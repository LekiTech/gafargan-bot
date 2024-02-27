package core.searchers;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.utils.MarkupLineEditor.convertMarkupToHTML;

public class SearchByExample {

    public Response findTranslationByExamples(Map<String, Set<String>> listOfExample, String userMessage) {
        StringBuilder outputMessage = new StringBuilder("<i>" + userMessage + "</i> ⤵️️️\n\n");
        String inputMessage = userMessage.toLowerCase().replaceAll("i", "I");
        Set<String> tempExamples;
        String[] msg = inputMessage.split("[^а-яА-ЯёЁiI1lӏ|Ӏ]");
        if (msg.length > 1) {
            tempExamples = listOfExample.get(msg[0]);
        } else {
            tempExamples = listOfExample.get(inputMessage);
        }
        if (tempExamples == null) {
            return null;
        }
        int numberOfExamples = 0;
        String cleanInputMsg = inputMessage.replaceAll("[,?!.;]", "");
        String wordToFind = "\\b" + Pattern.quote(cleanInputMsg) + "\\b";
        Pattern pattern = Pattern.compile(wordToFind, Pattern.UNICODE_CHARACTER_CLASS);
        for (String example : tempExamples) {
            String tempEx = example.replaceAll("[,?!.;]", "");
            Matcher matcher = pattern.matcher(tempEx);
            if (matcher.find()) {
                outputMessage.append(convertMarkupToHTML(example)
                        .replaceAll(cleanInputMsg, "<u>" + cleanInputMsg + "</u>")).append("\n");
                numberOfExamples++;
                if (numberOfExamples >= 10) {
                    break;
                }
            }
        }
        if (numberOfExamples == 0) {
            return null;
        }
        return new Response(outputMessage.toString());
    }
}