package core.utils;

public class WordCapitalize {

    public static String capitalizeFirstLetter(String word) {
        return "<i>"
                + word.substring(0, 1).toUpperCase() + word.substring(1)
                + "</i> ⤵️️️\n\n";
    }

    public static String capitalizeFirstLetterWithNum(String word) {
        return "<i>" +
                word.substring(0, 3)
                + word.substring(3, 4).toUpperCase()
                + word.substring(4)
                + "</i> ⤵️️️\n\n";
    }
}
