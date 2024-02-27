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
                        new KeyboardButton("\uD83C\uDF10Чи сайт: gafargan.com")
                                .webAppInfo(new WebAppInfo("https://gafargan.com/"))
                }
        );
        keypad.resizeKeyboard(true).inputFieldPlaceholder("Жугъурзавай гаф кхьихь...");
        return keypad;
    }
}