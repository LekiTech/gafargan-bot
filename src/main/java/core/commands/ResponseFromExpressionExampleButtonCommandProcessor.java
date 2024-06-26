package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.Response;
import core.searchers.SearchExpressionExample;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseFromExpressionExampleButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;
    private final String word;
    private final String lang;

    @Override
    public void execute() throws NotFoundException {
        var chatId = message.chat().id();
        SearchExpressionExample searchExpExample = new SearchExpressionExample();
        Response response = searchExpExample.getExampleExpression(lang, dictionaries, word);
        bot.execute(new SendMessage(chatId, response.messageText()).parseMode(ParseMode.HTML));
    }
}