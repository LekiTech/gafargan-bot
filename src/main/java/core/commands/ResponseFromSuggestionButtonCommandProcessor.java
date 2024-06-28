package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.SearchResponseHandler;

public class ResponseFromSuggestionButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;
    private final String word;
    private final String lang;

    public ResponseFromSuggestionButtonCommandProcessor(Message message,
                                                        DictionaryRepository dictionaries,
                                                        TelegramBot bot,
                                                        String word,
                                                        String lang) {
        this.message = message;
        this.dictionaries = dictionaries;
        this.bot = bot;
        this.word = word;
        this.lang = lang;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        SearchResponseHandler messageHandler = new SearchResponseHandler(bot);
        messageHandler.sendResponse(lang, dictionaries, word, chatId);
    }
}