package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import core.commands.*;
import core.database.service.SelectedDictionaryService;
import core.dictionary.parser.DictionaryRepository;
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
            case OLD_LEZ_RUS_TAL,
                    OLD_LEZ_RUS_BB,
                    OLD_RUS_LEZ_GADZH,
                    OLD_LEZGI_ALPHABET_OLD,
                    OLD_ABOUT_US,
                    OLD_LEZGI_RUS,
                    OLD_RUS_LEZGI,
                    OLD_LEZGI_ENG,
                    OLD_LEZGI_DIALECT_DICT,
                    OLD_LEZGI_NUMBERS,
                    OLD_LEZGI_ALPHABET:
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
                                                               CallbackQuery callbackQuery) {
        String[] buttonCallbackData = callbackQuery.data().split(EQUALS);
        String commandKey = buttonCallbackData[0];
        String messageFromButton = buttonCallbackData[1];
        String lang = buttonCallbackData[2];
        return switch (commandKey) {
            case ALPHABET, AUDIO_ALPHABET ->
                    new ResponseFromAlphabetCommandProcessor(message, bot, commandKey, messageFromButton);
            case SUGGESTION ->
                    new ResponseFromSuggestionButtonCommandProcessor(message, dictionaries, bot, messageFromButton, lang);
            case EXPRESSION_EXAMPLE ->
                    new ResponseFromExpressionExampleButtonCommandProcessor(message, dictionaries, bot, messageFromButton, lang);
            case SEARCH_IN_EXAMPLES ->
                    new ResponseFromSearchInExamplesButtonCommandProcessor(message, dictionaries, bot, messageFromButton, lang);
            case SEARCH_SUGGESTIONS ->
                    new ResponseFromSearchSuggestionsButtonCommandProcessor(message, dictionaries, bot, messageFromButton, lang);
            case AUDIO_NUMERAL -> new ResponseFromNumeralAudioButtonCommandProcessor(message, bot, messageFromButton);
            default -> null;
        };
    }
}