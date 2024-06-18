package core.ui;

import com.pengrad.telegrambot.model.WebAppInfo;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import static core.commands.CommandsList.*;

public class KeypadCreator {

    public ReplyKeyboardMarkup createMainMenuKeypad() {
        ReplyKeyboardMarkup keypad = new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(LEZGI_RUS),
                        new KeyboardButton(RUS_LEZGI)
                },
                new KeyboardButton[]{
                        new KeyboardButton(LEZGI_ENG),
                        new KeyboardButton(LEZGI_DIALECT_DICT)
                },
                new KeyboardButton[]{
                        new KeyboardButton(LEZGI_NUMBERS),
                        new KeyboardButton(LEZGI_ALPHABET)
                }
        );
        keypad.resizeKeyboard(true).inputFieldPlaceholder("Кхьихь гаф...");
        return keypad;
    }
}