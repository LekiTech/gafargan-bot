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

    public void sendResponse(String lang, DictionaryRepository dictionaries, String userMessage, Long chatId) {
        Response response = getResponse(lang, dictionaries, userMessage);
        if (response.exampleButton() == null || response.exampleButton().isEmpty()) {
            bot.execute(new SendMessage(chatId, response.messageText())
                    .parseMode(ParseMode.HTML));
        } else {
            InlineKeyboardMarkup inlineKeyboard = createInlineKeyboard(response.exampleButton());
            bot.execute(new SendMessage(chatId, response.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(inlineKeyboard));
        }
    }

    private Response getResponse(String lang, DictionaryRepository dictionaries, String userMessage) {
        Response responseBySpelling = new SearchBySpelling().searchResponse(lang, dictionaries, userMessage);
        if (responseBySpelling != null) {
            return responseBySpelling;
        }
        Response responseByDefinition = new SearchByDefinition().searchResponse(lang, dictionaries, userMessage);
        if (responseByDefinition != null) {
            return responseByDefinition;
        }
        Response responseByDialectDict = new SearchByDialectDict().searchResponse(lang, dictionaries, userMessage);
        if (responseByDialectDict != null) {
            return responseByDialectDict;
        }
        Response responseByExamples = new SearchByExample().searchResponse(lang, dictionaries, userMessage);
        if (responseByExamples != null) {
            return responseByExamples;
        }
        Response responseByFuzzySearch = new FuzzySearchBySpelling().searchResponse(lang, dictionaries, userMessage);
        if (responseByFuzzySearch != null) {
            return responseByFuzzySearch;
        }
        return null;
    }
}
