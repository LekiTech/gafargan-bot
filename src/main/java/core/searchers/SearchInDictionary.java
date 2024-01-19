package core.searchers;

import core.dictionary.parser.DictionaryRepository;
import core.dictionary.model.Definition;
import core.dictionary.model.DefinitionDetails;
import core.dictionary.model.ExpressionDetails;

import java.util.List;

import static core.correction.LineEditor.editLine;
import static core.correction.WordCapitalize.capitalizeFirstLetter;
import static core.main.BotUpdates.listOfExample;
import static core.factory.CommandsFactory.COMMAND_EXAMPLE_SUFFIX;

public class SearchInDictionary {

    public Answer sendAnswerFromDictionary(DictionaryRepository dictionary, String spelling) {
        String userMessage = capitalizeFirstLetter(spelling);
        int amountOfValues = 1;
        boolean generalExample = false;
        StringBuilder tags = new StringBuilder();
        StringBuilder outputMessage = new StringBuilder();
        try {
            List<ExpressionDetails> expressionDetails = dictionary.getDefinitions(spelling);
            for (ExpressionDetails details : expressionDetails) {
                /* Если слово есть в словаре, но нет перевода, вывести "перевод не найден" */
                if (details.getDefinitionDetails().isEmpty() && details.getExamples() == null) {
                    return new Answer("<b>❌Таржума жагъанач</b>");
                }
                /* Если к слову отсутвуют переводы, есть только examples, то вывести example */
                if ((details.getDefinitionDetails().size() == 0 || details.getDefinitionDetails() == null)
                        && details.getExamples() != null) {
                    return new Answer(sendOnlyExamples(userMessage, outputMessage, details));
                }
                /* Если у слова несколько или одно значений, то пронумеровать */
                markupExpressionsSpelling(userMessage, amountOfValues, outputMessage, expressionDetails, details);
                for (DefinitionDetails definitionDetails : details.getDefinitionDetails()) {
                    StringBuilder values = new StringBuilder();
                    for (Definition definitions : definitionDetails.getDefinitions()) {
                        boolean isTagNull = definitions.getTags() == null;
                        tags.append(!isTagNull && definitions.getTags().contains("см.")
                                ? definitions.getValue().replaceAll("\\{", "").replaceAll("}", "") + ", "
                                : ""
                        );
                        values.append(isTagNull || !definitions.getTags().contains("см.")
                                ? definitions.getValue().replaceAll("[<{]", "[").replaceAll("[>}]", "]") + ", "
                                : "");
                    }
                    if (!values.isEmpty()) { /* Присваиваем value и удаляем запятую на конце */
                        outputMessage
                                .append("➡️<b>️ ")
                                .append(values.deleteCharAt(values.length() - 2))
                                .append("</b>\n\n");
                    }
                    if (definitionDetails.getExamples() != null) {
                        definitionDetails.getExamples().forEach(example -> {
                            outputMessage.append(editLine(example.getRaw())).append("\n");
                        });
                        outputMessage.append("\n");
                    }
                }
                if (!generalExample) { /* Проверяем наличие общего example */
                    generalExample = details.getExamples() != null;
                }
                amountOfValues++;
            }
            /* Если теги есть, присваиваем к концу ответной строки */
            if (!tags.isEmpty()) {
                outputMessage.append(createTags(tags));
            }
            /* Выводим ответ с наличием общих примеров */
            if (generalExample) {
                return new Answer(outputMessage.toString().replaceAll("ё", "е"),
                        List.of(userMessage.toLowerCase() + COMMAND_EXAMPLE_SUFFIX));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SearchByExample().sendAnswerFromExamples(listOfExample, dictionary, userMessage);
        }
        return new Answer(outputMessage.toString().replaceAll("ё", "е"));
    }

    private void markupExpressionsSpelling(String userMessage, int amountOfValues, StringBuilder outputMessage,
                                           List<ExpressionDetails> expressionDetails, ExpressionDetails details) {
        if (expressionDetails.size() > 1) {
            outputMessage.append(details.getInflection() != null ?
                    "<i>" + amountOfValues + ". " + userMessage + " (" + details.getInflection() + ")</i> ⤵️\n\n"
                    : "<i>" + amountOfValues + ". " + userMessage + "️</i> ⤵️️\n\n");
        } else {
            outputMessage.append(details.getInflection() != null ?
                    "<i>" + userMessage + " (" + details.getInflection() + ")</i> ⤵️\n\n"
                    : "<i>" + userMessage + "</i> ⤵️\n\n");
        }
    }

    private String sendOnlyExamples(String userMessage, StringBuilder outputMessage, ExpressionDetails details) {
        outputMessage.append("<i>").append(userMessage).append("️</i> ⤵️\n\n");
        details.getExamples().forEach(example -> {
            outputMessage.append(editLine(example.getRaw())).append("\n");
        });
        return outputMessage.toString().replaceAll("ё", "е");
    }

    private StringBuilder createTags(StringBuilder tags) {
        StringBuilder result = new StringBuilder("\n<i>мадни килиг:</i> ");
        String[] tagArr = tags.toString().split(", ");
        for (String tag : tagArr) {
            if (!tag.isEmpty()) {
                result.append("<code>").append(tag).append("</code>").append(", ");
            }
        }
        return result.deleteCharAt(result.length() - 2);
    }
}