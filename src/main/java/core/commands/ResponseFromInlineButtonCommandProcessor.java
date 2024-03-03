package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.Response;
import core.searchers.SearchForExampleExpression;
import core.searchers.SearchResponseHandler;
import javassist.NotFoundException;

import static core.commands.CommandsList.*;

public class ResponseFromInlineButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;
    private final DictionaryRepository dictionaries;

    public ResponseFromInlineButtonCommandProcessor(Message message,
                                                    DictionaryRepository dictionaries,
                                                    TelegramBot bot,
                                                    CallbackQuery callbackQuery) {
        this.message = message;
        this.dictionaries = dictionaries;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void execute() throws NotFoundException {
        var chatId = message.chat().id();
        var userMessage = callbackQuery.data();
        var language = DataStorage.instance().getLastSelectedDictionary(chatId);
        switch (language) {
            case LEZGI_RUS -> {
                sendResponseFromInlineButton("lez", userMessage, chatId);
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            case RUS_LEZGI -> {
                sendResponseFromInlineButton("rus", userMessage, chatId);
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            default -> {
            }
        }
    }

    private void sendResponseFromInlineButton(String lang, String userMessage, Long chatId) {
        if (userMessage.contains("=example")) {
            SearchForExampleExpression searchForExampleExpression = new SearchForExampleExpression();
            Response response = searchForExampleExpression.sendExampleExpression(lang, dictionaries, userMessage);
            bot.execute(new SendMessage(chatId, response.messageText()).parseMode(ParseMode.HTML));
            return;
        }
        SearchResponseHandler handler = new SearchResponseHandler(bot);
        handler.findResponse(lang, dictionaries, userMessage, chatId);
    }
}