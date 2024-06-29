package core.ui;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

import static core.commands.CommandsList.*;

public class InlineKeyboardCreator {

    public static InlineKeyboardMarkup createNumeralAudioButton(String num) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[][]{
                        {new InlineKeyboardButton("Ван\uD83D\uDD09")
                                .callbackData(AUDIO_NUMERAL + EQUALS + num + EQUALS + num)}
                }
        );
    }

    public static InlineKeyboardMarkup createSearchSuggestionsButton(String word, String lang) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[][]{
                        {new InlineKeyboardButton("Мукьва тир гафар жагъурдан?")
                                .callbackData(SEARCH_SUGGESTIONS + EQUALS + word + EQUALS + lang)}
                }
        );
    }

    public static InlineKeyboardMarkup createSearchButtonFromDefinitionResponse(String word, String lang) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[][]{
                        {new InlineKeyboardButton("Мад жагъурдан?")
                                .callbackData(SEARCH_IN_EXAMPLES + EQUALS + word + EQUALS + lang)}
                }
        );
    }

    public static InlineKeyboardMarkup createExpressionExampleButton(String word, String lang) {
        String callBackData = EXPRESSION_EXAMPLE + EQUALS + word + EQUALS + lang;
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[][]{
                        {new InlineKeyboardButton("Мад меселаяр къалурун")
                                .callbackData(callBackData)}
                }
        );
    }

    public static InlineKeyboardMarkup createSuggestionButtons(List<String> words, String lang) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String word : words) {
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            buttonList.add(new InlineKeyboardButton(word).callbackData(SUGGESTION + EQUALS + word + EQUALS + lang));
            buttons.add(buttonList);
        }
        InlineKeyboardButton[][] inlineKeyboardButton = new InlineKeyboardButton[buttons.size()][1];
        for (int i = 0; i < inlineKeyboardButton.length; i++) {
            for (int j = 0; j < inlineKeyboardButton[i].length; j++) {
                inlineKeyboardButton[i][j] = buttons.get(i).get(j);
            }
        }
        return new InlineKeyboardMarkup(inlineKeyboardButton);
    }

    public static InlineKeyboardMarkup createAlphabetAudioButton(String letter) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[][]{
                        {new InlineKeyboardButton("Ван\uD83D\uDD09")
                                .callbackData(AUDIO_ALPHABET + EQUALS + letter + EQUALS + ALPHABET)}
                }
        );
    }
}
