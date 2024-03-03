package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import core.database.DataStorage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.SearchResponseHandler;

import static core.commands.CommandsList.*;
import static core.utils.SearchStringNormalizer.normalizeString;

public class ResponseFinderCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;

    public ResponseFinderCommandProcessor(Message message, DictionaryRepository dictionaries, TelegramBot bot) {
        this.message = message;
        this.dictionaries = dictionaries;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = normalizeString(message.text());
        var selectedSearcher = DataStorage.instance().getLastSelectedDictionary(chatId);
        SearchResponseHandler messageHandler = new SearchResponseHandler(bot);
        switch (selectedSearcher) {
            case LEZGI_RUS -> messageHandler.findResponse("lez", dictionaries, userMessage, chatId);
            case RUS_LEZGI -> messageHandler.findResponse("rus", dictionaries, userMessage, chatId);
            default -> {
            }
        }
    }
}