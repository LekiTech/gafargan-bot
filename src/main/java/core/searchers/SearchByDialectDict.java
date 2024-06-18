package core.searchers;

import core.dictionary.model.Definition;
import core.dictionary.model.DefinitionDetails;
import core.dictionary.model.DialectDictionary;
import core.dictionary.model.ExpressionDetails;
import core.dictionary.parser.DictionaryRepository;

import java.util.*;

import static core.utils.OutputLineEditor.convertMarkupToHTML;
import static core.utils.OutputLineEditor.insertAuthorsName;
import static core.utils.WordCapitalize.capitalizeFirstLetter;
import static core.utils.WordCapitalize.capitalizeFirstLetterWithNum;

public class SearchByDialectDict implements Searcher {

    @Override
    public Response searchResponse(String lang, DictionaryRepository dictionaries, String spelling) {
        Map<String, List<DialectDictionary.Dialect>> dialectDictionary = dictionaries.getDialectDictionary();
        String literaryDialectWord = "";
        String dialectWord = "";
        final List<String> dialectNames = new ArrayList<>();
        boolean flag = false;
        for (Map.Entry<String, List<DialectDictionary.Dialect>> entry : dialectDictionary.entrySet()) {
            for (DialectDictionary.Dialect dialect : entry.getValue()) {
                if (dialect.getSpelling().equals(spelling)) {
                    literaryDialectWord = entry.getKey();
                    dialectWord = dialect.getSpelling();
                    dialectNames.add(dialect.getName());
                    flag = true;
                }
            }
            if (flag) {
                spelling = literaryDialectWord;
                break;
            }
        }
        if (literaryDialectWord.isEmpty() && dialectWord.isEmpty()) {
            return null;
        }
        final List<ExpressionDetails> expressionDetails = dictionaries.getExpressionDetails(lang, spelling);
        if (expressionDetails == null) {
            return null;
        }
        StringBuilder outputMessage = new StringBuilder();
        for (String dialectName : dialectNames) {
            outputMessage
                    .append("<i>")
                    .append(dialectWord.substring(0, 1).toUpperCase())
                    .append(dialectWord.substring(1))
                    .append("</i> ")
                    .append("(<b>")
                    .append(dialectName)
                    .append("</b> нугъат)")
                    .append("\n");
        }
        outputMessage
                .append("<i>")
                .append(spelling.substring(0, 1).toUpperCase())
                .append(spelling.substring(1))
                .append("</i> ").append("(<b>")
                .append("литератур.")
                .append("</b> нугъат)")
                .append("\n\n");
        int amountOfDetails = 1;
        boolean expressionExample = false;
        StringBuilder tags = new StringBuilder();
        for (ExpressionDetails details : expressionDetails) {
            /* Если слово есть в словаре, но нет перевода, вывести "перевод не найден" */
            if (details.getDefinitionDetails().isEmpty() && details.getExamples() == null) {
                return new Response("<b>❌Гафуниз таржума жагъанач</b>\n\uD83D\uDCDA" + insertAuthorsName(lang));
            }
            /* Если к слову отсутвуют переводы, есть только examples, то вывести example */
            if ((details.getDefinitionDetails().size() == 0 || details.getDefinitionDetails() == null)
                && details.getExamples() != null) {
                outputMessage.append(capitalizeFirstLetter(spelling));
                return new Response(sendOnlyExamples(outputMessage, details, lang));
            }
            /* Если у слова несколько или одно значений, то пронумеровать */
            markupExpressionsSpelling(spelling, amountOfDetails, outputMessage, expressionDetails, details);
            for (DefinitionDetails definitionDetails : details.getDefinitionDetails()) {
                StringBuilder definitionValues = new StringBuilder();
                for (Definition definitions : definitionDetails.getDefinitions()) {
                    boolean isTagNull = definitions.getTags() == null;
                    tags.append(!isTagNull && definitions.getTags().contains("см.")
                            ? definitions.getValue().replaceAll("\\{", "").replaceAll("}", "") + ", "
                            : ""
                    );
                    definitionValues.append(isTagNull || !definitions.getTags().contains("см.")
                            ? definitions.getValue().replaceAll("[<{]", "[").replaceAll("[>}]", "]") + ", "
                            : "");
                }
                if (!definitionValues.isEmpty()) { /* Присваиваем value и удаляем запятую на конце */
                    outputMessage
                            .append("➡️<b>️ ")
                            .append(definitionValues.deleteCharAt(definitionValues.length() - 2))
                            .append("</b>\n\n");
                }
                if (definitionDetails.getExamples() != null) {
                    definitionDetails.getExamples().forEach(example -> {
                        outputMessage.append(convertMarkupToHTML(example.getRaw())).append("\n");
                    });
                    outputMessage.append("\n");
                }
            }
            if (!expressionExample) {
                expressionExample = details.getExamples() != null;
            }
            amountOfDetails++;
        }
        /* присваиваем к концу ответной строки тэги и автора словаря */
        if (!tags.isEmpty()) {
            outputMessage.append(createTags(tags));
            outputMessage.append("\n\n").append(insertAuthorsName(lang));
        } else {
            outputMessage.append(insertAuthorsName(lang));
        }
        /* Выводим ответ с наличием общих примеров */
        if (expressionExample) {
            return new Response(outputMessage.toString().replaceAll("ё", "е"),
                    List.of(spelling.toLowerCase() + "=example"));
        } else {
            return new Response(outputMessage.toString());
        }
    }

    private void markupExpressionsSpelling(String userMessage, int amountOfDetails, StringBuilder outputMessage,
                                           List<ExpressionDetails> expressionDetails, ExpressionDetails details) {
        if (expressionDetails.size() > 1) {
            outputMessage.append(details.getInflection() != null
                    ? capitalizeFirstLetterWithNum(amountOfDetails + ". ", userMessage + " (" + details.getInflection() + ")")
                    : capitalizeFirstLetterWithNum(amountOfDetails + ". ", userMessage));
        } else {
            outputMessage.append(details.getInflection() != null
                    ? capitalizeFirstLetter(userMessage + " (" + details.getInflection() + ")")
                    : capitalizeFirstLetter(userMessage));
        }
    }

    private String sendOnlyExamples(StringBuilder outputMessage, ExpressionDetails details, String lang) {
        details.getExamples().forEach(example -> {
            outputMessage.append(convertMarkupToHTML(example.getRaw())).append("\n");
        });
        outputMessage.append("\n").append(insertAuthorsName(lang));
        return outputMessage.toString().replaceAll("ё", "е");
    }

    private StringBuilder createTags(StringBuilder tags) {
        StringBuilder result = new StringBuilder("\n<b>\uD83D\uDD0Eмадни килиг:</b> ");
        String[] tagArr = tags.toString().split(", ");
        Set<String> tagList = new HashSet<>(Arrays.asList(tagArr));
        for (String tag : tagList) {
            if (!tag.isEmpty()) {
                result.append("<code>").append(tag).append("</code>").append(", ");
            }
        }
        return result.deleteCharAt(result.length() - 2);
    }
}