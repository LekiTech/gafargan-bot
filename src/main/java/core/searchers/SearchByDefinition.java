package core.searchers;

import core.dictionary.model.Definition;
import core.dictionary.model.ExpressionDetails;
import core.dictionary.parser.DictionaryRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static core.commands.CommandsList.*;
import static core.utils.OutputLineEditor.insertAuthorsName;
import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class SearchByDefinition implements Searcher {

    @Override
    public Response searchResponse(String lang, DictionaryRepository dictionaries, String userMessage) {
        /* This condition will need to be changed when the JSON format of other dictionaries is updated to version two. */
        if (lang.equals(LEZGI_RUS) || lang.equals(RUS_LEZGI)) {
            lang = lang.equals(LEZGI_RUS) ? RUS_LEZGI : LEZGI_RUS;
        } else {
            return null;
        }
        final Map<String, List<ExpressionDetails>> dictionary = dictionaries.getDictionaryByLang(lang);
        List<String> spellings = searchSpellings(dictionary, defValue -> defValue.equals(userMessage));
        if (!spellings.isEmpty()) {
            return createOutputMessage(userMessage, spellings, lang);
        }
        return null;
    }

    private List<String> searchSpellings(Map<String, List<ExpressionDetails>> dictionary, Predicate<String> predicate) {
        return dictionary.entrySet().stream()
                .filter(entry -> entry.getValue().stream()
                        .flatMap(expDetails -> expDetails.getDefinitionDetails().stream())
                        .flatMap(defDetails -> defDetails.getDefinitions().stream())
                        .filter(def -> def != null)
                        .filter(def -> def.getTags() == null || !def.getTags().contains("см."))
                        .map(Definition::getValue)
                        .anyMatch(predicate)
                )
                .map(Map.Entry::getKey)
                .limit(7)
                .toList();
    }

    private Response createOutputMessage(String userMessage, List<String> spellings, String lang) {
        StringBuilder outputMessage = new StringBuilder(capitalizeFirstLetter(userMessage));
        for (String spelling : spellings) {
            outputMessage.append("<b>➡️ ").append(spelling).append("</b>\n\n");
        }
        outputMessage.append(insertAuthorsName(lang));
        return new Response(outputMessage.toString(), List.of(userMessage + "=searchMore"));
    }
}
