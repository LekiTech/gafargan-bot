package core.utils;

import java.util.HashMap;
import java.util.Map;

public class AlphabetBuilder {

    private final Map<String, String> alphabetMap = new HashMap<>();

    {
        alphabetMap.put("А", "аялар\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66");
        alphabetMap.put("Б", "бацIи\uD83D\uDC10");
        alphabetMap.put("В", "вирт\uD83C\uDF6F");
        alphabetMap.put("Г", "гурцIул\uD83D\uDC36");
        alphabetMap.put("Гъ", "гъед\uD83D\uDC20");
        alphabetMap.put("Гь", "гьер\uD83D\uDC0F");
        alphabetMap.put("Д", "дакIар\uD83E\uDE9F");
        alphabetMap.put("Е", "еб\uD83E\uDEA2");
        alphabetMap.put("Ж", "жив❄️");
        alphabetMap.put("З", "зарар\uD83C\uDFB2");
        alphabetMap.put("И", "ич\uD83C\uDF4F");
        alphabetMap.put("Й", "йиф\uD83C\uDF19");
        alphabetMap.put("К", "кац\uD83D\uDC08");
        alphabetMap.put("Къ", "къаз\uD83E\uDEBF");
        alphabetMap.put("Кь", "кьиф\uD83D\uDC01");
        alphabetMap.put("КI", "кIвал\uD83C\uDFE0");
        alphabetMap.put("Л", "лекь\uD83E\uDD85");
        alphabetMap.put("М", "муьгъ\uD83C\uDF09");
        alphabetMap.put("Н", "нуькI\uD83D\uDC26\u200D⬛️");
        alphabetMap.put("П", "пеш\uD83C\uDF42");
        alphabetMap.put("ПI", "пIини\uD83C\uDF52");
        alphabetMap.put("Р", "рагъ☀️");
        alphabetMap.put("С", "сикI\uD83E\uDD8A");
        alphabetMap.put("Т", "тар\uD83C\uDF34");
        alphabetMap.put("ТI", "тIур\uD83E\uDD44");
        alphabetMap.put("У", "улуб\uD83D\uDCD8");
        alphabetMap.put("Уь", "уьтуь");
        alphabetMap.put("Ф", "фу\uD83C\uDF5E");
        alphabetMap.put("Х", "хват");
        alphabetMap.put("Хъ", "хъвер\uD83D\uDE42");
        alphabetMap.put("Хь", "хьел\uD83C\uDFF9");
        alphabetMap.put("Ц", "ципицIар\uD83C\uDF47");
        alphabetMap.put("ЦI", "цIай\uD83D\uDD25");
        alphabetMap.put("Ч", "чичIек\uD83E\uDDC5");
        alphabetMap.put("ЧI", "чIиж\uD83D\uDC1D");
        alphabetMap.put("Ш", "шив\uD83D\uDC0E");
        alphabetMap.put("Ъ", "кIеви лишан");
        alphabetMap.put("Ы", "ы");
        alphabetMap.put("Ь", "хъуьтуьл лишан");
        alphabetMap.put("Э", "экв\uD83D\uDCA1");
        alphabetMap.put("Ю", "югъ\uD83C\uDFD9");
        alphabetMap.put("Я", "яргъируш");
    }

    public String get(String key) {
        return alphabetMap.get(key);
    }

    public boolean containsKey(String key) {
        return alphabetMap.containsKey(key);
    }
}