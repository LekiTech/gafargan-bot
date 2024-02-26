package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.searchers.Answer;
import core.searchers.SearchForExampleOfWord;
import javassist.NotFoundException;

import static core.tgbothandler.BotUpdates.*;

public class ResponseFromExamplesButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;

    public ResponseFromExamplesButtonCommandProcessor(Message message, TelegramBot bot, CallbackQuery callbackQuery) {
        this.message = message;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void execute() throws NotFoundException {
        var chatId = message.chat().id();
        String userMessage = callbackQuery.data();
        var language = DataStorage.instance().getLastSelectedDictionary(chatId);
        SearchForExampleOfWord searchExample = new SearchForExampleOfWord();
        switch (language) {
            case CommandsList.LEZGI_RUS -> {
                Answer answer = searchExample.sendExample(lezgiRusDictionary, userMessage);
                bot.execute(new SendMessage(chatId, answer.messageText()).parseMode(ParseMode.HTML));
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            case CommandsList.RUS_LEZGI -> {
                Answer answer = searchExample.sendExample(rusLezgiDictionary, userMessage);
                bot.execute(new SendMessage(chatId, answer.messageText()).parseMode(ParseMode.HTML));
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            }
            default -> {
            }
        }
    }
}