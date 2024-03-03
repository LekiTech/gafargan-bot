package core.ui;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardCreator {

    public static InlineKeyboardMarkup createInlineKeyboard(List<String> words) {
        if (words.get(0).contains("=example")) {
            return new InlineKeyboardMarkup(
                    new InlineKeyboardButton[][]{
                            {new InlineKeyboardButton("Мад меселаяр къалурун").callbackData(words.get(0))}
                    }
            );
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String word : words) {
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            buttonList.add(new InlineKeyboardButton(word).callbackData(word));
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
}
