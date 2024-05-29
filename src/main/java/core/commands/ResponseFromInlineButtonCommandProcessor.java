package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.*;
import javassist.NotFoundException;

import static core.commands.CommandsList.*;

public class ResponseFromInlineButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;
    private final DictionaryRepository dictionaries;
    private final String lang;

    public ResponseFromInlineButtonCommandProcessor(Message message,
                                                    DictionaryRepository dictionaries,
                                                    TelegramBot bot,
                                                    CallbackQuery callbackQuery,
                                                    String lang) {
        this.message = message;
        this.dictionaries = dictionaries;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
        this.lang = lang;
    }

    @Override
    public void execute() throws NotFoundException {
        var chatId = message.chat().id();
        var userMessage = callbackQuery.data();
        switch (lang) {
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
        } else if (userMessage.contains("=searchMore")) {
            String[] spell = userMessage.split("=");
            String wordToSearch = "";
            if (spell.length == 2) {
                wordToSearch = spell[0];
                Response response = new SearchByExample().searchResponse(null, dictionaries, wordToSearch);
                if (response != null) {
                    bot.execute(new SendMessage(chatId, response.messageText())
                            .parseMode(ParseMode.HTML));
                    return;
                } else {
                    bot.execute(new SendMessage(chatId, "<b>❌Мад жагъай затI авач</b>")
                            .parseMode(ParseMode.HTML));
                    return;
                }
            }
        }
        SearchResponseHandler handler = new SearchResponseHandler(bot);
        handler.sendResponse(lang, dictionaries, userMessage, chatId);
    }
}