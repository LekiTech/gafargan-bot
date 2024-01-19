package core.searchers;

import core.dictionary.parser.DictionaryParser;
import core.dictionary.parser.DictionaryRepository;
import core.dictionary.parser.JsonDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SearchForExampleOfWordTest {


    private final DictionaryRepository lezgiRusDictionary = new JsonDictionary();
    private final DictionaryRepository rusLezgiDictionary = new JsonDictionary();
    DictionaryParser dictionaryParser = new DictionaryParser();

    @BeforeEach
    public void initDictionaries() throws Exception {
        lezgiRusDictionary.setDictionary(dictionaryParser.parse("lezgi_rus_dict_babakhanov_v2.json"));
        rusLezgiDictionary.setDictionary(dictionaryParser.parse("rus_lezgi_dict_hajiyev_v2.json"));
    }

    @Test
    void sendExample() {
        String inputMessage = "Руш";
        String expectedButtCallbackData = "руш=example";
        Answer answer = new SearchInDictionary().sendAnswerFromDictionary(lezgiRusDictionary, inputMessage);
        assertThat(answer.exampleButton().get(0)).isEqualTo(expectedButtCallbackData);
        Answer answer1 = new SearchForExampleOfWord().sendExample(lezgiRusDictionary, inputMessage);
        String expected = """
                <i>Руш</i> ⤵️

                <b><i>   - руш гана лам къачуна</i></b> —  [погов]. шут. отдал дочку, приобрел осла (зятя);\s
                <b><i>   - руш чарадан цлан къван я</i></b> —  [погов]. дочь - камень в чужой стене;\s
                руш жедалди къван хьуй [погов]. пусть камень родится, чем дочка
                <b><i>   - чан руш!</i></b> —  обр. дочка! доченька! (ласковое обращение)
                <b><i>   - кицIин руш!</i></b> —  обр. собачья дочь! (грубое обращение)
                """;
        String actual = answer1.messageText();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void sendExample1() {
        String inputMessage = "КIвал";
        String expectedButtCallbackData = "кiвал=example";
        Answer answer = new SearchInDictionary().sendAnswerFromDictionary(lezgiRusDictionary, inputMessage);
        assertThat(answer.exampleButton().get(0)).isEqualTo(expectedButtCallbackData);
        Answer answer1 = new SearchForExampleOfWord().sendExample(lezgiRusDictionary, inputMessage);
        String expected = """
                <i>КIвал</i> ⤵️

                <b><i>   - кIвал авун</i></b> —  [гл.] 1) строить дом; 2) [перен]. создавать семью; обзаводиться имуществом
                <b><i>   - кIвал алай къиб</i></b> —  [зоол.] [сущ.] черепаха; [см.тж]. хъалхъас
                <b><i>   - кIвал алай шуькьуьнт</i></b> —  [зоол.] сущ. улитка с раковиной
                <b><i>   - кIвал атIун</i></b> —  гл. ограбить дом (со взломом)
                <b><i>   - кIвал къени авун</i></b> —  [гл.] делать так, чтобы в доме, в семье было благополучие
                <b><i>   - кIвал къени хьайиди!</i></b> —  обр. милый! любезный!
                <b><i>   - кIвал кIаняй акъатуй!</i></b> —  [межд.] чтоб до основания разрушился дом!
                <b><i>   - кIвал чIур хьайиди!</i></b> —  обр. окаянный! отверженный! проклятый!
                <b><i>   - кIвал чIур хьун</i></b> —  гл. 1) разрушаться (о доме); 2) [перен]. разоряться, лишаться всего, оставаться ни с чем
                <b><i>   - кIвал чIур хьуй [вичин]!</i></b> —  [межд]. чтобы разорился [его] дом!
                <b><i>   - кIвал чIурун</i></b> —  гл. 1) разрушать дом; 2) [перен]. разрушать семью; навлекать беду на семью
                <b><i>   - кIвалел атун [фин]</i></b> —  [гл.] совершать первую побывку родительского дома невестой после свадьбы; [см.тж.] ацIунар (ацIунрал атун [фин])
                <b><i>   - кIвалел эверун</i></b> —  гл. 1) приглашение на <b><i>   - мел</i></b> —  (помочи, толока) по случаю завершения строительства дома; [см]. <b><i>   - мел</i></b> — ; 2) приглашение на званный обед молодоженов
                <b><i>   - кIвалин юкь аватуй!</i></b> —  [межд.] да разрушится дом!
                <b><i>   - кIваляй чукурун</i></b> —  гл. выгонять, выселять из дому
                <b><i>   - кIваляй кьве кьил кьуна акъудрай!</i></b> —  [межд]. чтоб выносили из дому (мертвого)
                """;
        String actual = answer1.messageText();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void sendExample2() {
        String inputMessage = "рикi";
        String expectedButtCallbackData = "рикi=example";
        Answer answer = new SearchInDictionary().sendAnswerFromDictionary(lezgiRusDictionary, inputMessage);
        assertThat(answer.exampleButton().get(0)).isEqualTo(expectedButtCallbackData);
        Answer answer1 = new SearchForExampleOfWord().sendExample(lezgiRusDictionary, inputMessage);
        String expected = """
                <i>РикI</i> ⤵️

                <b><i>   - рикIин куьлег мез я</i></b> —  [погов]. язык - ключ от сердца
                <b><i>   - хъсанвал рикIелай фидач</i></b> —  [погов]. доброе дело не забывается
                <b><i>   - рикIел хуьх</i></b> —  запомни
                <b><i>   - тупIун рикI</i></b> —  анат. сущ. подушечка пальца
                <b><i>   - рикIи тухун</i></b> —  [гл]. иметь аппетит
                <b><i>   - рикIин дарман</i></b> —  мед. сущ. валидол
                <b><i>   - рикIин духтур</i></b> —  мед. сущ. кардиолог
                <b><i>   - рикI авай</i></b> —  [прил.] смелый, мужественный
                <b><i>   - рикI аватун</i></b> —  гл. испугаться, ужаснуться
                <b><i>   - рикI авудун</i></b> —  [гл.] пугать, приводить в ужас
                <b><i>   - рикI авуна</i></b> —  [нареч]. смело, решительно
                <b><i>   - рикI агатун</i></b> —  гл. приходиться по душе, нравиться
                <b><i>   - рикI акъатун</i></b> —  гл. 1) скучать; тосковать; 2) соскучиться, истосковаться (по кому-чему-л.); 3) томиться, испытывать тягость; 4) злиться, раздражаться (от зависти)
                <b><i>   - рикI акъудун</i></b> —  гл. 1) томить; 2) мучить, терзать; 3) надоедать (кому-л. чем-л.), тянуть душу
                <b><i>   - рикI аладарун</i></b> —  гл. 1) рассеять печальное настроение, развлечь; 2) рассеяться, развеселиться
                <b><i>   - рикI алаз</i></b> —  [нареч]. охотно, с желанием
                <b><i>   - рикI алай</i></b> —  [прил]. любимый, дорогой, желанный
                <b><i>   - рикI алахьун</i></b> —  гл. развлечься, забыться
                <b><i>   - рикI атун</i></b> —  [гл.] набраться смелости, осмелеть
                <b><i>   - рикI атIун</i></b> —  гл. ранить душу, причинять неприятность
                <b><i>   - рикI ацукьун</i></b> —  [гл.] понравиться, приглянуться
                <b><i>   - рикI ачух</i></b> —  [прил.] искренний, откровенный
                """;
        String actual = answer1.messageText();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void sendExample3() {
        String inputMessage = "раши";
        String expectedButtCallbackData = "раши=example";
        Answer answer = new SearchInDictionary().sendAnswerFromDictionary(lezgiRusDictionary, inputMessage);
        assertThat(answer.exampleButton().get(0)).isEqualTo(expectedButtCallbackData);
        Answer answer1 = new SearchForExampleOfWord().sendExample(lezgiRusDictionary, inputMessage);
        String expected = """
                <i>Раши</i> ⤵️

                <b><i>   - раши авун</i></b> —  [гл.] сделать темно-желтым
                <b><i>   - раши хьун</i></b> —  [гл.] становиться темно-желтым
                """;
        String actual = answer1.messageText();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void sendExample4() {
        String inputMessage = "видеть";
        String expectedButtCallbackData = "видеть=example";
        Answer answer = new SearchInDictionary().sendAnswerFromDictionary(rusLezgiDictionary, inputMessage);
        assertThat(answer.exampleButton().get(0)).isEqualTo(expectedButtCallbackData);
        Answer answer1 = new SearchForExampleOfWord().sendExample(rusLezgiDictionary, inputMessage);
        String expected = """
                <i>Видеть</i> ⤵️

                <b><i>   - видеть насквозь </i></b> — (са кас) лап хъсандиз чир хьун, адан къастар лап хъсандиз чир хьун
                """;
        String actual = answer1.messageText();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void sendExample5() {
        String inputMessage = "что";
        String expectedButtCallbackData = "что=example";
        Answer answer = new SearchInDictionary().sendAnswerFromDictionary(rusLezgiDictionary, inputMessage);
        assertThat(answer.exampleButton().get(0)).isEqualTo(expectedButtCallbackData);
        Answer answer1 = new SearchForExampleOfWord().sendExample(rusLezgiDictionary, inputMessage);
        String expected = """
                <i>Что</i> ⤵️

                <b><i>   - вот что</i></b> —  1) ингье икI; 2) вуна вуч аяни, заз килиг, яб це
                <b><i>   - что до меня</i></b> —  зун лагьайтIа, закай рахайтIа, зал гьалтайтIа
                <b><i>   - ни за что</i></b> —  гьич, кьейитIани, вуч гайитIани, вуч авуртIани
                <b><i>   - ни за что, ни про что</i></b> —  гьакI са карни авачиз, нагьакь, гьавайда
                <b><i>   - ни к чему</i></b> —  герек туш, герек авач
                <b><i>   - с чего?</i></b> —  вучиз? вуч себебдалди?
                <b><i>   - пока что</i></b> —  гьелелиг
                <b><i>   - почти что</i></b> —  саки
                <b><i>   - ну, что же</i></b> —  мад вуч ийида кьван
                <b><i>   - что бы ни было</i></b> —  вуч хьайитIани
                <b><i>   - что ты!</i></b> —  вуна вуч лугьузва! ваз вуч хьанва!
                <b><i>   - чуть что</i></b> —  са жизвидлай, са жизви кар хьанамазди, са жизви малум хьанамазди
                """;
        String actual = answer1.messageText();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void sendExample6() {
        String inputMessage = "углубленный";
        String expectedButtCallbackData = "углубленный=example";
        Answer answer = new SearchInDictionary().sendAnswerFromDictionary(rusLezgiDictionary, inputMessage);
        assertThat(answer.exampleButton().get(0)).isEqualTo(expectedButtCallbackData);
        Answer answer1 = new SearchForExampleOfWord().sendExample(rusLezgiDictionary, inputMessage);
        String expected = """
                <i>Углубленный</i> ⤵️

                <b><i>   - углубленный в себя</i></b> —  дерин фикирриз фенвай, фикиррин деринра гьахьнавай
                """;
        String actual = answer1.messageText();
        assertThat(actual).isEqualTo(expected);
    }
}