package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.SearchResponseHandler;
import lombok.AllArgsConstructor;

import static core.commands.CommandsList.*;

@AllArgsConstructor
public class ResponseFromSuggestionButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;
    private final String word;
    private final String lang;

    @Override
    public void execute() {
        var chatId = message.chat().id();
        SearchResponseHandler messageHandler = new SearchResponseHandler(bot);
        var searchDialectWord = new ResponseSearchCommandProcessor(message, dictionaries, bot, lang);
        switch (lang) {
            case LEZGI_RUS, RUS_LEZGI, LEZGI_ENG -> messageHandler.sendResponse(lang, dictionaries, word, chatId);
            case LEZGI_DIALECT_DICT -> searchDialectWord.sendResponseFromDialectDict(dictionaries, word, chatId);
            default -> {
            }
        }
    }
}