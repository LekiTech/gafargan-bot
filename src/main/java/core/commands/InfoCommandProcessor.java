package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class InfoCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public InfoCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        String msg = String.format("""
                Чи проектар📌:
                <b>⌨️Лезги клавиатура — <a href="%s"><i>ссылка</i></a></b>
                <b>🌐Сайт гафарган — gafargan.com</b>
                                
                Гафарганрин авторар👤:
                <b>📗Лезги-урус гафарган —</b> Бабаханов М.Б.
                <b>📕Урус-лезги гафарган —</b> Гаджиев М.М.
                                
                <b>Чешме: <a href="https://github.com/LekiTech/data-sources">LekiTech</a></b>
                                
                <b>Хотите принять участие в проектах? Свяжитесь с нами👇🏼</b>
                """, "https://apps.apple.com/ru/app/%D0%BB%D0%B5%D0%B7%D0%B3%D0%B8%D0%BD%D1%81%D0%BA%D0%B0%D1%8F-%D0%BA%D0%BB%D0%B0%D0%B2%D0%B8%D0%B0%D1%82%D1%83%D1%80%D0%B0/id6444746265"
        );
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("\uD83D\uDCE9✍\uD83C\uDFFC").url("https://t.me/FeedbackGafarganBot");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardButton);
        bot.execute(new SendMessage(chatId, msg)
                .replyMarkup(inlineKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true));
    }
}