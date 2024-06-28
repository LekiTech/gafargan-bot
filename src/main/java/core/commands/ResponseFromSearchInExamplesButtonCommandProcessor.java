package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.Response;
import core.searchers.SearchByExample;

public class ResponseFromSearchInExamplesButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;
    private final String word;
    private final String lang;

    public ResponseFromSearchInExamplesButtonCommandProcessor(Message message,
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
        Response response = new SearchByExample().searchResponse(lang, dictionaries, word);
        if (response != null) {
            bot.execute(new SendMessage(chatId, response.messageText())
                    .parseMode(ParseMode.HTML));
        } else {
            bot.execute(new SendMessage(chatId, "<b>❌Мад жагъай затI авач</b>")
                    .parseMode(ParseMode.HTML));
        }
    }
}