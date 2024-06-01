//package core.searchers;
//
//import core.dictionary.parser.DictionaryRepository;
//import core.dictionary.parser.JsonDictionary;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
//import static core.dictionary.parser.DictionaryParser1.parse;
//import static org.assertj.core.api.Assertions.*;
//
//@Disabled
//class SearchByExampleTest {
//
//    private final DictionaryRepository dictionaries = new JsonDictionary();
//
//    @BeforeEach
//    public void initDictionaries() throws Exception {
//        dictionaries.setDictionary("lez", parse("lez_rus_dict"));
//        dictionaries.setDictionary("rus", parse("rus_lez_dict"));
//    }
//
//    @Test
//    void sendAnswerFromExamples() {
//        String input = "рикIелай алатна";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>РикIелай алатна</i> ⤵️️️
//
//                <b><i>   - из головы вон </i></b> — фикирдай акъатна, <u>рикIелай алатна</u>
//                <b><i>   - травой поросло</i></b> —  <u>рикIелай алатна</u>, садан рикIелни аламач
//                <b><i>   - всё ушло из памяти</i></b> —  [разг.] вири <u>рикIелай алатна</u>, вири фикирдай акъатна
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples1() {
//        String input = "гъил къачу";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Гъил къачу</i> ⤵️️️
//
//                <b><i>   - поставить крест </i></b> — <u>гъил къачу</u>н, вил атIун, умуд атIун; куьтягь хьайидай гьисабун
//                <b><i>   - <u>гъил къачу</u>н</i></b> —  [гл]. прощать
//                <b><i>   - не давать спуску</i></b> —  <u>гъил къачу</u>н тавун; гьакI ахъай тавун; эвез агакьар тавуна тутун; гьакI алат тавун
//                <b><i>   - принести повинную</i></b> —  багъишламиша лугьун, тахсирдилай <u>гъил къачу</u>н тIалабун
//                <b><i>   - инжиклу хьанатIа, <u>гъил къачу</u></i></b> —  если обидел, извини
//                <b><i>   - <u>гъил къачу</u>н</i></b> —  [гл.] 1) прощать, извинять; 2) отказаться (от чего-л.)
//                <b><i>   - вымолить себе прощение </i></b> — минетна вичелай <u>гъил къачу</u>з тун
//                <b><i>   - пойти на уступки</i></b> —  бязи истемишунрилай <u>гъил къачу</u>н
//                <b><i>   - отпущение грехов</i></b> —  гунагьрилай <u>гъил къачу</u>н, гунагьар багъишламашун.
//                <b><i>   - замолить грехи</i></b> —  гунагьрилай <u>гъил къачу</u>н тIалабун
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples2() {
//        String input = "фимир";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Фимир</i> ⤵️️️
//
//                <b><i>   - не смей ходить</i></b> —  <u>фимир</u>
//                <b><i>   - ламран кьулухъайни бегдин виликай <u>фимир</u></i></b> —  [погов.] сзади осла и впереди бека не ходи
//                <b><i>   - анихъ <u>фимир</u></i></b> —  туда не ходи
//                <b><i>   - раз дело обстоит так, не уезжай</i></b> —  эгер кар акI ятIа (кар акI яз хьайила, я кьван), хъ<u>фимир</u>
//                <b><i>   - не ходи</i></b> —  <u>фимир</u>
//                <b><i>   - если уж поздно, то не ходи</i></b> —  эгер геж ятIа, <u>фимир</u>
//                <b><i>   - санизни <u>фимир</u></i></b> —  никуда не ходи
//                <b><i>   - а патахъ <u>фимир</u></i></b> —  не ходи в ту сторону
//                <b><i>   - не уходи, побудь с детьми</i></b> —  хъ<u>фимир</u>, са тIимил аялрихъ галаз акъваз (хьухь)
//                <b><i>   - рекьиз нагьар тавуна <u>фимир</u></i></b> —  не отправляйся в путь без завтрака
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples3() {
//        String input = "ам вуж я";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Ам вуж я</i> ⤵️️️
//
//                <b><i>   - кто такой он</i></b> —  <u>ам вуж я</u>?
//                <b><i>   - <u>ам вуж я</u> ана манидик кекянавайди?</i></b> —  это кто там распелся? <b><i>   - гадади бригадирдин гуьгъуьниз кекяна</i></b> —  парень пустился вдогонку за бригадиром
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples4() {
//        String input = "ви шумуд йис я";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Ви шумуд йис я</i> ⤵️️️
//
//                <b><i>   - сколько тебе лет?</i></b> —  <u>ви шумуд йис я</u>?
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples5() {
//        String input = "ви тIвар вуж я";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Ви тIвар вуж я</i> ⤵️️️
//
//                <b><i>   -  как твоё имя?</i></b> —  <u>ви тIвар вуж я</u>?
//                <b><i>   - как тебя звать (зовут)?</i></b> —  <u>ви тIвар вуж я</u>?
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples6() {
//        String input = "вун атуй рагъ атуй";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Вун атуй рагъ атуй</i> ⤵️️️
//
//                <b><i>   - вун атуй, рагъ атуй!</i></b> —  [межд.] ты пришёл, солнце пришло!
//                <b><i>   - вун атуй, рагъ атуй!</i></b> —  [межд]. добро пожаловать!
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples7() {
//        String input = "вун гьинай я";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Вун гьинай я</i> ⤵️️️
//
//                <b><i>   - <u>вун гьинай я</u>ни лугьун тавун</i></b> —  гл. не признавать, не считаться
//                <b><i>   - жузун айиб тахьуй, <u>вун гьинай я</u>?</i></b> —  извините, откуда вы?
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples9() {
//        String input = "мое сердце";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Мое сердце</i> ⤵️️️
//
//                <b><i>   - ваз хьайи кар фикирдиз атай кьван зи рикI гьайифдив ацIузва</i></b> —  каждый раз, когда я думаю о случившемся с тобой, моё сердце наполняется скорбью
//                <b><i>   - агь, ада зи рикI кана хьи</i></b> —  ах, он испепелил моё сердце!
//                <b><i>   - ада зи рикI цIурурзава</i></b> —  он терзает моё сердце
//                <b><i>   - адан язух къведай акунри зи рикI чIулаварна</i></b> —  его жалкий вид омрачил моё сердце
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void sendAnswerFromExamples10() {
//        String input = "твое имя";
//        Response response = new SearchByExample().searchResponse(null, dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Твое имя</i> ⤵️️️
//
//                <b><i>   -  как твоё имя?</i></b> —  ви тIвар вуж я?
//                """;
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//}