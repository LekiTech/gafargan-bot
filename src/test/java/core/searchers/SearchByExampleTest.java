package core.searchers;

import core.Examples;
import core.parser.DictionaryParser;
import core.parser.DictionaryRepository;
import core.parser.JsonDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SearchByExampleTest {

    private final DictionaryRepository lezgiRusDictionary = new JsonDictionary();
    private final DictionaryRepository rusLezgiDictionary = new JsonDictionary();
    public Map<String, Set<String>> listOfExample;
    DictionaryParser dictionaryParser = new DictionaryParser();

    @BeforeEach
    public void initDictionaries() throws Exception {
        lezgiRusDictionary.setDictionary(dictionaryParser.parse("lezgi_rus_dict_babakhanov_v2.json"));
        rusLezgiDictionary.setDictionary(dictionaryParser.parse("rus_lezgi_dict_hajiyev_v2.json"));
        var examples = new Examples();
        listOfExample = examples.getAll(List.of(lezgiRusDictionary, rusLezgiDictionary));
    }

    @Test
    void sendAnswerFromExamples() {
        String input = "РикIелай алатна";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>РикIелай алатна</i> ⤵️️️

                <b><i>   - всё ушло из памяти</i></b> —  [разг.] вири <u>рикIелай алатна</u>, вири фикирдай акъатна
                <b><i>   - из головы вон </i></b> — фикирдай акъатна, <u>рикIелай алатна</u>
                <b><i>   - травой поросло</i></b> —  <u>рикIелай алатна</u>, садан рикIелни аламач
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples1() {
        String input = "Гъил къачу";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>Гъил къачу</i> ⤵️️️

                <b><i>   - завай са хата хьана, <u>гъил къачу</u></i></b> —  я ошибся, прости
                <b><i>   - инжиклу хьанатIа, <u>гъил къачу</u></i></b> —  если обидел, извини
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples2() {
        String input = "Фимир";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>Фимир</i> ⤵️️️

                <b><i>   - ламран кьулухъайни бегдин виликай <u>фимир</u></i></b> —  [погов.] сзади осла и впереди бека не ходи
                <b><i>   - если уж поздно, то не ходи</i></b> —  эгер геж ятIа, <u>фимир</u>
                <b><i>   - не смей ходить</i></b> —  <u>фимир</u>
                <b><i>   - санизни <u>фимир</u></i></b> —  никуда не ходи
                <b><i>   - а патахъ <u>фимир</u></i></b> —  не ходи в ту сторону
                <b><i>   - рекьиз нагьар тавуна <u>фимир</u></i></b> —  не отправляйся в путь без завтрака
                <b><i>   - не ходи</i></b> —  <u>фимир</u>
                <b><i>   - анихъ <u>фимир</u></i></b> —  туда не ходи
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples3() {
        String input = "ам вуж я?";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>ам вуж я?</i> ⤵️️️

                <b><i>   - кто такой он</i></b> —  <u>ам вуж я</u>?
                <b><i>   - <u>ам вуж я</u> ана манидик кекянавайди?</i></b> —  это кто там распелся? <b><i>   - гадади бригадирдин гуьгъуьниз кекяна</i></b> —  парень пустился вдогонку за бригадиром
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples4() {
        String input = "ви шумуд йис я?";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>ви шумуд йис я?</i> ⤵️️️

                <b><i>   - сколько тебе лет?</i></b> —  <u>ви шумуд йис я</u>?
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples5() {
        String input = "ви тIвар вуж я?";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>ви тIвар вуж я?</i> ⤵️️️

                <b><i>   -  как твоё имя?</i></b> —  <u>ви тIвар вуж я</u>?
                <b><i>   - как тебя звать (зовут)?</i></b> —  <u>ви тIвар вуж я</u>?
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples6() {
        String input = "вун атуй рагъ атуй";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>вун атуй рагъ атуй</i> ⤵️️️

                <b><i>   - вун атуй, рагъ атуй!</i></b> —  [межд.] ты пришёл, солнце пришло!
                <b><i>   - вун атуй, рагъ атуй!</i></b> —  [межд]. добро пожаловать!
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples7() {
        String input = "вун гьинай я?";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>вун гьинай я?</i> ⤵️️️

                <b><i>   - жузун айиб тахьуй, <u>вун гьинай я</u>?</i></b> —  извините, откуда вы?
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }

    @Test
    void sendAnswerFromExamples8() {
        String input = "вун гьинай я?";
        Answer answer = new SearchByExample().sendAnswerFromExamples(listOfExample, lezgiRusDictionary, input);
        String actualMessage = answer.messageText();
        String expected = """
                <i>вун гьинай я?</i> ⤵️️️

                <b><i>   - жузун айиб тахьуй, <u>вун гьинай я</u>?</i></b> —  извините, откуда вы?
                """;
        assertThat(actualMessage).isEqualTo(expected);
    }
}