package core.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import core.database.entity.UserChatId;
import core.database.service.UserChatIdService;
import org.springframework.context.ApplicationContext;

import java.util.*;

public class Mailing {

    public static void startMailing(String messageToMailing, ApplicationContext context, TelegramBot bot) throws InterruptedException {
        UserChatIdService service = context.getBean(UserChatIdService.class);
        List<UserChatId> allUsers = service.getAllChatId();
        Set<Long> allChatId = new HashSet<>();
        for (UserChatId chatId : allUsers) {
            allChatId.add(chatId.getChatId());
        }
        bot.execute(new SendMessage(Env.instance().getSecretId(), "Кол-во юзеров: " + allChatId.size()));
        String msgToMailing = messageToMailing.substring(messageToMailing.indexOf(" "));
        int cursor = 0;
        int half = allChatId.size() / 2;
        int j = 0;
        bot.execute(new SendMessage(Env.instance().getSecretId(), "Старт рассылки."));
        for (Long chatId : allChatId) {
            bot.execute(new SendMessage(chatId, msgToMailing));
            cursor++;
            if (cursor == 25) {
                cursor = 0;
                Thread.sleep(1500);
            }
            j++;
            if (j == half) {
                bot.execute(new SendMessage(Env.instance().getSecretId(), "Половина пользователей (" + half + ") получили рассылку"));
            } else if (j == 200) {
                bot.execute(new SendMessage(Env.instance().getSecretId(), "200 человек получили рассылку"));
            } else if (j == 1000) {
                bot.execute(new SendMessage(Env.instance().getSecretId(), "1000 человек получили рассылку"));
            } else if (j == 3500) {
                bot.execute(new SendMessage(Env.instance().getSecretId(), "3500 человек получили рассылку"));
            }
        }
        bot.execute(new SendMessage(Env.instance().getSecretId(), "Рассылка окончена. " + allChatId.size() + " юзеров получили рассылку."));
    }
}

