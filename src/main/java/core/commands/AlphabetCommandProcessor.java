package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;

import static core.commands.CommandsList.*;

@AllArgsConstructor
public class AlphabetCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    @Override
    public void execute() {
        var chatId = message.chat().id();
        String outputMessage = """
                🔤<b>Лезги гьарфар (<a href="https://www.youtube.com/watch?v=z01MAVOtyNM"><i>чешме</i></a>)</b>
                <b>Илис са гьарфунал яб гудайвал👇🏼</b>
                """;
        InlineKeyboardMarkup inlineKeyboardMarkup = createAlphabetButtons();
        bot.execute(new SendMessage(chatId, outputMessage)
                .replyMarkup(inlineKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true));
    }

    private InlineKeyboardMarkup createAlphabetButtons() {
        String[][] alphabetMatrix = {
                {"А", "Б", "В", "Г", "Гъ", "Гь"},
                {"Д", "Е", "Ж", "З", "И", "Й"},
                {"К", "Къ", "Кь", "КI", "Л", "М"},
                {"Н", "П", "ПI", "Р", "С", "Т"},
                {"ТI", "У", "Уь", "Ф", "Х", "Хъ"},
                {"Хь", "Ц", "ЦI", "Ч", "ЧI", "Ш"},
                {"Ъ", "Ы", "Ь", "Э", "Ю", "Я"},
        };
        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[7][6];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                buttons[i][j] = new InlineKeyboardButton(alphabetMatrix[i][j])
                        .callbackData(ALPHABET + EQUALS + alphabetMatrix[i][j] + EQUALS + "lez");
            }
        }
        return new InlineKeyboardMarkup(buttons);
    }
}