package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.FuzzySearchBySpelling;
import core.searchers.Response;
import core.searchers.SearchByExample;
import core.ui.InlineKeyboardCreator;

public class ResponseFromSearchSuggestionsButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;
    private final String word;
    private final String lang;

    public ResponseFromSearchSuggestionsButtonCommandProcessor(Message message,
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
        Response response = new FuzzySearchBySpelling().searchResponse(lang, dictionaries, word);
        if (response.suggestions() != null) {
            var inlineKeyboard
                    = InlineKeyboardCreator.createSuggestionButtons(response.suggestions(), lang);
            bot.execute(new SendMessage(chatId, response.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(inlineKeyboard));
        } else {
            bot.execute(new SendMessage(chatId, "<b>❌Мукьва тир гафар жагъанач</b>")
                    .parseMode(ParseMode.HTML));
        }
    }
}
