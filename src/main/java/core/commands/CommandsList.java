package core.commands;

public class CommandsList {

    /* Menu bar commands */
    public static final String START = "/start";
    public static final String INFO = "/info";

    /* Keypad commands and dictionary keys */
    public static final String LEZGI_RUS = "\uD83D\uDCD7Лезги-урус гафарган";
    public static final String RUS_LEZGI = "\uD83D\uDCD5Урус-лезги гафарган";
    public static final String LEZGI_ENG = "\uD83C\uDDEC\uD83C\uDDE7Лезги-инглис гафарган";
    public static final String LEZGI_DIALECT_DICT = "⛰Нугъатдин гафарган";
    public static final String LEZGI_NUMBERS = "\uD83D\uDD22Лезги числительнияр";
    public static final String LEZGI_ALPHABET = "\uD83D\uDD20Лезги гьарфарган";

    /* Old keypad commands */
    /* These commands are needed because there are users who still have the old
    version of Keypad (the front buttons for switching dictionaries). In order for
    Keypad to update to the current version, it is necessary to read this old command,
    then update the Keypad of the user. */
    public static final String LEZ_RUS_TAL = "\uD83D\uDCD9Лезгинско-русский словарь (Талибов Б., Гаджиев М.)";
    public static final String LEZ_RUS_BB = "\uD83D\uDCD7Лезгинско-русский словарь (Бабаханов М.Б.)";
    public static final String RUS_LEZ_GADZH = "\uD83D\uDCD5Русско-лезгинский словарь (Гаджиев М.М.)";
    public static final String LEZGI_ALPHABET_OLD = "\uD83D\uDD20Лезги гьарфалаг";
    public static final String ABOUT_US = "\uD83D\uDCDCЧакай";
}