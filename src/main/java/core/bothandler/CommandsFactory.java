package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import core.commands.*;
import core.database.service.SelectedDictionaryService;
import core.dictionary.parser.DictionaryRepository;
import core.utils.AlphabetBuilder;
import org.springframework.context.ApplicationContext;

import static core.commands.CommandsList.*;

public class CommandsFactory {

    public static ChatCommandProcessor createMessageProcessor(Message message,
                                                              DictionaryRepository dictionaries,
                                                              TelegramBot bot,
                                                              ApplicationContext context) {
        String userMessage = message.text();
        switch (userMessage) {
            case START:
                return new StartCommandProcessor(message, bot, context);
            case LEZGI_RUS:
                return new LezgiRusDictionaryCommandProcessor(message, bot, context);
            case RUS_LEZGI:
                return new RusLezgiDictionaryCommandProcessor(message, bot, context);
            case LEZGI_ENG:
                return new LezgiEngDictionaryCommandProcessor(message, bot, context);
            case LEZGI_DIALECT_DICT:
                return new LezgiDialectDictionaryCommandProcessor(message, bot, context);
            case LEZGI_NUMBERS:
                return new NumberTranslationCommandProcessor(message, bot, context);
            case LEZGI_ALPHABET:
                return new AlphabetCommandProcessor(message, bot);
            case INFO:
                return new InfoCommandProcessor(message, bot);
            case LEZ_RUS_TAL,
                    RUS_LEZ_GADZH,
                    LEZ_RUS_BB,
                    LEZGI_ALPHABET_OLD,
                    ABOUT_US:
                return new DefaultCommandProcessor(message, bot, context);
            default:
                SelectedDictionaryService selectedDictionaryService = context.getBean(SelectedDictionaryService.class);
                String lang = selectedDictionaryService.findSelectedDictionary(message.chat().id());
                if (lang == null) {
                    return new DefaultCommandProcessor(message, bot, context);
                }
                return new ResponseSearchCommandProcessor(message, dictionaries, bot, lang);
        }
    }

    public static ChatCommandProcessor createCallbackProcessor(Message message,
                                                               DictionaryRepository dictionaries,
                                                               TelegramBot bot,
                                                               CallbackQuery callbackQuery,
                                                               ApplicationContext context) {
        String userMessage = callbackQuery.data();
        AlphabetBuilder alphabet = new AlphabetBuilder();
        if (alphabet.containsKey(userMessage)) {
            return new ResponseFromAlphabetCommandProcessor(message, bot, callbackQuery);
        }
        SelectedDictionaryService selectedDictionaryService = context.getBean(SelectedDictionaryService.class);
        String lang = selectedDictionaryService.findSelectedDictionary(message.chat().id());
        return new ResponseFromInlineButtonCommandProcessor(message, dictionaries, bot, callbackQuery, lang);
    }
}