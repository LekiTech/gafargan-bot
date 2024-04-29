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
        String outputMsg = """
                Ас саляму алейкум!👋🏼
                Вун атуй, рагъ атуй!⛰
                                
                <b>Таржумачи хкягъа👇🏼</b>
                """;
        bot.execute(new SendMessage(chatId, outputMsg)
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
        DataStorage.instance().createUser(chatId);
    }
}