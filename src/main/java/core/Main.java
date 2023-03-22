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

        TelegramBot bot = new TelegramBot("5664347820:AAGEZQcSEoRkdQqcxRbcIKo0vIe2Lymqh3c");

        ParsedDictionary rusLezgiDictionary = new ParsedDictionary();
        rusLezgiDictionary.parse("D:/projects/GafarganBot/src/main/resources/rus_lezgi_dict_hajiyev.json");
        ParsedDictionary lezgiRusDictionary = new ParsedDictionary();
        lezgiRusDictionary.parse("D:/projects/GafarganBot/src/main/resources/lezgi_rus_dict_babakhanov.json");

        Map<Long, String> chatLang = new HashMap<>();
        Map<String, String> clickButtonsLezgi = new HashMap<>();
        Map<String, String> clickButtonsRus = new HashMap<>();

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


                    else if (clickButtonsLezgi.containsKey(data) && "/lezgi_rus".equals(clickButtonsLezgi.get(data))) {

                        List<String> translations = lezgiRusDictionary.map.get(data.toUpperCase());
                        String msgStr = convertToHtml(translations);
                        var sendMessage = new SendMessage(chatId, msgStr);
                        sendMessage.parseMode(ParseMode.HTML);
                        bot.execute(new SendMessage(chatId, data + "\n\n"));
                        bot.execute(sendMessage);
                        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                    }

                    else if (clickButtonsRus.containsKey(data) && "/rus_lezgi".equals(clickButtonsRus.get(data))) {

                        List<String> translations = rusLezgiDictionary.map.get(data.toUpperCase());
                        String msgStr = convertToHtml(translations);
                        var sendMessage = new SendMessage(chatId, msgStr);
                        sendMessage.parseMode(ParseMode.HTML);
                        bot.execute(new SendMessage(chatId, data + "\n\n"));
                        bot.execute(sendMessage);
                        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
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

                    sendAnswerToUser(bot, lezgiRusDictionary, clickButtonsLezgi, chatId, userMessage, "/lezgi_rus");
                }

                else if (chatLang.get(chatId).equals("/RUS_LEZGI")
                        && !("/START".equals(userMessage))
                        && !("/LEZGI_RUS".equals(userMessage))
                        && !("/RUS_LEZGI".equals(userMessage))) {
                    sendAnswerToUser(bot, rusLezgiDictionary, clickButtonsRus, chatId, userMessage, "/rus_lezgi");
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void sendAnswerToUser(TelegramBot bot, ParsedDictionary dictionary,
                                         Map<String, String> clickButtons, long chatId, String userMessage, String language) {
        record WordSim(String word, Double sim){}

        List<WordSim> temp = new ArrayList<>();
        for (String word : dictionary.map.keySet()) {
            double sim = similarity(word, userMessage);
            if (sim >= 0.5) {
                temp.add(new WordSim(word.toLowerCase().replaceAll("i", "I"), sim));
            }
        }
        //    Сортируем
        temp.sort(Comparator.comparing(WordSim::sim).reversed());
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (var wordSim : temp.subList(0, Math.min(7, temp.size()))) {
            List<InlineKeyboardButton> tempList = new ArrayList<>();
            tempList.add(new InlineKeyboardButton(wordSim.word).callbackData(wordSim.word));
            buttons.add(tempList);
            // сохраняем "words"
            clickButtons.put(wordSim.word, language);
        }

        InlineKeyboardButton[][] inlineKeyboardButton = new InlineKeyboardButton[buttons.size()][1];
        // Добавляем кнопки
        for (int i = 0; i < inlineKeyboardButton.length; i++) {
            for (int j = 0; j < inlineKeyboardButton[i].length; j++) {
                inlineKeyboardButton[i][j] = buttons.get(i).get(j);
            }
        }
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(inlineKeyboardButton);
        // Выводим кнопки
        if (clickButtons.size() > 0 && temp.size() > 0) {

            bot.execute(new SendMessage(chatId, "Ничего не нашлось, возможно вы имели ввиду:\n")
                    .replyMarkup(inlineKeyboard));
        } else {
            bot.execute(new SendMessage(chatId, "Ничего не нашлось. Повторите запрос"));
        }
    }

    private static String convertToHtml(List<String> translations) {
        String msgStr = String.join("\n\n", translations).replaceAll(">", ">>>")
                .replaceAll("<", "<i>")
                .replaceAll(">>>", "</i>")
                .replaceAll("\\{", "<b>")
                .replaceAll("}", "</b>");
        return msgStr;
    }
}