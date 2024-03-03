package core.searchers;

import core.dictionary.parser.DictionaryRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static core.searchers.StringSimilarity.similarity;

public class FuzzySearchBySpelling {

    public Response findSimilarWordsBySpelling(String lang, DictionaryRepository dictionary, String userMessage) {
        record WordSim(String supposedWord, Double sim) {
        }
        List<WordSim> wordList = new ArrayList<>();
        for (String supposedWord : dictionary.getDictionaryByLang(lang).keySet()) {
            double sim = similarity(supposedWord, userMessage.toLowerCase());
            if (sim >= 0.5) {
                wordList.add(new WordSim(supposedWord.replaceAll("i", "I"), sim));
            }
        }
        wordList.sort(Comparator.comparing(WordSim::sim).reversed());
        if (wordList.subList(0, Math.min(7, wordList.size())).size() == 0) {
            return new Response("<b>❌Жагъай гаф авач</b>");
        }
        List<String> supposedWords = new ArrayList<>();
        for (WordSim word : wordList.subList(0, Math.min(7, wordList.size()))) {
            supposedWords.add(word.supposedWord);
        }
        return new Response("\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n", supposedWords);
    }
}