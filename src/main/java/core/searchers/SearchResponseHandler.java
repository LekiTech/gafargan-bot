package core.searchers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.dictionary.parser.DictionaryRepository;


import static core.ui.InlineKeyboardCreator.createInlineKeyboard;

public class SearchResponseHandler {

    private final TelegramBot bot;

    public SearchResponseHandler(TelegramBot bot) {
        this.bot = bot;
    }

    public void findResponse(String lang,
                             DictionaryRepository dictionaries,
                             String userMessage,
                             Long chatId) {
        Response responseBySpelling = new SearchBySpelling().findResponseBySpelling(lang, dictionaries, userMessage);
        if (responseBySpelling != null) {
            sendResponseToUser(responseBySpelling, chatId);
            return;
        }
        Response responseByExamples = new SearchByExample().findResponseByExamples(dictionaries, userMessage);
        if (responseByExamples != null) {
            sendResponseToUser(responseByExamples, chatId);
            return;
        }
        Response responseByFuzzySearch = new FuzzySearchBySpelling().findSimilarWordsBySpelling(lang, dictionaries, userMessage);
        if (responseByFuzzySearch != null) {
            sendResponseToUser(responseByFuzzySearch, chatId);
        }
    }

    private void sendResponseToUser(Response responseToUser, Long chatId) {
        if (responseToUser.exampleButton() == null || responseToUser.exampleButton().isEmpty()) {
            bot.execute(new SendMessage(chatId, responseToUser.messageText())
                    .parseMode(ParseMode.HTML));
        } else {
            InlineKeyboardMarkup inlineKeyboard = createInlineKeyboard(responseToUser.exampleButton());
            bot.execute(new SendMessage(chatId, responseToUser.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(inlineKeyboard));
        }
    }
}
