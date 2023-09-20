package core;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class KeypadCreator {

    public ReplyKeyboardMarkup createMenuForDictionarySelection() {
        ReplyKeyboardMarkup keypad = new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton("\uD83D\uDCD7Лезги-урус гафарган"),
                        new KeyboardButton("\uD83D\uDCD5Урус-лезги гафарган")
                },
                new KeyboardButton[]{
                        new KeyboardButton("\uD83D\uDCE9Кхьихь чаз"),
                        new KeyboardButton("\uD83D\uDCDCИнфо")
                }
        );
        keypad.resizeKeyboard(true).inputFieldPlaceholder("Жугъурзавай гаф кхьихь...");
        return keypad;
    }
}