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
            return "<i>\"<b>Лезги-урус</b> гафарган\" Бабаханов М.Б.</i>";
        } else if (dictionaryLang.equals("rus")) {
            return "<i>\"<b>Урус-лезги</b> гафарган\" Гаджиев М.М.</i>";
        }
        return "";
    }
}