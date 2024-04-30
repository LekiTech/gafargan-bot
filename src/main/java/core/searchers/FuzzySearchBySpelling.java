package core.searchers;

import core.dictionary.parser.DictionaryRepository;

import java.util.Comparator;
import java.util.List;

import static core.searchers.StringSimilarity.similarity;
import static core.utils.OutputLineEditor.insertAuthorsName;

public class FuzzySearchBySpelling implements Searcher {

    @Override
    public Response searchResponse(String lang, DictionaryRepository dictionary, String userMessage) {
        record WordSim(String supposedWord, Double sim) {
        }
        final List<WordSim> wordList = dictionary.getDictionaryByLang(lang).keySet().stream()
                .parallel()
                .map(supposedWord -> new WordSim(supposedWord.replaceAll("i", "I"), similarity(supposedWord, userMessage.toLowerCase())))
                .filter(wordSim -> wordSim.sim() >= 0.5)
                .sorted(Comparator.comparing(WordSim::sim).reversed())
                .limit(7)
                .toList();
        if (wordList.isEmpty()) {
            return new Response("<b>❌Гаф жагъанач</b>\n\uD83D\uDCDA" + insertAuthorsName(lang));
        }
        final List<String> supposedWords = wordList.stream()
                .map(WordSim::supposedWord)
                .toList();
        return new Response("\uD83E\uDD14жагъай гаф авач, ибуруз килиг:\n", supposedWords);
    }
}