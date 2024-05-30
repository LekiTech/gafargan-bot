package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.entity.SelectedDictionary;
import core.database.entity.UserChatId;
import core.database.service.SelectedDictionaryService;
import core.ui.KeypadCreator;
import org.springframework.context.ApplicationContext;

import java.sql.Timestamp;
import java.util.UUID;

public class LezgiRusDictionaryCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final ApplicationContext context;

    public LezgiRusDictionaryCommandProcessor(Message message, TelegramBot bot, ApplicationContext context) {
        this.message = message;
        this.bot = bot;
        this.context = context;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        KeypadCreator keypadCreator = new KeypadCreator();
        ReplyKeyboardMarkup keypad = keypadCreator.createMainMenuKeypad();
        bot.execute(new SendMessage(chatId, "<b>✏️Лезги чIалал кхьихь</b>")
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
        SelectedDictionaryService selectedDictionaryService = context.getBean(SelectedDictionaryService.class);
        selectedDictionaryService.saveDictionary(new SelectedDictionary(
                UUID.randomUUID(),
                CommandsList.LEZGI_RUS,
                new UserChatId(chatId, new Timestamp(System.currentTimeMillis())),
                new Timestamp(System.currentTimeMillis()))
        );
    }
}