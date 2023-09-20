package core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import core.commands.*;

import static core.BotUpdates.selectedLanguage;

public class CommandsFactory {

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
                return new SendInfoCommandProcessor(message, bot);
            case CommandsList.EMAIL_US:
                return new SendEmailCommandProcessor(message, bot);
            default:
                if (selectedLanguage.getDictionaryLanguage(chatId) == null) {
                    return new DefaultCommandProcessor(message, bot);
                }
                return new WordSearchCommandProcessor(message, bot);
        }
    }

    public static ChatCommandProcessor createCallbackProcessor(CallbackQuery callbackQuery, TelegramBot bot) {
        var message = callbackQuery.message();
        return new SendResponseFromButtonCommandProcessor(message, bot, callbackQuery);
    }
}