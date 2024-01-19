package core.factory;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import core.commands.*;
import core.storage.DataStorage;

public class CommandsFactory {

    public static final String COMMAND_EXAMPLE_SUFFIX = "=example";

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
            case CommandsList.ABOUT_US:
                return new AboutUsCommandProcessor(message, bot);
            default:
                if (DataStorage.instance().getLastSelectedDictionary(chatId) == null) {
                    return new DefaultCommandProcessor(message, bot);
                }
                return new WordSearchCommandProcessor(message, bot);
        }
    }

    public static ChatCommandProcessor createCallbackProcessor(CallbackQuery callbackQuery, TelegramBot bot) {
        var message = callbackQuery.message();
        String userMessage = callbackQuery.data();
        if (userMessage.contains(COMMAND_EXAMPLE_SUFFIX)) {
            return new ResponseFromExamplesButtonCommandProcessor(message, bot, callbackQuery);
        }
        return new ResponseFromButtonsOfSupposedWordsCommandProcessor(message, bot, callbackQuery);
    }
}