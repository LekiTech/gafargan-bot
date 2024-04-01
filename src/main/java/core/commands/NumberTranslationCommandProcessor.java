package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.DataStorage;
import core.ui.KeypadCreator;

public class NumberTranslationCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public NumberTranslationCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        DataStorage.instance().saveSelectedDictionary(chatId, CommandsList.LEZGI_NUMBERS);
        ReplyKeyboardMarkup keypad = new KeypadCreator().createMainMenuKeypad();
        String message = """
                <b>Кхьихь число, ботди таржума ийида</b>.
                Месела: "<code>978</code>" ва я "<code>агъзурни вад вишни пуд</code>"
                
                <i>Ведите число в формате цифр или текста, например: "978" или "агъзурни вад вишни пуд".
                Текстовый вариант вводить только на лезгинском языке.</i>
                """;
        bot.execute(new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .replyMarkup(keypad));
    }
}
