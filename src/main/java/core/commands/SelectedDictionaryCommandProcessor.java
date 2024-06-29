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
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.sql.Timestamp;
import java.util.UUID;

import static core.commands.CommandsList.*;

@AllArgsConstructor
public class SelectedDictionaryCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final ApplicationContext context;

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var dictionaryKey = message.text();
        var outputMessage = getOutputMessage(dictionaryKey);
        KeypadCreator keypadCreator = new KeypadCreator();
        ReplyKeyboardMarkup keypad = keypadCreator.createMainMenuKeypad();
        bot.execute(new SendMessage(chatId, outputMessage)
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
        var selectedDictionary = context.getBean(SelectedDictionaryService.class);
        selectedDictionary.saveDictionary(new SelectedDictionary(
                        UUID.randomUUID(),
                        dictionaryKey,
                        new UserChatId(chatId, new Timestamp(System.currentTimeMillis())),
                        new Timestamp(System.currentTimeMillis())
                )
        );
    }

    private String getOutputMessage(String dictionaryKey) {
        return switch (dictionaryKey) {
            case LEZGI_RUS, LEZGI_ENG -> "<b>✏️Лезги чIалал кхьихь</b>";
            case RUS_LEZGI -> "<b>✏Урус чIалал кхьихь</b>";
            case LEZGI_DIALECT_DICT -> "<b>✏️Кхьихь са нугъатдин гаф</b>";
            case LEZGI_NUMBERS -> "<b>✏️Числительное кхьихь</b>";
            default -> "";
        };
    }
}
