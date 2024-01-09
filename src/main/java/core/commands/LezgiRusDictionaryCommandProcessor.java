package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.updates.DataStorage;

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
        bot.execute(new SendMessage(chatId, "\uD83D\uDCD6Лезги-урус гафарган\n"
                + "<b><i>✏️Лезги чIалал кхьихь</i></b>").parseMode(ParseMode.HTML));
    }
}