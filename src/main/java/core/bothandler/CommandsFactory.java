package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import core.commands.*;
import core.database.service.SelectedDictionaryService;
import core.dictionary.parser.DictionaryRepository;
import core.utils.AlphabetBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static core.commands.CommandsList.*;

@Component
public class CommandsFactory {

    public static ChatCommandProcessor createMessageProcessor(Message message,
                                                              DictionaryRepository dictionaries,
                                                              TelegramBot bot,
                                                              ApplicationContext context) {
        String userMessage = message.text();
        switch (userMessage) {
            case START:
                return new StartCommandProcessor(message, bot, context);
            case INFO:
                return new InfoCommandProcessor(message, bot);
            case LEZGI_ALPHABET:
                return new AlphabetCommandProcessor(message, bot);
            case LEZGI_RUS, LEZGI_ENG, RUS_LEZGI, LEZGI_NUMBERS, LEZGI_DIALECT_DICT:
                return new SelectedDictionaryCommandProcessor(message, bot, context);
            case LEZ_RUS_TAL, RUS_LEZ_GADZH, LEZ_RUS_BB, LEZGI_ALPHABET_OLD, ABOUT_US:
                return new DefaultCommandProcessor(message, bot, context);
            default:
                var selectedDictionary = context.getBean(SelectedDictionaryService.class);
                String lang = selectedDictionary.findSelectedDictionary(message.chat().id());
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