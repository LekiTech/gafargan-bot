package core.utils;

public class WordCapitalize {

    public static String capitalizeFirstLetter(String word) {
        return "<i>"
               + word.substring(0, 1).toUpperCase() + word.substring(1)
               + "</i> ⤵️️️\n\n";
    }

    public static String capitalizeFirstLetterWithNum(String num, String word) {
        String capitalizedWord = word.substring(0, 1).toUpperCase()
                                 + word.substring(1);
        return "<i>"
               + num
               + capitalizedWord
               + "</i> ⤵️️️\n\n";
    }
}
