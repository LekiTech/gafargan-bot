package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.ui.KeypadCreator;

public class StartCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public StartCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        KeypadCreator keypadCreator = new KeypadCreator();
        ReplyKeyboardMarkup keypad = keypadCreator.createMainMenuKeypad();
        bot.execute(new SendMessage(chatId, "Ас-саляму алейкум!\uD83D\uDC4B\uD83C\uDFFC\n"
                                            + "Вун атуй, рагъ атуй!⛰\n\n"
                                            + "\uD83D\uDCDA<b>Гафарган хкягъа\uD83D\uDC47\uD83C\uDFFC</b>")
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
        DataStorage.instance().createUser(chatId);
    }
}