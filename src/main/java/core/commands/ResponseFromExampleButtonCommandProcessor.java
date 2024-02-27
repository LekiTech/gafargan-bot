package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.searchers.Response;
import core.searchers.SearchByExamplesOfSpelling;
import javassist.NotFoundException;

import static core.bothandler.BotUpdates.*;

public class ResponseFromExampleButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;

    public ResponseFromExampleButtonCommandProcessor(Message message, TelegramBot bot, CallbackQuery callbackQuery) {
        this.message = message;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void execute() throws NotFoundException {
        var chatId = message.chat().id();
        String userMessage = callbackQuery.data();
        var language = DataStorage.instance().getLastSelectedDictionary(chatId);
        SearchByExamplesOfSpelling searchExample = new SearchByExamplesOfSpelling();
        switch (language) {
            case CommandsList.LEZGI_RUS -> {
                Response response = searchExample.sendExample(lezgiRusDictionary, userMessage);
                bot.execute(new SendMessage(chatId, response.messageText()).parseMode(ParseMode.HTML));
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            case CommandsList.RUS_LEZGI -> {
                Response response = searchExample.sendExample(rusLezgiDictionary, userMessage);
                bot.execute(new SendMessage(chatId, response.messageText()).parseMode(ParseMode.HTML));
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            default -> {
            }
        }
    }
}