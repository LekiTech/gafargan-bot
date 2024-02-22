package core.utils;

public class SearchStringNormalizer {

    /**
     * Replaces certain characters in a given line with the uppercase letter "I".
     * The characters that will be replaced are "i", "1", "l", "ӏ", "|" and "!"
     * if they are preceded by any of the following characters:
     * "к", "п", "т", "ц", "ч", "К", "П", "Т", "Ц" or "Ч".
     * <p>
     * This method can be static because it is a "pure function"
     * and does not change the "state" outside its scope.
     *
     * @param line the input line where replacements will be made.
     * @return a new string with the specified characters replaced by "I".
     */
    public static String replaceVerticalBar(String line) {
        String lowercaseString = line.toLowerCase();
        return lowercaseString.replaceAll("(?<=[кптцчКПТЦЧ])[i1lӏ|!]", "I");
    }

    /**
     * Trims excess spaces in a given line by replacing consecutive whitespace characters with a single space
     * and then trimming any leading or trailing spaces.
     *
     * @param line the input line where excess spaces will be trimmed.
     * @return a new string with excess spaces removed.
     */
    public static String trimSpaces(String line) {
        return line.replaceAll("\\s+", " ").trim();
    }

    /**
     * Normalizes a string by trimming leading and trailing spaces, replacing vertical bars with appropriate characters,
     * and converting all characters to lowercase. Additionally, this method replaces Cyrillic letter "ё" with "е".
     *
     * @param line The input string to be normalized.
     * @return the normalized string.
     */
    public static String normalizeString(String line) {
        String resultString = replaceVerticalBar(trimSpaces(line));
        return resultString.replaceAll("ё", "е");
    }
}
