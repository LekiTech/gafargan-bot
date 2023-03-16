package core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {

        TelegramBot bot = new TelegramBot("");

        ParsedDictionary rusLezgiDictionary = new ParsedDictionary();
        rusLezgiDictionary.parse("D:/projects/GafarganBot/src/main/resources/rus_lezgi_dict_hajiyev.json");
        ParsedDictionary lezgiRusDictionary = new ParsedDictionary();
        lezgiRusDictionary.parse("D:/projects/GafarganBot/src/main/resources/lezgi_rus_dict_babakhanov.json");

        Map<Long, String> chatLang = new HashMap<>();

// Подписка на обновления
        bot.setUpdatesListener(updates -> {
            for (var update : updates) {
                var message = update.message(); // Получаем сообщение
                if (message == null) {
                    continue;
                }
                long chatId = message.chat().id(); // Получаем ID пользователя
                String textMsg = message.text();
                if (textMsg == null) {
                    continue;
                }
                String userMessage = textMsg.toUpperCase();

                if ("/START".equals(userMessage)) {
                    var sendMessage = new SendMessage(chatId, "Вун атуй, рагъ атуй!\nДобро пожаловать!");
                    sendMessage.parseMode(ParseMode.HTML);
                    bot.execute(sendMessage);
                    bot.execute(new SendMessage(chatId, "Выберите словарь:\n"
                            + "/rus_lezgi - Русско-Лезгинский\n"
                            + "/lezgi_rus - Лезгинско-Русский"));
                } else if (userMessage.equals("/RUS_LEZGI")) {
                    chatLang.put(chatId, "/RUS_LEZGI");
                    bot.execute(new SendMessage(chatId, "Выбран Русско-Лезгинский словарь.\n " +
                            "\nВведите слово на русском языке."));
                } else if (userMessage.equals("/LEZGI_RUS")) {
                    chatLang.put(chatId, "/LEZGI_RUS");
                    bot.execute(new SendMessage(chatId, "Выбран Лезгинско-Русский словарь.\n" +
                            "\nВведите слово на лезгинском языке."));
                }

                if (chatLang.get(chatId) == null) {
                    continue;
                }

                if (chatLang.get(chatId).equals("/RUS_LEZGI") && (rusLezgiDictionary.map.containsKey(userMessage))) {
                    List<String> translations = rusLezgiDictionary.map.get(userMessage);
                    String msgStr = convertToHtml(translations);
                    var sendMessage = new SendMessage(chatId, msgStr);
                    sendMessage.parseMode(ParseMode.HTML);
                    bot.execute(sendMessage);
                } else if (chatLang.get(chatId).equals("/LEZGI_RUS") && (lezgiRusDictionary.map.containsKey(userMessage))) {
                    List<String> translations = lezgiRusDictionary.map.get(userMessage);
                    String msgStr = convertToHtml(translations);
                    var sendMessage = new SendMessage(chatId, msgStr);
                    sendMessage.parseMode(ParseMode.HTML);
                    bot.execute(sendMessage);
                } else if (chatLang.get(chatId).equals("/LEZGI_RUS")
                        && !lezgiRusDictionary.map.containsKey(userMessage)
                        && !("/START".equals(userMessage))
                        && !("/LEZGI_RUS".equals(userMessage))
                        && !("/RUS_LEZGI".equals(userMessage))) {
                    bot.execute(new SendMessage(chatId, "Ничего не найдено"));
                } else if (chatLang.get(chatId).equals("/RUS_LEZGI")
                        && !rusLezgiDictionary.map.containsKey(userMessage)
                        && !("/START".equals(userMessage))
                        && !("/LEZGI_RUS".equals(userMessage))
                        && !("/RUS_LEZGI".equals(userMessage))) {
                    bot.execute(new SendMessage(chatId, "Ничего не найдено"));
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static String convertToHtml(List<String> translations) {
        String msgStr = String.join("\n", translations).replaceAll(">", ">>>")
                .replaceAll("<", "<i>")
                .replaceAll(">>>", "</i>")
                .replaceAll("\\{", "<b>")
                .replaceAll("}", "</b>");
        return msgStr;
    }
}