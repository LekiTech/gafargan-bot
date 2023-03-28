package core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ParsedDictionaryTest {

    @Test
    void whenListByKeyIsFound1() throws IOException {
        ParsedDictionary parsedDictionary = new ParsedDictionary();
        parsedDictionary.parse("src/main/resources/lezgi_rus_dict_babakhanov.json");
        List<String> expected = List.of("I <прич>. от гл. {кIамукьун}",
                "♦ {кIамай кьван} <нареч.> вдоволь, сколько угодно",
                "II <сущ>. дурак, болван, идиот; см.тж. акьулсуз, ахмакь, кимиди, къанажагъсуз, кIамаш, сефигь,  "
                        + "тахкимиди, тахсара, хибриди  ",
                "III <прил>. придурковатый, слабоумный; см.тж. акьулсуз II, ахмакь II, кими II, къанажагъсуз II, "
                        + "кIамаш II, сефигь  II, тахкими, хибри");
        List<String> actual = parsedDictionary.map.get("кiамай");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenListByKeyIsFound2() throws IOException {
        ParsedDictionary parsedDictionary = new ParsedDictionary();
        parsedDictionary.parse("src/main/resources/lezgi_rus_dict_babakhanov.json");
        List<String> expected = List.of("I <прил.>",
                "1. недостающий, отсутствующий,  но необходимый; {кими  кьадар пул за вав пака вахкуда} недостающую "
                        + "сумму денег я тебе отдам завтра",
                "2. не доведённый до необходимой нормы, неполный,  недостаточный, некомплектный; {виш грамм кими"
                        + " я лугьуз икьван рахадани бес?} разве можно столько говорить из-за недовеса  в сто граммов;"
                        + " <см.тж.> уьскуьк, эксик ",
                "⦿ {пад  кими гафар} <сущ>. глупые слова, чушь  ",
                "♦ {кими авун} <гл.> убавлять, уменьшать",
                "♦ {кими хьун} <гл.> убавляться, уменьшаться;  ☼ {чарадахъ шехьдай вилелай нагъв  кими жедач} "
                        + "<погов.> не стоит всё  принимать близко к сердцу (<букв>. с плачущего по чужому глаза "
                        + "слеза не  сходит)",
                "II <прил>. глупый, придурковатый; {кими инсан} неумный, глупый, тупой человек; {кими ихтилатар} "
                        + "глупые разговоры, чушь; {адай бязи  вахтара кими амалар акъатзава} у него иногда бывают "
                        + "глупые выходки; см.тж. акьулсуз II, ахмакь II, къанажагъсуз II, кIамай III, кIамаш II, "
                        + "сефигь  II, тахкими, хибри",
                "♦ {кими авун} <гл.> сводить с ума",
                "♦ {кими хьун} <гл.> сходить  с ума, становиться  глупым, бестолковым, глупеть");
        List<String> actual = parsedDictionary.map.get("кими");
        assertThat(actual).isEqualTo(expected);
    }
}