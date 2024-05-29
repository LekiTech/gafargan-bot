package core.ui;

import com.pengrad.telegrambot.model.WebAppInfo;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import core.commands.CommandsList;

public class KeypadCreator {

    public ReplyKeyboardMarkup createMainMenuKeypad() {
        ReplyKeyboardMarkup keypad = new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(CommandsList.LEZGI_RUS),
                        new KeyboardButton(CommandsList.RUS_LEZGI)
                },
                new KeyboardButton[]{
                        new KeyboardButton(CommandsList.LEZGI_NUMBERS),
                        new KeyboardButton(CommandsList.LEZGI_ALPHABET)
                }
        );
        keypad.resizeKeyboard(true).inputFieldPlaceholder("Жугъурзавай гаф кхьихь...");
        return keypad;
    }
}