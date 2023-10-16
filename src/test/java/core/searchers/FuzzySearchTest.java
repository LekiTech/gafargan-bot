package core.searchers;

import core.parser.DictionaryParser;
import core.parser.DictionaryRepository;
import core.parser.JsonDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FuzzySearchTest {


    private final DictionaryRepository lezgiRusDictionary = new JsonDictionary();
    private final DictionaryRepository rusLezgiDictionary = new JsonDictionary();
    DictionaryParser dictionaryParser = new DictionaryParser();

    @BeforeEach
    public void initDictionaries() throws Exception {
        lezgiRusDictionary.setDictionary(dictionaryParser.parse("lezgi_rus_dict_babakhanov_v2.json"));
        rusLezgiDictionary.setDictionary(dictionaryParser.parse("rus_lezgi_dict_hajiyev_v2.json"));
    }

    @Test
    void whenWordNotFound() {
        String input = "ывфоларфвыолаофываолыфвифвыоафы";
        Answer answer = new FuzzySearch().sendAnswerWithSupposedWords(rusLezgiDictionary, input);
        String actualMessage = answer.messageText();
        String expected = "<b>❌Жагъай гаф авач</b>";
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerWithSuggestedWords() {
        String input = "рыш";
        Answer answer = new FuzzySearch().sendAnswerWithSupposedWords(lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        List<String> actualExampleButton = answer.exampleButton();
        String expectedMessage = "\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n";
        List<String> expectedVocabularyList = List.of("руш", "ериш", "аруш", "раши");
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualExampleButton).isEqualTo(expectedVocabularyList);
    }

    @Test
    void sendAnswerWithSuggestedWords1() {
        String input = "хъарнихъуз";
        Answer answer = new FuzzySearch().sendAnswerWithSupposedWords(lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        List<String> actualExampleButton = answer.exampleButton();
        String expectedMessage = "\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n";
        List<String> expectedVocabularyList = List.of("къарникъуз", "хъархъу", "хъарт-хъурт", "къарихдиз", "тарихсуз", "анихъун", "хъурхъуш");
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualExampleButton).isEqualTo(expectedVocabularyList);
    }

    @Test
    void sendAnswerWithSuggestedWords2() {
        String input = "ходитьь";
        Answer answer = new FuzzySearch().sendAnswerWithSupposedWords(rusLezgiDictionary, input);
        String actualMessage = answer.messageText();
        List<String> actualExampleButton = answer.exampleButton();
        String expectedMessage = "\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n";
        List<String> expectedVocabularyList = List.of("ходить", "уходить", "водить", "родить", "сходить", "холить", "входить");
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualExampleButton).isEqualTo(expectedVocabularyList);
    }
}