package core.utils;

public class OutputLineEditor {

    public static String convertMarkupToHTML(String line) {
        return line
                .replaceAll("<", "[")
                .replaceAll(">", "]")
                .replaceAll("\\{", "<b><i>   - ")
                .replaceAll("}", "</i></b> — ");
    }

    public static String insertAuthorsName(String dictionaryLang) {
        if (dictionaryLang.equals("lez")) {
            return "<i>\uD83D\uDCDA\"Лезги-урус гафарган\" Бабаханов М.Б.</i>";
        } else if (dictionaryLang.equals("rus")) {
            return "<i>\uD83D\uDCDA\"Урус-лезги гафарган\" Гаджиев М.М.</i>";
        }
        return "";
    }
}