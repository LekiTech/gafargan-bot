package core.ui;

import com.pengrad.telegrambot.model.WebAppInfo;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class KeypadCreator {

    public ReplyKeyboardMarkup createMainMenuKeypad() {
        ReplyKeyboardMarkup keypad = new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton("\uD83D\uDCD7Лезги-урус гафарган"),
                        new KeyboardButton("\uD83D\uDCD5Урус-лезги гафарган")
                },
                new KeyboardButton[]{
                        new KeyboardButton("\uD83D\uDD22Лезги числительнияр"),
                        new KeyboardButton("\uD83D\uDD20Лезги гьарфалаг")
                }
        );
        keypad.resizeKeyboard(true).inputFieldPlaceholder("Жугъурзавай гаф кхьихь...");
        return keypad;
    }
}