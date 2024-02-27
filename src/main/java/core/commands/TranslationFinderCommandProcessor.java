package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import core.database.DataStorage;
import core.searchers.SearchResponseHandler;

import static core.commands.CommandsList.*;
import static core.bothandler.BotUpdates.*;
import static core.utils.SearchStringNormalizer.normalizeString;

public class TranslationFinderCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public TranslationFinderCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = normalizeString(message.text());
        var selectedSearcher = DataStorage.instance().getLastSelectedDictionary(chatId);
        SearchResponseHandler messageHandler = new SearchResponseHandler(bot);
        switch (selectedSearcher) {
            case LEZGI_RUS -> messageHandler.findResponse(lezgiRusDictionary, userMessage, chatId);
            case RUS_LEZGI -> messageHandler.findResponse(rusLezgiDictionary, userMessage, chatId);
            default -> {
            }
        }
    }
}