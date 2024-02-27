package core.searchers;

import core.config.DictionaryConfigReader;
import core.dictionary.parser.DictionaryRepository;
import core.dictionary.parser.JsonDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static core.dictionary.parser.DictionaryParser.parse;
import static org.assertj.core.api.Assertions.*;

class FuzzySearchBySpellingTest {

    private final DictionaryConfigReader dictionaryConfig = new DictionaryConfigReader();
    private final DictionaryRepository lezgiRusDictionary = new JsonDictionary();
    private final DictionaryRepository rusLezgiDictionary = new JsonDictionary();

    @BeforeEach
    public void initDictionaries() throws Exception {
        lezgiRusDictionary.setDictionary(parse(dictionaryConfig.getFilePath("lez_rus_dict")));
        rusLezgiDictionary.setDictionary(parse(dictionaryConfig.getFilePath("rus_lez_dict")));
    }

    @Test
    void whenWordNotFound() {
        String input = "ывфоларфвыолаофываолыфвифвыоафы";
        Response response = new FuzzySearchBySpelling().findSimilarWordsBySpelling(rusLezgiDictionary, input);
        String actualMessage = response.messageText();
        String expected = "<b>❌Жагъай гаф авач</b>";
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerWithSuggestedWords() {
        String input = "рыш";
        Response response = new FuzzySearchBySpelling().findSimilarWordsBySpelling(lezgiRusDictionary, input);
        String actualMessage = response.messageText();
        List<String> actualExampleButton = response.exampleButton();
        String expectedMessage = "\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n";
        List<String> expectedVocabularyList = List.of("руш", "ериш", "аруш", "раши");
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualExampleButton).isEqualTo(expectedVocabularyList);
    }

    @Test
    void sendAnswerWithSuggestedWords1() {
        String input = "хъарнихъуз";
        Response response = new FuzzySearchBySpelling().findSimilarWordsBySpelling(lezgiRusDictionary, input);
        String actualMessage = response.messageText();
        List<String> actualExampleButton = response.exampleButton();
        String expectedMessage = "\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n";
        List<String> expectedVocabularyList = List.of("къарникъуз", "хъархъу", "хъарт-хъурт", "къарихдиз", "тарихсуз", "анихъун", "хъурхъуш");
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualExampleButton).isEqualTo(expectedVocabularyList);
    }

    @Test
    void sendAnswerWithSuggestedWords2() {
        String input = "ходитьь";
        Response response = new FuzzySearchBySpelling().findSimilarWordsBySpelling(rusLezgiDictionary, input);
        String actualMessage = response.messageText();
        List<String> actualExampleButton = response.exampleButton();
        String expectedMessage = "\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n";
        List<String> expectedVocabularyList = List.of("ходить", "уходить", "водить", "родить", "сходить", "холить", "входить");
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualExampleButton).isEqualTo(expectedVocabularyList);
    }
}