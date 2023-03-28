package core;

public class StringSimilarity {

    public static double similarity(String firstLine, String secondLine) {
        String longer = firstLine;
        String shorter = secondLine;
        if (firstLine.length() < secondLine.length()) { /* longer всегда должен иметь большую длину */
            longer = secondLine;
            shorter = firstLine;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* обе строки имеют нулевую длину */
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private static int editDistance(String firstLine, String secondLine) {
        firstLine = firstLine.toLowerCase();
        secondLine = secondLine.toLowerCase();

        int[] costs = new int[secondLine.length() + 1];
        for (int i = 0; i <= firstLine.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= secondLine.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (firstLine.charAt(i - 1) != secondLine.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[secondLine.length()] = lastValue;
        }
        return costs[secondLine.length()];
    }
}