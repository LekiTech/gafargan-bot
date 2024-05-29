package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import core.database.DataStorage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.NumbersSearchResponseHandler;
import core.searchers.SearchResponseHandler;

import static core.commands.CommandsList.*;
import static core.utils.SearchStringNormalizer.normalizeString;

public class ResponseSearchCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;
    private final String lang;

    public ResponseSearchCommandProcessor(Message message, DictionaryRepository dictionaries, TelegramBot bot, String lang) {
        this.message = message;
        this.dictionaries = dictionaries;
        this.bot = bot;
        this.lang = lang;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = normalizeString(message.text());
        SearchResponseHandler messageHandler = new SearchResponseHandler(bot);
        NumbersSearchResponseHandler numbersHandler = new NumbersSearchResponseHandler(bot);
        switch (lang) {
            case LEZGI_RUS -> messageHandler.sendResponse("lez", dictionaries, userMessage, chatId);
            case RUS_LEZGI -> messageHandler.sendResponse("rus", dictionaries, userMessage, chatId);
            case LEZGI_NUMBERS -> numbersHandler.findResponse(userMessage, chatId);
            default -> {
            }
        }
    }
}