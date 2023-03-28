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
    private final static String START = "/start";
    private final static String LEZGI_RUS_BABAKHANOV = "/lezgi_rus_babakhanov";
    private final static String LEZGI_RUS_TALIBOV_HAJIYEV = "/lezgi_rus_talibov_hajiyev";
    private final static String RUS_LEZGI = "/rus_lezgi_hajiyev";
    private final static String INFO = "/info";

    public static void main(String[] args) throws IOException {

        TelegramBot bot = new TelegramBot("");

        ParsedDictionary rusLezgiDict = new ParsedDictionary();
        rusLezgiDict.parse("src/main/resources/rus_lezgi_dict_hajiyev.json");
        ParsedDictionary lezgiRusDictBabakhanov = new ParsedDictionary();
        lezgiRusDictBabakhanov.parse("src/main/resources/lezgi_rus_dict_babakhanov.json");
        ParsedDictionary lezgiRusDictTalibovHajiyev = new ParsedDictionary();
        lezgiRusDictTalibovHajiyev.parse("src/main/resources/lezgi_rus_dict_talibov_hajiyev.json");

        Map<Long, String> chatLanguage = new HashMap<>();
        Map<String, String> clickButtLezgiBabakhanov = new HashMap<>();
        Map<String, String> clickButtLezgiTalibovHajiyev = new HashMap<>();
        Map<String, String> clickButtonsRus = new HashMap<>();

        bot.setUpdatesListener(updates -> {
            try {
                for (var update : updates) {
                    /* Обработка buttons */
                    if (update.callbackQuery() != null) {
                        CallbackQuery callbackQuery = update.callbackQuery();
                        Message buttonMessage = callbackQuery.message();
                        long chatId = buttonMessage.chat().id();
                        String data = callbackQuery.data();
                        if (RUS_LEZGI.equals(data)) {
                            bot.execute(new SendMessage(chatId, "Выбран Русско-Лезгинский словарь\uD83D\uDCD5(Гаджиев М.М.)\n\n"
                                    + "Вводите слово на русском языке."));
                            bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                            chatLanguage.put(chatId, RUS_LEZGI);
                        } else if (LEZGI_RUS_BABAKHANOV.equals(data)) {
                            bot.execute(new SendMessage(chatId, "Выбран Лезгинско-Русский словарь\uD83D\uDCD7(Бабаханов М.Б.)\n\n"
                                    + "Буквы с палочкой (пI, цI, кI ...) пишите через латинскую «i» или единицу «1», например:\n"
                                    + "кiвал или к1вал\n\n"
                                    + "Введите слово на лезгинском языке"));
                            bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                            chatLanguage.put(chatId, LEZGI_RUS_BABAKHANOV);
                        } else if (LEZGI_RUS_TALIBOV_HAJIYEV.equals(data)) {
                            bot.execute(new SendMessage(chatId, "Выбран Лезгинско-Русский словарь\uD83D\uDCD9(Талибов Б., Гаджиев М.)\n\n"
                                    + "Буквы с палочкой (пI, цI, кI ...) пишите через латинскую «i» или единицу «1»:\n"
                                    + "кiвал - к1вал\n\n"
                                    + "Введите слово на лезгинском языке"));
                            bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                            chatLanguage.put(chatId, LEZGI_RUS_TALIBOV_HAJIYEV);
                        } else if (clickButtLezgiBabakhanov.containsKey(data) && LEZGI_RUS_BABAKHANOV.equals(clickButtLezgiBabakhanov.get(data))) {
                            sendAnswerFromButtons(bot, lezgiRusDictBabakhanov, callbackQuery, chatId, data);
                        } else if (clickButtLezgiTalibovHajiyev.containsKey(data) && LEZGI_RUS_TALIBOV_HAJIYEV.equals(clickButtLezgiTalibovHajiyev.get(data))) {
                            sendAnswerFromButtons(bot, lezgiRusDictTalibovHajiyev, callbackQuery, chatId, data);
                        } else if (clickButtonsRus.containsKey(data) && RUS_LEZGI.equals(clickButtonsRus.get(data))) {
                            sendAnswerFromButtons(bot, rusLezgiDict, callbackQuery, chatId, data);
                        }
                    }
                    var message = update.message();
                    if (message == null) {
                        continue;
                    }
                    long chatId = message.chat().id();
                    String textMsg = message.text();
                    if (textMsg == null) {
                        continue;
                    }
                    /* Обработка команд */
                    String userMessage = textMsg.toLowerCase().replaceAll("1", "i");
                    if (START.equals(userMessage)) {
                        var sendMessage = new SendMessage(chatId, "Ас-саляму алейкум\uD83D\uDC4B\uD83C\uDFFC\n\n" +
                                "Вун атуй, рагъ атуй!\nДобро пожаловать!");
                        sendMessage.parseMode(ParseMode.HTML);
                        bot.execute(sendMessage);
                        /* Create the inline keyboard */
                        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                                new InlineKeyboardButton[][]{
                                        {new InlineKeyboardButton("Лезгинско-Русский\uD83D\uDCD7(Бабаханов М.Б.)")
                                                .callbackData(LEZGI_RUS_BABAKHANOV)},
                                        {new InlineKeyboardButton("Лезгинско-Русский\uD83D\uDCD9(Талибов Б., Гаджиев М.)")
                                                .callbackData(LEZGI_RUS_TALIBOV_HAJIYEV)},
                                        {new InlineKeyboardButton("Русско-Лезгинский\uD83D\uDCD5(Гаджиев М.М.)")
                                                .callbackData(RUS_LEZGI)}
                                });
                        bot.execute(new SendMessage(chatId, "Выберите словарь\uD83D\uDCDA:")
                                .replyMarkup(inlineKeyboard));
                    } else if (INFO.equals(userMessage)) {
                        String txt = "Разработчик: Артур Магомедов.\n\n"
                                + "Бот-словарь, переводит слова с Лезгинского языка на русский и наоборот. "
                                + "Также бот умеет находить слова, если пользователь вводит их с орфографическими ошибками.\n\n"
                                + "Используемые словари:\n"
                                + "- Лезгинско-русский словарь (Б.Талибов, М.Гаджиев)\n"
                                + "- Русско-лезгинский словарь (М.М.Гаджиев)\n"
                                + "- Лезгинско-русский словарь (Бабаханов М.Б.)";
                        bot.execute(new SendMessage(chatId, txt));
                    } else if (RUS_LEZGI.equals(userMessage)) {
                        chatLanguage.put(chatId, RUS_LEZGI);
                        bot.execute(new SendMessage(chatId, "Выбран Русско-Лезгинский словарь\uD83D\uDCD5(Гаджиев М.М.)\n\n"
                                + "Введите слово на русском языке."));
                    } else if (LEZGI_RUS_BABAKHANOV.equals(userMessage)) {
                        chatLanguage.put(chatId, LEZGI_RUS_BABAKHANOV);
                        bot.execute(new SendMessage(chatId, "Выбран Лезгинско-Русский словарь\uD83D\uDCD7(Бабаханов М.Б.)\n\n"
                                + "Введите слово на лезгинском языке.\n"
                                + "Символ «I» (палочка: кI, тI, пI...) вводите через латинкую букву «i»."));
                    } else if (LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage)) {
                        chatLanguage.put(chatId, LEZGI_RUS_TALIBOV_HAJIYEV);
                        bot.execute(new SendMessage(chatId, "Выбран Лезгинско-Русский словарь\uD83D\uDCD9(Талибов Б., Гаджиев М.)\n\n"
                                + "Введите слово на лезгинском языке.\n"
                                + "Символ «I» (палочка: кI, тI, пI...) вводите через латинкую букву «i»."));
                    }

                    if (RUS_LEZGI.equals(chatLanguage.get(chatId))
                            && (rusLezgiDict.map.containsKey(userMessage))) {
                        sendAnswerFromDictionary(bot, rusLezgiDict, chatId, userMessage);
                    } else if (LEZGI_RUS_BABAKHANOV.equals(chatLanguage.get(chatId))
                            && (lezgiRusDictBabakhanov.map.containsKey(userMessage))) {
                        sendAnswerFromDictionary(bot, lezgiRusDictBabakhanov, chatId, userMessage);
                    } else if (LEZGI_RUS_TALIBOV_HAJIYEV.equals(chatLanguage.get(chatId))
                            && (lezgiRusDictTalibovHajiyev.map.containsKey(userMessage))) {
                        sendAnswerFromDictionary(bot, lezgiRusDictTalibovHajiyev, chatId, userMessage);
                    } else if (LEZGI_RUS_BABAKHANOV.equals(chatLanguage.get(chatId))
                            && !(START.equals(userMessage))
                            && !(LEZGI_RUS_BABAKHANOV.equals(userMessage))
                            && !(LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage))
                            && !(INFO.equals(userMessage))
                            && !(RUS_LEZGI.equals(userMessage))) {
                        sendAnswerToUser(bot, lezgiRusDictBabakhanov, clickButtLezgiBabakhanov, chatId, userMessage, LEZGI_RUS_BABAKHANOV);
                    } else if (LEZGI_RUS_TALIBOV_HAJIYEV.equals(chatLanguage.get(chatId))
                            && !(START.equals(userMessage))
                            && !(LEZGI_RUS_BABAKHANOV.equals(userMessage))
                            && !(LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage))
                            && !(INFO.equals(userMessage))
                            && !(RUS_LEZGI.equals(userMessage))) {
                        sendAnswerToUser(bot, lezgiRusDictTalibovHajiyev, clickButtLezgiTalibovHajiyev, chatId, userMessage, LEZGI_RUS_TALIBOV_HAJIYEV);
                    } else if (RUS_LEZGI.equals(chatLanguage.get(chatId))
                            && !(START.equals(userMessage))
                            && !(LEZGI_RUS_BABAKHANOV.equals(userMessage))
                            && !(LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage))
                            && !(INFO.equals(userMessage))
                            && !(RUS_LEZGI.equals(userMessage))) {
                        sendAnswerToUser(bot, rusLezgiDict, clickButtonsRus, chatId, userMessage, RUS_LEZGI);
                        /*  Когда ни один словарь не выбран */
                    } else if (chatLanguage.isEmpty()
                            && !START.equals(userMessage)
                            && !INFO.equals(userMessage)
                            && !RUS_LEZGI.equals(userMessage)
                            && !LEZGI_RUS_BABAKHANOV.equals(userMessage)
                            && !LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage)) {
                        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                                new InlineKeyboardButton[][]{
                                        {new InlineKeyboardButton("Лезгинско-Русский\uD83D\uDCD7(Бабаханов М.Б.)")
                                                .callbackData(LEZGI_RUS_BABAKHANOV)},
                                        {new InlineKeyboardButton("Лезгинско-Русский\uD83D\uDCD9(Талибов Б., Гаджиев М.)")
                                                .callbackData(LEZGI_RUS_TALIBOV_HAJIYEV)},
                                        {new InlineKeyboardButton("Русско-Лезгинский\uD83D\uDCD5(Гаджиев М.М.)")
                                                .callbackData(RUS_LEZGI)}
                                });
                        bot.execute(new SendMessage(chatId, "Пожалуйста, выберите словарь\uD83D\uDCDA:")
                                .replyMarkup(inlineKeyboard));
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void sendAnswerFromDictionary(TelegramBot bot, ParsedDictionary dictionary, long chatId,
                                                 String userMessage) {
        List<String> translations = dictionary.map.get(userMessage);
        String msgStr = convertToHtml(translations);
        var sendMessage = new SendMessage(chatId, msgStr);
        sendMessage.parseMode(ParseMode.HTML);
        bot.execute(sendMessage);
    }

    private static void sendAnswerFromButtons(TelegramBot bot, ParsedDictionary dictionary, CallbackQuery callbackQuery,
                                              long chatId, String data) {
        List<String> translations = dictionary.map.get(data.toLowerCase());
        String msgStr = convertToHtml(translations);
        var sendMessage = new SendMessage(chatId, msgStr);
        sendMessage.parseMode(ParseMode.HTML);
        bot.execute(new SendMessage(chatId, data + "\n\n"));
        bot.execute(sendMessage);
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
    }

    private static void sendAnswerToUser(TelegramBot bot, ParsedDictionary dictionary, Map<String, String> clickButtons,
                                         long chatId, String userMessage, String language) {
        record WordSim(String word, Double sim) {
        }
        List<WordSim> temp = new ArrayList<>();
        for (String word : dictionary.map.keySet()) {
            double sim = similarity(word, userMessage);
            if (sim >= 0.5) {
                temp.add(new WordSim(word.toLowerCase().replaceAll("i", "I"), sim));
            }
        }
        temp.sort(Comparator.comparing(WordSim::sim).reversed());
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (var wordSim : temp.subList(0, Math.min(7, temp.size()))) {
            List<InlineKeyboardButton> tempList = new ArrayList<>();
            tempList.add(new InlineKeyboardButton(wordSim.word).callbackData(wordSim.word));
            buttons.add(tempList);
            clickButtons.put(wordSim.word, language);
        }
        InlineKeyboardButton[][] inlineKeyboardButton = new InlineKeyboardButton[buttons.size()][1];
        for (int i = 0; i < inlineKeyboardButton.length; i++) {
            for (int j = 0; j < inlineKeyboardButton[i].length; j++) {
                inlineKeyboardButton[i][j] = buttons.get(i).get(j);
            }
        }
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(inlineKeyboardButton);
        if (clickButtons.size() > 0 && temp.size() > 0) {
            bot.execute(new SendMessage(chatId, "Ничего не нашлось\uD83E\uDD14, возможно вы имели ввиду:\n")
                    .replyMarkup(inlineKeyboard));
        } else {
            bot.execute(new SendMessage(chatId, "Ничего не нашлось\uD83E\uDD72. Повторите запрос"));
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