package core.util;

public class LineCorrection {

    public String lineEdit(String line) {
        return line
                .replaceAll("<", "[")
                .replaceAll(">", "]")
                .replaceAll("\\{", "<b><i>   - ")
                .replaceAll("}", "</i></b> — ");
    }
}