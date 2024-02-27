package core.searchers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.dictionary.parser.DictionaryRepository;

import static core.ui.InlineKeyboardCreator.createInlineKeyboard;
import static core.bothandler.BotUpdates.listOfExample;

/**
 * The SearchResponseHandler class manages responses to user queries in a chat environment.
 * It handles searching for translations or similar words based on user input and sends the
 * appropriate response back to the user.
 */
public class SearchResponseHandler {

    private final TelegramBot bot;

    public SearchResponseHandler(TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Finds the appropriate response for the user's query and sends it to the chat.
     *
     * @param dictionary  The dictionary repository containing the necessary data for searching.
     * @param userMessage The message input by the user to be searched for.
     * @param chatId      The ID of the chat where the response should be sent.
     */
    public void findResponse(DictionaryRepository dictionary, String userMessage, Long chatId) {
        Response responseBySpelling = new SearchBySpelling().findTranslationBySpelling(dictionary, userMessage);
        if (responseBySpelling != null) {
            sendResponseToUser(responseBySpelling, chatId);
            return;
        }
        Response responseByExamples = new SearchByExample().findTranslationByExamples(listOfExample, userMessage);
        if (responseByExamples != null) {
            sendResponseToUser(responseByExamples, chatId);
            return;
        }
        Response responseByFuzzySearch = new FuzzySearchBySpelling().findSimilarWordsBySpelling(dictionary, userMessage);
        if (responseByFuzzySearch != null) {
            sendResponseToUser(responseByFuzzySearch, chatId);
        }
    }

    /**
     * Sends the provided response to the specified chat.
     * If the response contains example buttons, it includes them in the message as inline keyboards.
     *
     * @param responseToUser The response to be sent to the user.
     * @param chatId         The ID of the chat where the response should be sent.
     */
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
