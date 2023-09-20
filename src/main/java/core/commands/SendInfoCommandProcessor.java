package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class SendInfoCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public SendInfoCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        String msg = "Бот туькIуьрнавайди\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB: Артур Магомедов.\n\n"
                + "Гафарганар:\n"
                + "- Лезги-урус гафарган\uD83D\uDCD7 (Бабаханов М.Б.)\n"
                + "- Урус-лезги гафарган\uD83D\uDCD5 (Гаджиев М.М.)\n\n"
                + "<b>Чешме: <a href=\"https://github.com/LekiTech/data-sources\">LekiTech</a></b>";
        bot.execute(new SendMessage(chatId, msg).parseMode(ParseMode.HTML).disableWebPagePreview(true));
    }
}