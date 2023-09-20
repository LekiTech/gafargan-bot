package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import core.parser.DictionaryRepository;
import core.parser.model.Example;
import core.parser.model.ExpressionDetails;
import javassist.NotFoundException;

import java.util.List;

import static core.BotUpdates.*;

public class SendResponseFromButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;
    public static final String COMMAND_EXAMPLE_SUFFIX = "=example";

    public SendResponseFromButtonCommandProcessor(Message message, TelegramBot bot, CallbackQuery callbackQuery) {
        this.message = message;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void execute() throws NotFoundException {
        var chatId = message.chat().id();
        String spelling = callbackQuery.data();
        var language = selectedLanguage.getDictionaryLanguage(chatId);
        switch (language) {
            case CommandsList.LEZGI_RUS:
                if (spelling.contains(COMMAND_EXAMPLE_SUFFIX)) {
                    sendGeneralExample(lezgiRusDictionary, spelling, chatId);
                    break;
                }
                new WordSearchCommandProcessor(message, bot).sendAnswerOfFoundWord(lezgiRusDictionary, spelling, chatId);
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                break;
            case CommandsList.RUS_LEZGI:
                if (spelling.contains(COMMAND_EXAMPLE_SUFFIX)) {
                    sendGeneralExample(rusLezgiDictionary, spelling, chatId);
                    break;
                }
                new WordSearchCommandProcessor(message, bot).sendAnswerOfFoundWord(rusLezgiDictionary, spelling, chatId);
                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                break;
            default:
                return;
        }
    }

    private void sendGeneralExample(DictionaryRepository dictionary, String spelling, long chatId) throws NotFoundException {
        StringBuilder outputMessage = new StringBuilder();
        String[] spell = spelling.split("=");
        if (spell.length == 2) {
            spelling = spell[0];
        }
        outputMessage.append("<i>").append(spelling.substring(0, 1).toUpperCase()).append(spelling.substring(1)
                .replaceAll("[i1lӏ|]", "I")).append("</i>⤵️\n\n");
        List<ExpressionDetails> expressionDetails = dictionary.getDefinitions(spelling);
        for (ExpressionDetails details : expressionDetails) {
            if (details.getExamples() != null) {
                int count = 0;
                for (Example examples : details.getExamples()) {
                    outputMessage.append("   - ").append(examples.getRaw()
                            .replaceAll("<", "[")
                            .replaceAll(">", "]")
                            .replaceAll("\\{", "<b><i>")
                            .replaceAll("}", "</i></b> -")).append("\n");
                    if (count > 20) {
                        break;
                    }
                    count++;
                }
            }
        }
        bot.execute(new SendMessage(chatId, outputMessage.toString()).parseMode(ParseMode.HTML));
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
    }
}