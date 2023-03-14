package core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.MenuButtonCommands;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatMenuButton;
import com.pengrad.telegrambot.request.SetMyCommands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        ParsedDictionary rusLezgiDictionary = new ParsedDictionary();
        rusLezgiDictionary.parse("D:/projects/GafarganBot/src/main/resources/rus_lezgi_dict_hajiyev.json");

        Map<Long, LanguageChoice> chatLang = new HashMap<>();

        TelegramBot bot = new TelegramBot("5664347820:AAGEZQcSEoRkdQqcxRbcIKo0vIe2Lymqh3c");

// Подписка на обновления
        bot.setUpdatesListener(updates -> {
            for (var update : updates) {
                var message = update.message();
                if (message == null) {
                    continue;
                }
                long chatId = message.chat().id();

                String textMsg = message.text();
                if (textMsg == null) {
                    continue;
                }
                String userMessage = textMsg.toUpperCase();

                if ("/START".equals(userMessage)) {
                    var sendMessage = new SendMessage(chatId, "Вун атуй, рагъ атуй!");
                    sendMessage.parseMode(ParseMode.HTML);
                    bot.execute(sendMessage);
                } else if (rusLezgiDictionary.map.containsKey(userMessage)) {
                    List<String> rsl = rusLezgiDictionary.map.get(userMessage);
                    String msgStr = String.join("\n", rsl).replaceAll(">", ">>>")
                            .replaceAll("<", "<i>")
                            .replaceAll(">>>", "</i>")
                            .replaceAll("\\{", "<b>")
                            .replaceAll("}", "</b>");
                    var sendMessage = new SendMessage(chatId, msgStr);
                    sendMessage.parseMode(ParseMode.HTML);
                    bot.execute(sendMessage);
                } else {
                    bot.execute(new SendMessage(chatId, "Не найдено"));
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}