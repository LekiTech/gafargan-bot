package core.searchers;

import core.util.LineCorrection;
import core.parser.DictionaryRepository;
import javassist.NotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchByExample {

    public Answer sendAnswerFromExamples(Map<String, Set<String>> listOfExample, DictionaryRepository dictionary,
                                         String userMessage) {
        StringBuilder outputMessage = new StringBuilder("<i>" + userMessage + "</i> ⤵️️️\n\n");
        String inputMessage = userMessage.toLowerCase().replaceAll("i", "I");
        try {
            Set<String> tempExamples;
            String[] msg = inputMessage.split("[^а-яА-ЯёЁiI1lӏ|Ӏ]");
            if (msg.length > 1) {
                tempExamples = listOfExample.get(msg[0]);
            } else {
                tempExamples = listOfExample.get(inputMessage);
            }
            if (tempExamples == null) {
                throw new NotFoundException("example not found!");
            }
            int numberOfExamples = 0;
            String cleanInputMsg = inputMessage.replaceAll("[,?!.;]", "");
            String wordToFind = "\\b" + Pattern.quote(cleanInputMsg) + "\\b";
            Pattern pattern = Pattern.compile(wordToFind, Pattern.UNICODE_CHARACTER_CLASS);
            LineCorrection correction = new LineCorrection();
            for (String example : tempExamples) {
                String tempEx = example.replaceAll("[,?!.;]", "");
                Matcher matcher = pattern.matcher(tempEx);
                if (matcher.find()) {
                    outputMessage.append(correction.lineEdit(example)
                            .replaceAll(cleanInputMsg, "<u>" + cleanInputMsg + "</u>")).append("\n");
                    numberOfExamples++;
                    if (numberOfExamples >= 10) {
                        break;
                    }
                }
            }
            if (numberOfExamples == 0) {
                throw new NotFoundException("example not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new FuzzySearch().sendAnswerWithSupposedWords(dictionary, userMessage);
        }
        return new Answer(outputMessage.toString());
    }
}