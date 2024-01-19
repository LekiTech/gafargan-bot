package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.storage.DataStorage;

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
        String normalized = "\uD83D\uDCD6Урус-лезги гафарган\n<b><i>✏Урус чIалал кхьихь</i></b>️"
                .replaceAll("\\p{Mn}", "");
        bot.execute(new SendMessage(chatId, normalized).parseMode(ParseMode.HTML));
    }
}