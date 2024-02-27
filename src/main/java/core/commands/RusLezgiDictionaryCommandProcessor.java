package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.ui.KeypadCreator;

public class RusLezgiDictionaryCommandProcessor implements ChatCommandProcessor {
    private final Message message;
    private final TelegramBot bot;

    public RusLezgiDictionaryCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        DataStorage.instance().saveSelectedDictionary(chatId, CommandsList.RUS_LEZGI);
        String normalized = "\uD83D\uDCD6Урус-лезги гафарган.\n<b>✏Урус чIалал кхьихь.</b>️"
                .replaceAll("\\p{Mn}", "");
        KeypadCreator keypadCreator = new KeypadCreator();
        ReplyKeyboardMarkup keypad = keypadCreator.createMainMenuKeypad();
        bot.execute(new SendMessage(chatId, normalized)
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
    }
}