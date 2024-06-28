//package core.searchers;
//
//import core.dictionary.parser.DictionaryRepository;
//import core.dictionary.parser.JsonDictionary;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static core.dictionary.parser.DictionaryParser1.parse;
//import static core.utils.OutputLineEditor.insertAuthorsName;
//import static org.assertj.core.api.Assertions.*;
//
//@Disabled
//class SearchBySpellingTest {
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
//    void whenTranslateIsFound() {
//        String input = "спасибо";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Спасибо</i> ⤵️️️
//
//                ➡️<b>️ сагърай, чухсагъул </b>
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsNotFound() {
//        String input = "фирдавай";
//        Response response = new SearchBySpelling().searchResponse("lez",dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = "<b>❌Гафуниз таржума жагъанач</b>\n\uD83D\uDCDA" + insertAuthorsName("lez");
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenWordHasNoTranslationAndOnlyAnExample() {
//        String input = "финдикь";
//        Response response = new SearchBySpelling().searchResponse("lez", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Финдикь</i> ⤵️️️
//
//                <b><i>   - финдикьни акъуд тавун</i></b> —  [гл.] молчать, не пикнуть, слова не вымолвить
//
//                <i>Бабаханов М.Б. "Лезги-урус гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound1() {
//        String input = "рикI";
//        Response response = new SearchBySpelling().searchResponse("lez", dictionaries, input);
//        String actualMessage = response.messageText();
//        List<String> actualExampleButton = response.suggestions();
//        String expected = """
//                <i>РикI (-и, -е, -ери)</i> ⤵️️️
//
//                ➡️<b>️ сердце </b>
//
//                <b><i>   - рикIин</i></b> —  сердечный
//                <b><i>   - рикIин тIал</i></b> —  сердечная боль; сердечная болезнь
//
//                ➡️<b>️ сердце, душа, внутренний мир человека </b>
//
//                <b><i>   - рикIин</i></b> —  душевный; сердечный
//                <b><i>   - рикIин дуст</i></b> —  сердечный друг
//                <b><i>   - рикI алай шаир</i></b> —  любимый поэт
//                <b><i>   - рикIе затI авачирди</i></b> —  простак
//
//                ➡️<b>️ в сочетании с некоторыми глаголами может выражать значение сохранения чего-л. в памяти, мысленного воспроизведение этого: [ам зи рикIел алама] я его помню </b>
//
//                <b><i>   - рикIелай алат тийидай кар</i></b> —  незабываемое дело
//                <b><i>   - пака зи рикIел хкваш</i></b> —  завтра напомни мне
//
//
//                <b>🔎мадни килиг:</b> <code>къелби</code>\s
//
//                <i>Бабаханов М.Б. "Лезги-урус гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//        assertThat(actualExampleButton).isNotEmpty();
//    }
//
//    @Test
//    void whenTranslateIsFound2() {
//        String input = "къвал";
//        Response response = new SearchBySpelling().searchResponse("lez", dictionaries, input);
//        String actualMessage = response.messageText();
//        List<String> actualExampleButton = response.suggestions();
//        String expected = """
//                <i>1. Къвал (-а, -а, -ари)</i> ⤵️️️
//
//                ➡️<b>️ бок </b>
//
//                <b><i>   - къвал лацу кал</i></b> —  белобокая корова
//                <b><i>   - са къвалахъ къаткун</i></b> —  ложиться набок
//                <b><i>   - къвал къвалаз яна</i></b> —  бок о бок
//                <b><i>   - къвал чIуькьвез акъвазун</i></b> —  ежиться, мяться (стесняясь)
//                <b><i>   - къвалан</i></b> —  боковой
//
//                ➡️<b>️ боковая стенка </b>
//
//                <b><i>   - шкафдин къвал</i></b> —  стенка шкафа
//                <b><i>   - катулдин къвалар цIал чIулав хьанва</i></b> —  стенки кастрюли покрылись черным нагаром
//
//                ➡️<b>️ фланг, сторона </b>
//
//                <b><i>   - эрчIи къвала кавалерия акъвазарна</i></b> —  на правый фланг поставили кавалерию
//
//                <i>2. Къвал (-ди, -да, -ри)</i> ⤵️️️
//
//                ➡️<b>️ дождь </b>
//
//                <b><i>   - къвал акатна</i></b> —  начался дождь
//                <b><i>   - къвал квай югъ</i></b> —  дождливый день
//
//
//                <b>🔎мадни килиг:</b> <code>пагв</code>\s
//
//                <i>Бабаханов М.Б. "Лезги-урус гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//        assertThat(actualExampleButton).isNotEmpty();
//    }
//
//    @Test
//    void whenTranslateIsFound3() {
//        String input = "гада";
//        Response response = new SearchBySpelling().searchResponse("lez", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Гада (-ди, -да, -йри)</i> ⤵️️️
//
//                ➡️<b>️ мальчик </b>
//
//                <b><i>   - вад гада</i></b> —  пять мальчиков
//                <b><i>   - гадайри яд гъизва, рушари класс михьзава</i></b> —  мальчики носят воду, девочки убирают класс
//
//                ➡️<b>️ сын </b>
//
//                <b><i>   - адаз пуд гада ава</i></b> —  у него трое сыновей
//                <b><i>   - жуван гададиз лагь</i></b> —  скажи своему сыну
//
//                ➡️<b>️ парень </b>
//
//                <b><i>   - гадаяр</i></b> —  ребята
//                <b><i>   - ам хуш къилихрин жегьил гада я</i></b> —  он благонравный молодой парень
//
//                <i>Бабаханов М.Б. "Лезги-урус гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound4() {
//        String input = "кхьихь";
//        Response response = new SearchBySpelling().searchResponse("lez", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Кхьихь</i> ⤵️️️
//
//                ➡️<b>️ повел. [ф] от [кхьин] </b>
//
//                <i>Бабаханов М.Б. "Лезги-урус гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound5() {
//        String input = "что";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>1. Что</i> ⤵️️️
//
//                ➡️<b>️ вуч </b>
//
//                <b><i>   - что это такое?</i></b> —  им вуч я?
//                <b><i>   - чего ты ждешь?</i></b> —  вуна вуч гуьзлемишзава?
//                <b><i>   - чему ты смеется?</i></b> —  вун кквел хъуьрезва?
//                <b><i>   - чем?</i></b> —  кквелди?
//                <b><i>   - о чем?</i></b> —  кквекай? ккуьн гьакъиндай?
//                <b><i>   - на чем?</i></b> —  кквел?
//
//                ➡️<b>️ гьикI я? гьикI хьана? вуч хьана? </b>
//
//                <b><i>   - ну что же, ты едешь?</i></b> —  гьан гьикI хьана, вун физвани?
//
//                ➡️<b>️ вуч? вучиз? </b>
//
//                <b><i>   - что ты лежишь?</i></b> —  вун вучиз къатканва?
//
//                ➡️<b>️ вуч? гьикьван? ккуьн? </b>
//
//                <b><i>   - что стоит эта книжка?</i></b> —  и ктаб ккуьн ква?
//
//                ➡️<b>️ са кар, са затI </b>
//
//                <b><i>   - если что случится, я не отвечаю</i></b> —  нагагь са кар хьайитIа, за жаваб гудач.
//
//                <i>2. Что</i> ⤵️️️
//
//                ➡️<b>️ хьи, лагьана (гзаф вахтара перевод ийидач) </b>
//
//                <b><i>   - он ответил, что они уехали</i></b> —  ада, абур хъфена лагьана, жаваб гана
//                <b><i>   - он увидел, что-дело плохо</i></b> —  адаз акуна хьи, кар пис я
//                <b><i>   - я знаю, что он не приедет </i></b> — ам къвен тийирди заз чизва (ва я заз чизва хьи, ам къведач)
//
//                ➡️<b>️ гьар, гьар са, кьилиз </b>
//
//                <b><i>   - что ни день, то новые желания</i></b> —  гьар йкъуз цIийи-цIийи мурадар ава
//
//                ➡️<b>️ кIантIа, кIантIа…. хьурай </b>
//
//                <b><i>   - мне все равно, что у меня, что у тебя собраться</i></b> —  кIантIа вина кIватI жен, кIантIа зина -заз сад я
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound6() {
//        String input = "чергесви";
//        Response response = new SearchBySpelling().searchResponse("lez", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Чергесви (-ди, -да, -йри)</i> ⤵️️️
//
//
//                <b>🔎мадни килиг:</b> <code>черкес I</code>\s
//
//                <i>Бабаханов М.Б. "Лезги-урус гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound7() {
//        String input = "долговременный";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Долговременный</i> ⤵️️️
//
//                ➡️<b>️ яргъал вахтунин, гзаф вахтунин </b>
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound8() {
//        String input = "тав";
//        Response response = new SearchBySpelling().searchResponse("lez", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>1. Тав (-уни, -уна, -ари)</i> ⤵️️️
//
//                ➡️<b>️ камин </b>
//
//                <b><i>   - тав авай кIвал</i></b> —  комната с камином
//
//                <i>2. Тав (-ди, -да, -ри)</i> ⤵️️️
//
//                ➡️<b>️ гостиная </b>
//
//                <b><i>   - расай тав ваз мичIи сур хьана</i></b> —  [фольк] обставленной гостиной для тебя могила стала
//
//                <i>3. Тав (-ди, -да, -ари)</i> ⤵️️️
//
//                ➡️<b>️ оттенок </b>
//
//                <b><i>   - ам бирдан насигьат гузвай тав кваз рахана</i></b> —  он вдруг заговорил в нравоучительном тоне
//                <b><i>   - абур вири крариз диндин тав гуз алахъзава</i></b> —  они стараются всем событиям придавать религиозный оттенок
//
//                <i>4. Тав (-ди, -да, -ри)</i> ⤵️️️
//
//                ➡️<b>️ редко свадебный музыкальный вечер в доме жениха </b>
//
//                <b><i>   - ам вуч тав хьуй ашукь алачир?</i></b> —  что это за музыкальный вечер без ашуга?
//
//                <i>5. Тав (-ди, -да)</i> ⤵️️️
//
//                ➡️<b>️ отпуск (металла) </b>
//
//                <b><i>   - чатухъанди гьулдандиз тав гузва</i></b> —  кузнец отпускает сталь (уменьшает накал стали)
//
//
//                <b>🔎мадни килиг:</b> <code>дем</code>\s
//
//                <i>Бабаханов М.Б. "Лезги-урус гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound9() {
//        String input = "жилье";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Жилье</i> ⤵️️️
//
//                ➡️<b>️ инсанар яшамиш жезвай чка </b>
//
//                ➡️<b>️ кIвал, яшамиш жедай чка </b>
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound10() {
//        String input = "дешевый";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Дешевый</i> ⤵️️️
//
//                ➡️<b>️ ужуз </b>
//
//                ➡️<b>️ усал, къиметсуз, са къиметдикни квачир </b>
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound11() {
//        String input = "еж";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Еж</i> ⤵️️️
//
//                ➡️<b>️ кьуьгъуьр </b>
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound12() {
//        String input = "счетный";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Счетный</i> ⤵️️️
//
//                ➡️<b>️ гьисабдин, гьисабардай </b>
//
//                <b><i>   - счётная машина</i></b> —  гьисабардай машин
//
//                ➡️<b>️ гьисабрин, счётдин, счётрин </b>
//
//                <b><i>   - счётная книга </i></b> — счётрин ктаб
//                <b><i>   - счётный работник</i></b> —  счётдин работник (счетоводвилин)
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//
//    @Test
//    void whenTranslateIsFound13() {
//        String input = "убеленный";
//        Response response = new SearchBySpelling().searchResponse("rus", dictionaries, input);
//        String actualMessage = response.messageText();
//        String expected = """
//                <i>Убеленный</i> ⤵️️️
//
//                <b><i>   - убелённый сединами</i></b> —  рехивили лацу авунвай, рехи хьанвай, лацу хьанвай (чIарар, кьил)
//
//                <i>Гаджиев М.М. "Урус-лезги гафарган"</i>""";
//        assertThat(actualMessage).isEqualTo(expected);
//    }
//}