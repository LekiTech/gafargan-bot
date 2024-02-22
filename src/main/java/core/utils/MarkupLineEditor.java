package core.utils;

public class MarkupLineEditor {

    public static String convertMarkupToHTML(String line) {
        return line
                .replaceAll("<", "[")
                .replaceAll(">", "]")
                .replaceAll("\\{", "<b><i>   - ")
                .replaceAll("}", "</i></b> — ");
    }
}