package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.Answer;
import core.searchers.SearchInDictionary;

import static core.tgbothandler.BotUpdates.*;
import static core.ui.InlineKeyboardCreator.createInlineKeyboard;
import static core.utils.SearchStringNormalizer.normalizeString;

public class WordSearchCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public WordSearchCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = normalizeString(message.text());
        var language = DataStorage.instance().getLastSelectedDictionary(chatId);
        switch (language) {
            case CommandsList.LEZGI_RUS -> sendAnswerToUser(lezgiRusDictionary, userMessage, chatId);
            case CommandsList.RUS_LEZGI -> sendAnswerToUser(rusLezgiDictionary, userMessage, chatId);
            default -> {
            }
        }
    }

    public void sendAnswerToUser(DictionaryRepository dictionary, String userMessage, Long chatId) {
        SearchInDictionary search = new SearchInDictionary();
        Answer answer = search.sendAnswerFromDictionary(dictionary, userMessage);
        if (answer.exampleButton() == null || answer.exampleButton().isEmpty()) {
            bot.execute(new SendMessage(chatId, answer.messageText())
                    .parseMode(ParseMode.HTML));
        } else {
            InlineKeyboardMarkup inlineKeyboard = createInlineKeyboard(answer.exampleButton());
            bot.execute(new SendMessage(chatId, answer.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(inlineKeyboard));
        }
    }
}