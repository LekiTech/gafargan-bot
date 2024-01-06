package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class AboutUsCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public AboutUsCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        String msg = "Гафарганрин авторар\uD83D\uDC64:\n"
                + "\uD83D\uDCD7Лезги-урус гафарган — <b>Бабаханов М.Б.</b>\n"
                + "\uD83D\uDCD5Урус-лезги гафарган — <b>Гаджиев М.М.</b>\n"
                + "\n"
                + "<b>Чешме: <a href=\"https://github.com/LekiTech/data-sources\">LekiTech</a></b>\n"
                + "\n"
                + "<b>Ботдин патахъай суалар, бот хъсанарун ийидай меселаяр аватIа ва я къимет гуз кIанзаватIа, кхьихь чаз</b>\uD83D\uDC47\uD83C\uDFFC\n";
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("\uD83D\uDCE9Кхьихь чаз").url("https://t.me/GafarganSupportBot");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardButton);
        bot.execute(new SendMessage(chatId, msg)
                .replyMarkup(inlineKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true));
    }
}