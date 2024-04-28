package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import core.commands.*;
import core.database.DataStorage;
import core.dictionary.parser.DictionaryRepository;
import core.utils.AlphabetBuilder;

public class CommandsFactory {

    public static ChatCommandProcessor createMessageProcessor(Message message,
                                                              DictionaryRepository dictionaries,
                                                              TelegramBot bot) {
        String userMessage = message.text();
        switch (userMessage) {
            case CommandsList.START:
                return new StartCommandProcessor(message, bot);
            case CommandsList.LEZGI_RUS:
                return new LezgiRusDictionaryCommandProcessor(message, bot);
            case CommandsList.RUS_LEZGI:
                return new RusLezgiDictionaryCommandProcessor(message, bot);
            case CommandsList.LEZGI_NUMBERS:
                return new NumberTranslationCommandProcessor(message, bot);
            case CommandsList.LEZGI_ALPHABET:
                return new AlphabetCommandProcessor(message, bot);
            case CommandsList.INFO:
                return new InfoCommandProcessor(message, bot);
            case CommandsList.LEZ_RUS_TAL,
                    CommandsList.RUS_LEZ_GADZH,
                    CommandsList.LEZ_RUS_BB:
                return new DefaultCommandProcessor(message, bot);
            default:
                var chatId = message.chat().id();
                if (DataStorage.instance().getLastSelectedDictionary(chatId) == null) {
                    return new DefaultCommandProcessor(message, bot);
                }
                return new ResponseSearchCommandProcessor(message, dictionaries, bot);
        }
    }

    public static ChatCommandProcessor createCallbackProcessor(Message message,
                                                               DictionaryRepository dictionaries,
                                                               TelegramBot bot,
                                                               CallbackQuery callbackQuery) {
        String userMessage = callbackQuery.data();
        AlphabetBuilder alphabet = new AlphabetBuilder();
        if (alphabet.containsKey(userMessage)) {
            return new ResponseFromAlphabetCommandProcessor(message, bot, callbackQuery);
        }
        return new ResponseFromInlineButtonCommandProcessor(message, dictionaries, bot, callbackQuery);
    }
}