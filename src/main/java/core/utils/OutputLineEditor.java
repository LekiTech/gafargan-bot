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
            return "<i>Бабаханов М.Б. \"Лезги-урус гафарган\"</i>";
        } else if (dictionaryLang.equals("rus")) {
            return "<i>Гаджиев М.М. \"Урус-лезги гафарган\"</i>";
        }
        return "";
    }
}