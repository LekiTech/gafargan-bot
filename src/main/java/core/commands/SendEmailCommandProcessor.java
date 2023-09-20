package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class SendEmailCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public SendEmailCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        String msg = "<b>Эгер квез суалар аватIа, бот жуьреба-жуьре ийидай меселаяр аватIа ва я " +
                "къимет гуз кIанзаватIа, квевай чаз иниз кхьиз жеда:</b>\n\n"
                + "\uD83D\uDCE9<b> lezgilanguage@gmail.com </b>";
        bot.execute(new SendMessage(chatId, msg).parseMode(ParseMode.HTML));
    }
}