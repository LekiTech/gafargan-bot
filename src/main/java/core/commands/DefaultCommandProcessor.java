package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import core.DataStorage;
import core.KeypadCreator;

public class DefaultCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public DefaultCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        KeypadCreator keypadCreator = new KeypadCreator();
        ReplyKeyboardMarkup keypad = keypadCreator.createMenuForDictionarySelection();
        bot.execute(new SendMessage(chatId, "<b>Вуна гафарган хкягънавач.\n\n"
                + "Гафарган хкягъа\uD83D\uDC47\uD83C\uDFFC</b>").parseMode(ParseMode.HTML).replyMarkup(keypad));
        DataStorage.instance().createUser(chatId);
    }
}