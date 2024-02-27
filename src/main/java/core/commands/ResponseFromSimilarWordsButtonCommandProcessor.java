package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import core.database.DataStorage;
import core.searchers.SearchResponseHandler;
import javassist.NotFoundException;

import static core.bothandler.BotUpdates.*;

public class ResponseFromSimilarWordsButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;

    public ResponseFromSimilarWordsButtonCommandProcessor(Message message, TelegramBot bot, CallbackQuery callbackQuery) {
        this.message = message;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void execute() throws NotFoundException {
        var chatId = message.chat().id();
        String userMessage = callbackQuery.data();
        var language = DataStorage.instance().getLastSelectedDictionary(chatId);
        SearchResponseHandler handler = new SearchResponseHandler(bot);
        switch (language) {
            case CommandsList.LEZGI_RUS -> {
                handler.findResponse(lezgiRusDictionary, userMessage, chatId);
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            case CommandsList.RUS_LEZGI -> {
                handler.findResponse(rusLezgiDictionary, userMessage, chatId);
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            default -> {
            }
        }
    }
}