package core.searchers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.dictionary.parser.DictionaryRepository;
import core.ui.InlineKeyboardCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SearchResponseHandler {

    private final TelegramBot bot;

    public void sendResponse(String lang, DictionaryRepository dictionaries, String userMessage, Long chatId) {
        Response responseBySpelling = new SearchBySpelling().searchResponse(lang, dictionaries, userMessage);
        if (responseBySpelling != null) {
            if (responseBySpelling.buttonKey() != null) {
                var keyboard
                        = InlineKeyboardCreator.createExpressionExampleButton(responseBySpelling.buttonKey(), lang);
                bot.execute(new SendMessage(chatId, responseBySpelling.messageText())
                        .parseMode(ParseMode.HTML)
                        .replyMarkup(keyboard)
                );
            } else {
                bot.execute(new SendMessage(chatId, responseBySpelling.messageText())
                        .parseMode(ParseMode.HTML));
            }
            return;
        }
        Response responseByDefinition = new SearchByDefinition().searchResponse(lang, dictionaries, userMessage);
        if (responseByDefinition != null) {
            var keyboard
                    = InlineKeyboardCreator.createSearchButtonFromDefinitionResponse(responseByDefinition.buttonKey(), lang);
            bot.execute(new SendMessage(chatId, responseByDefinition.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(keyboard)
            );
            return;
        }
        Response responseByDialectDict = new SearchByDialectDict().searchResponse(lang, dictionaries, userMessage);
        if (responseByDialectDict != null) {

            return;
        }
        Response responseByExamples = new SearchByExample().searchResponse(lang, dictionaries, userMessage);
        if (responseByExamples != null) {
            var keyboard
                    = InlineKeyboardCreator.createSearchSuggestionsButton(userMessage, lang);
            bot.execute(new SendMessage(chatId, responseByExamples.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(keyboard)
            );
            return;
        }
        Response responseByFuzzySearch = new FuzzySearchBySpelling().searchResponse(lang, dictionaries, userMessage);
        if (responseByFuzzySearch != null) {
            var inlineKeyboard
                    = InlineKeyboardCreator.createSuggestionButtons(responseByFuzzySearch.suggestions(), lang);
            bot.execute(new SendMessage(chatId, responseByFuzzySearch.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(inlineKeyboard));
        }
    }
}
