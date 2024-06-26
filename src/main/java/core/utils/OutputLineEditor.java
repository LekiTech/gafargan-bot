package core.utils;

import static core.commands.CommandsList.*;

public class OutputLineEditor {

    public static String convertMarkupToHTML(String line) {
        return line
                .replaceAll("<", "[")
                .replaceAll(">", "]")
                .replaceAll("\\{", "<b><i>   - ")
                .replaceAll("}", "</i></b> — ");
    }

    public static String insertAuthorsName(String dictionaryLang) {
        if (dictionaryLang.equals(LEZGI_RUS)) {
            return "<i>\uD83D\uDCDA\"Лезги-урус гафарган\" Бабаханов М.Б.</i>";
        } else if (dictionaryLang.equals(RUS_LEZGI)) {
            return "<i>\uD83D\uDCDA\"Урус-лезги гафарган\" Гаджиев М.М.</i>";
        } else if (dictionaryLang.equals(LEZGI_ENG)) {
            return "<i>\uD83C\uDDEC\uD83C\uDDE7\"Лезги-инглис гафарган\" Расим Расулов</i>";
        }
        return "";
    }
}