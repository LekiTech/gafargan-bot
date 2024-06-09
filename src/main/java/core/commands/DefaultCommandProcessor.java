package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.entity.UserChatId;
import core.database.service.UserChatIdService;
import core.ui.KeypadCreator;
import org.springframework.context.ApplicationContext;

import java.sql.Timestamp;

public class DefaultCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final ApplicationContext context;

    public DefaultCommandProcessor(Message message, TelegramBot bot, ApplicationContext context) {
        this.message = message;
        this.bot = bot;
        this.context = context;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        KeypadCreator keypadCreator = new KeypadCreator();
        ReplyKeyboardMarkup keypad = keypadCreator.createMainMenuKeypad();
        String outputMsg = """
                Ботдин цIийивилер акъатна ва я куьне таржумачи хкягънавач.
                <b>Садра мад хкягъ хъия👇🏼</b>
                """;
        bot.execute(new SendMessage(chatId, outputMsg)
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
        UserChatIdService userChatIdService = context.getBean(UserChatIdService.class);
        userChatIdService.saveUser(new UserChatId(chatId, new Timestamp(System.currentTimeMillis())));
    }
}