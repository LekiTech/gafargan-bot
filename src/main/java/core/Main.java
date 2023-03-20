package core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.util.*;

import static core.StringSimilarity.similarity;

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

                if (update.callbackQuery() != null) {
                    CallbackQuery callbackQuery = update.callbackQuery();
                    Message message1 = callbackQuery.message();
                    long chatId = message1.chat().id();
                    String data = callbackQuery.data();

                    if (data.equals("/rus_lezgi")) {
                        // Send a response message
                        bot.execute(new SendMessage(chatId, "Выбран Русско-Лезгинский словарь.\n\n" +
                                                          "Вводите слово на русском языке."));
                        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                        chatLang.put(chatId, "/RUS_LEZGI");

                    } else if (data.equals("/lezgi_rus")) {
                        bot.execute(new SendMessage(chatId, "Выбран Лезгинско-Русский словарь.\n\n" +
                                                             "Введите слово на лезгинском языке.\n" +
                                                            "Символ «I» (палочка: кI, тI, пI...) вводите через латинкую букву «i»."));
                        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                        chatLang.put(chatId, "/LEZGI_RUS");
                    }
                }

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

                    // Create the inline keyboard
                    InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton[][]{
                                    {new InlineKeyboardButton("Русско-Лезгинский").callbackData("/rus_lezgi")},
                                    {new InlineKeyboardButton("Лезгинско-Русский").callbackData("/lezgi_rus")}
                            });

                    bot.execute(new SendMessage(chatId, "Выберите словарь:\n")
                            .replyMarkup(inlineKeyboard));

                } else if (userMessage.equals("/RUS_LEZGI")) {
                    chatLang.put(chatId, "/RUS_LEZGI");
                    bot.execute(new SendMessage(chatId, "Выбран Русско-Лезгинский словарь.\n " +
                            "\nВведите слово на русском языке."));
                } else if (userMessage.equals("/LEZGI_RUS")) {
                    chatLang.put(chatId, "/LEZGI_RUS");
                    bot.execute(new SendMessage(chatId, "Выбран Лезгинско-Русский словарь.\n\n" +
                            "Введите слово на лезгинском языке.\n" +
                            "Символ «I» (палочка: кI, тI, пI...) вводите через латинкую букву «i»."));
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
                }

                else if (chatLang.get(chatId).equals("/LEZGI_RUS")
                        && !("/START".equals(userMessage))
                        && !("/LEZGI_RUS".equals(userMessage))
                        && !("/RUS_LEZGI".equals(userMessage))) {
                    StringBuilder options = new StringBuilder("Ничего не нашлось, возможно вы имели ввиду:\n\n");
                    for (String keys : lezgiRusDictionary.map.keySet()) {
                        if (similarity(keys, userMessage) > 0.700) {
                            options.append(keys.toLowerCase());
                            options.append("\n");
                        }
                    }

                    bot.execute(new SendMessage(chatId, options.toString()));
                }
                else if (chatLang.get(chatId).equals("/RUS_LEZGI")
                        && !("/START".equals(userMessage))
                        && !("/LEZGI_RUS".equals(userMessage))
                        && !("/RUS_LEZGI".equals(userMessage))) {
                    StringBuilder options = new StringBuilder("Ничего не нашлось, возможно вы имели ввиду:\n\n");
                    for (String keys : rusLezgiDictionary.map.keySet()) {
                        if (similarity(keys, userMessage) > 0.700) {
                            options.append(keys.toLowerCase());
                            options.append("\n");
                        }
                    }

                    bot.execute(new SendMessage(chatId, options.toString()));
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