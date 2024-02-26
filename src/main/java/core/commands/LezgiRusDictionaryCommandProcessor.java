package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.ui.KeypadCreator;

public class LezgiRusDictionaryCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public LezgiRusDictionaryCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        DataStorage.instance().saveSelectedDictionary(chatId, CommandsList.LEZGI_RUS);
        KeypadCreator keypadCreator = new KeypadCreator();
        ReplyKeyboardMarkup keypad = keypadCreator.createMenuForDictionarySelection();
        bot.execute(new SendMessage(chatId, "\uD83D\uDCD6Лезги-урус гафарган.\n"
                + "<b>✏️Лезги чIалал кхьихь.</b>")
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
    }
}