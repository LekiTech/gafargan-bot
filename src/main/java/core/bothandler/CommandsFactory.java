package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import core.commands.*;
import core.database.DataStorage;

/**
 * Factory class for creating command processors based on incoming messages or callback queries.
 */
public class CommandsFactory {

    /**
     * Creates a command processor for handling incoming text messages.
     *
     * @param message The incoming message.
     * @param bot     The TelegramBot instance.
     * @return the appropriate ChatCommandProcessor based on the message content.
     */
    public static ChatCommandProcessor createMessageProcessor(Message message, TelegramBot bot) {
        var chatId = message.chat().id();
        String userMessage = message.text();
        switch (userMessage) {
            case CommandsList.START:
                return new StartCommandProcessor(message, bot);
            case CommandsList.LEZGI_RUS:
                return new LezgiRusDictionaryCommandProcessor(message, bot);
            case CommandsList.RUS_LEZGI:
                return new RusLezgiDictionaryCommandProcessor(message, bot);
            case CommandsList.INFO:
                return new InfoCommandProcessor(message, bot);
            default:
                if (DataStorage.instance().getLastSelectedDictionary(chatId) == null) {
                    return new DefaultCommandProcessor(message, bot);
                }
                return new TranslationFinderCommandProcessor(message, bot);
        }
    }

    /**
     * Creates a command processor for handling callback queries.
     *
     * @param callbackQuery The callback query received.
     * @param bot           The TelegramBot instance.
     * @return the appropriate ChatCommandProcessor based on the callback query content.
     */
    public static ChatCommandProcessor createCallbackProcessor(CallbackQuery callbackQuery, TelegramBot bot) {
        var message = callbackQuery.message();
        String userMessage = callbackQuery.data();
        if (userMessage.contains(CommandsList.EXAMPLE_SUFFIX)) {
            return new ResponseFromExampleButtonCommandProcessor(message, bot, callbackQuery);
        }
        return new ResponseFromSimilarWordsButtonCommandProcessor(message, bot, callbackQuery);
    }
}